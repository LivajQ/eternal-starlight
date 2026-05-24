package cn.leolezury.eternalstarlight.common.entity.projectile;

import com.google.common.collect.Multimap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public abstract class ThrownSpear extends AbstractArrow {
	private static final String TAG_DEALT_DAMAGE = "dealt_damage";

	private static final EntityDataAccessor<Boolean> FOIL = SynchedEntityData.defineId(ThrownSpear.class, EntityDataSerializers.BOOLEAN);
	private boolean dealtDamage;
	private ItemStack weaponItem = ItemStack.EMPTY;

	public ThrownSpear(EntityType<? extends ThrownSpear> type, Level level) {
		super(type, level);
	}

	public ThrownSpear(EntityType<? extends ThrownSpear> type, Level level, @Nullable LivingEntity owner, double x, double y, double z, ItemStack pickupItemStack) {
		super(type, level);
		this.moveTo(x, y, z, this.getYRot(), this.getXRot());
		this.setOwner(owner);

		//this.pickupItem = pickupItemStack.copy();
		this.weaponItem = pickupItemStack == null ? ItemStack.EMPTY : pickupItemStack.copy();
		this.entityData.set(FOIL, this.weaponItem.hasFoil());
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(FOIL, false);
	}

	@Override
	public void tick() {
		if (this.inGroundTime > 4) {
			this.dealtDamage = true;
		}
		super.tick();
	}

	public boolean isFoil() {
		return this.entityData.get(FOIL);
	}

	@Nullable
	@Override
	protected EntityHitResult findHitEntity(Vec3 startVec, Vec3 endVec) {
		return this.dealtDamage ? null : super.findHitEntity(startVec, endVec);
	}

	private double getItemDamage(@Nullable AttributeInstance instance) {
		double baseValue = instance != null ? instance.getBaseValue() : 1.0;
		double result = baseValue;

		if (this.weaponItem.isEmpty()) {
			return result;
		}

		Multimap<Attribute, AttributeModifier> modifiers = this.weaponItem.getAttributeModifiers(EquipmentSlot.MAINHAND);
		Collection<AttributeModifier> dmgMods = modifiers.get(Attributes.ATTACK_DAMAGE);

		for (AttributeModifier modifier : dmgMods) {
			double amount = modifier.getAmount();
			switch (modifier.getOperation()) {
				case ADDITION -> result += amount;
				case MULTIPLY_BASE -> result += baseValue * amount;
				case MULTIPLY_TOTAL -> result += result * amount;
			}
		}

		return result;
	}

	@Override
	protected void onHitEntity(EntityHitResult result) {
		Entity target = result.getEntity();
		Entity owner = this.getOwner();

		float damage = getDamageScale(target) *
			(float) getItemDamage(owner instanceof LivingEntity living
				? living.getAttribute(Attributes.ATTACK_DAMAGE)
				: null);

		DamageSource damageSource = this.damageSources().thrown(this, owner == null ? this : owner);

		this.dealtDamage = true;

		if (target.hurt(damageSource, damage)) {
			if (target.getType() == EntityType.ENDERMAN) {
				return;
			}

			if (target instanceof LivingEntity livingTarget && owner instanceof LivingEntity livingOwner) {
				EnchantmentHelper.doPostHurtEffects(livingTarget, livingOwner);
				EnchantmentHelper.doPostDamageEffects(livingOwner, livingTarget);
			}

			if (target instanceof LivingEntity living) {
				this.applyKnockback(living, 0.4F);
				this.doPostHurtEffects(living);
			}
		}

		this.setDeltaMovement(this.getDeltaMovement().multiply(-0.01, -0.1, -0.01));
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

	protected float getDamageScale(Entity target) {
		return 2.0F;
	}

	/*
	@Override
	protected void hitBlockEnchantmentEffects(ServerLevel level, BlockHitResult hitResult, ItemStack stack) {
		Vec3 location = hitResult.getBlockPos().clampLocationWithin(hitResult.getLocation());
		EnchantmentHelper.onHitBlock(level, stack, this.getOwner() instanceof LivingEntity livingentity ? livingentity : null, this, null, location, level.getBlockState(hitResult.getBlockPos()), item -> this.kill());
	}
	 */

	@Override
	protected boolean tryPickup(Player player) {
		return super.tryPickup(player) || this.isNoPhysics() && this.ownedBy(player) && player.getInventory().add(this.getPickupItem());
	}

	@Override
	public void playerTouch(Player entity) {
		if (this.ownedBy(entity) || this.getOwner() == null) {
			super.playerTouch(entity);
		}
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		this.dealtDamage = compound.getBoolean(TAG_DEALT_DAMAGE);
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putBoolean(TAG_DEALT_DAMAGE, this.dealtDamage);
	}

	@Override
	public boolean shouldRender(double x, double y, double z) {
		return true;
	}
}
