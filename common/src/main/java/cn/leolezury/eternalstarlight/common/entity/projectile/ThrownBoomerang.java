package cn.leolezury.eternalstarlight.common.entity.projectile;

import cn.leolezury.eternalstarlight.common.data.ESEnchantments;
import cn.leolezury.eternalstarlight.common.enchantment.PrecisionEnchantment;
import cn.leolezury.eternalstarlight.common.util.ESEntityUtil;
import com.google.common.collect.Multimap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Comparator;

public abstract class ThrownBoomerang extends AbstractArrow {
	private static final String TAG_DEALT_DAMAGE = "dealt_damage";

	private boolean dealtDamage;
	private ItemStack weaponItem = ItemStack.EMPTY;

	public ThrownBoomerang(EntityType<? extends ThrownBoomerang> type, Level level) {
		super(type, level);
	}

	public ThrownBoomerang(EntityType<? extends ThrownBoomerang> type, Level level, @Nullable LivingEntity owner, double x, double y, double z, ItemStack pickupItemStack) {
		super(type, x, y, z, level);
		this.weaponItem = pickupItemStack == null ? ItemStack.EMPTY : pickupItemStack.copy();
		this.setOwner(owner);
	}

	@Override
	public void tick() {
		Entity owner = this.getOwner();
		if (this.inGround || (owner == null && tickCount > 40) || (owner != null && distanceTo(owner) > 15 + owner.getBbWidth() / 2)) {
			this.dealtDamage = true;
		}
		if (!level().isClientSide && !this.dealtDamage) {
			float homing = 0;
			if (level() instanceof ServerLevel serverLevel && this.weaponItem != null) {
				homing = ESEnchantments.modifyBoomerangHomingStrength(this.getWeaponItem(), homing);
			}
			homing = Mth.clamp(homing, 0, 1);
			Vec3 homingTarget = null;
			for (LivingEntity living : level().getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(3)).stream().sorted(Comparator.comparingDouble(i -> i.distanceTo(this))).toList()) {
				if (ESEntityUtil.shouldHarm(owner, living)) {
					homingTarget = living.position().add(0, living.getBbHeight() / 2, 0);
					break;
				}
			}
			if (homingTarget != null && homing > 0 && getDeltaMovement().normalize().dot(homingTarget.subtract(position()).normalize()) > 0.5) {
				this.setDeltaMovement(this.getDeltaMovement().normalize().scale(1 - homing).add(homingTarget.subtract(position()).normalize().scale(homing)).normalize().scale(getDeltaMovement().length()));
			}
		}
		if (owner instanceof Player player) {
			float pickupRadius = 0;
			if (level() instanceof ServerLevel serverLevel && this.getWeaponItem() != null) {
				pickupRadius = ESEnchantments.modifyBoomerangPickupRadius(this.getWeaponItem(), pickupRadius);
			}
			if (pickupRadius > 0) {
				for (ItemEntity item : level().getEntitiesOfClass(ItemEntity.class, getBoundingBox().inflate(pickupRadius))) {
					item.playerTouch(player);
				}
			}
		}
		if ((this.dealtDamage || this.isNoPhysics()) && owner != null) {
			this.inGround = false;
			if (!(owner instanceof Player) && !level().isClientSide) {
				this.discard();
			}
			if (!this.isAcceptableReturnOwner()) {
				if (!this.level().isClientSide && this.pickup == Pickup.ALLOWED) {
					this.spawnAtLocation(this.getPickupItem(), 0.1F);
				}

				this.discard();
			} else {
				this.setNoPhysics(true);
				Vec3 diff = owner.getEyePosition().subtract(this.position());
				double speed = Math.max(getDeltaMovement().length(), 1.5);
				if (speed > diff.length()) {
					speed = diff.length();
				}
				this.setDeltaMovement(diff.normalize().scale(speed));
			}
		}

		super.tick();
	}

	private boolean isAcceptableReturnOwner() {
		Entity entity = this.getOwner();
		if (entity != null && entity.isAlive()) {
			return !(entity instanceof ServerPlayer) || !entity.isSpectator();
		} else {
			return false;
		}
	}

	public ItemStack getWeaponItem() {
		return this.weaponItem;
	}

	@Nullable
	@Override
	protected EntityHitResult findHitEntity(Vec3 vec3, Vec3 vec32) {
		return this.dealtDamage ? null : super.findHitEntity(vec3, vec32);
	}

	private double getItemDamage(AttributeInstance instance) {
		double base = instance != null ? instance.getBaseValue() : 1.0;
		double result = base;

		if (weaponItem.isEmpty()) return result;

		Multimap<Attribute, AttributeModifier> modifiers = weaponItem.getAttributeModifiers(EquipmentSlot.MAINHAND);

		Collection<AttributeModifier> dmgMods = modifiers.get(Attributes.ATTACK_DAMAGE);

		for (AttributeModifier mod : dmgMods) {
			switch (mod.getOperation()) {
				case ADDITION -> result += mod.getAmount();
				case MULTIPLY_BASE -> result += base * mod.getAmount();
				case MULTIPLY_TOTAL -> result += result * mod.getAmount();
			}
		}

		return result;
	}

	@Override
	protected void onHitEntity(EntityHitResult hitResult) {
		Entity entity = hitResult.getEntity();
		Entity owner = this.getOwner();

		float damage = (float) getItemDamage(
			owner instanceof LivingEntity living ? living.getAttribute(Attributes.ATTACK_DAMAGE) : null
		);

		DamageSource source = this.damageSources().thrown(this, owner);

		float critChance = 0;
		critChance = ESEnchantments.modifyBoomerangCritChance(this.weaponItem, critChance);
		boolean critSuccess = false;

		if (this.level().getRandom().nextFloat() < critChance) {
			damage *= 1.5F;
			critSuccess = true;
		}

		this.dealtDamage = true;

		if (entity.hurt(source, damage)) {
			if (entity.getType() == EntityType.ENDERMAN) {
				return;
			}

			if (entity instanceof LivingEntity livingTarget && owner instanceof LivingEntity livingOwner) {
				EnchantmentHelper.doPostHurtEffects(livingTarget, livingOwner);
				EnchantmentHelper.doPostDamageEffects(livingOwner, livingTarget);
			}

			if (entity instanceof LivingEntity livingEntity) {
				applyKnockback(livingEntity, 0.4F);
				this.doPostHurtEffects(livingEntity);
			}

			if (critSuccess && owner instanceof Player player) {
				player.crit(entity);
			}
		}

		this.playSound(SoundEvents.PLAYER_ATTACK_CRIT, 1.0F, 1.0F);
	}

	protected void applyKnockback(LivingEntity target, float strength) {
		Vec3 dir = this.getDeltaMovement();
		if (dir.lengthSqr() == 0.0) return;

		Vec3 n = dir.normalize().scale(strength);
		target.push(n.x, 0.1F, n.z);
		target.hurtMarked = true;
	}

	@Override
	protected SoundEvent getDefaultHitGroundSoundEvent() {
		return SoundEvents.PLAYER_ATTACK_CRIT;
	}

	@Override
	protected boolean tryPickup(Player player) {
		return super.tryPickup(player) || this.isNoPhysics() && this.ownedBy(player) && player.getInventory().add(this.getPickupItem());
	}

	@Override
	public void playerTouch(Player player) {
		if (this.ownedBy(player) || this.getOwner() == null) {
			super.playerTouch(player);
		}
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compoundTag) {
		super.readAdditionalSaveData(compoundTag);
		this.dealtDamage = compoundTag.getBoolean(TAG_DEALT_DAMAGE);
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compoundTag) {
		super.addAdditionalSaveData(compoundTag);
		compoundTag.putBoolean(TAG_DEALT_DAMAGE, this.dealtDamage);
	}

	@Override
	protected float getWaterInertia() {
		return 0.99F;
	}

	@Override
	public boolean shouldRender(double d, double e, double f) {
		return true;
	}

}