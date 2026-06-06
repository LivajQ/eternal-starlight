package cn.leolezury.eternalstarlight.common.item.combat;

import cn.leolezury.eternalstarlight.common.entity.projectile.ThrownBoomerang;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public abstract class BoomerangItem extends TieredItem {

	protected final float attackDamage;
	protected final float attackSpeed;

	public BoomerangItem(Tier tier, int damage, float speed, Properties props) {
		super(tier, props);
		this.attackDamage = damage + tier.getAttackDamageBonus();
		this.attackSpeed = speed;
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
		if (slot == EquipmentSlot.MAINHAND) {
			ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();

			builder.put(
				Attributes.ATTACK_DAMAGE,
				new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "boomerang_damage", this.attackDamage, AttributeModifier.Operation.ADDITION)
			);

			builder.put(
				Attributes.ATTACK_SPEED,
				new AttributeModifier(BASE_ATTACK_SPEED_UUID, "boomerang_speed", this.attackSpeed, AttributeModifier.Operation.ADDITION)
			);

			return builder.build();
		}

		return super.getDefaultAttributeModifiers(slot);
	}

	public abstract ThrownBoomerang createBoomerang(Level level, @Nullable LivingEntity owner, double x, double y, double z, ItemStack pickupItemStack);

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);

		if (isTooDamagedToUse(stack)) {
			return InteractionResultHolder.fail(stack);
		}

		if (!level.isClientSide) {
			stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND));

			Vec3 shootPos = player.getEyePosition();
			ThrownBoomerang boomerang = createBoomerang(level, player, shootPos.x, shootPos.y, shootPos.z, stack);
			boomerang.setNoGravity(true);
			boomerang.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 2.0F, 0.3F);

			if (player.getAbilities().instabuild) {
				boomerang.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
			}

			level.addFreshEntity(boomerang);

			if (!player.getAbilities().instabuild) {
				player.getInventory().removeItem(stack);
			}

			player.getCooldowns().addCooldown(this, 15);
		}

		player.awardStat(Stats.ITEM_USED.get(this));
		return InteractionResultHolder.consume(stack);
	}

	private static boolean isTooDamagedToUse(ItemStack stack) {
		return stack.getDamageValue() >= stack.getMaxDamage() - 1;
	}

	@Override
	public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		return true;
	}

	@Override
	public int getEnchantmentValue() {
		return 1;
	}
}
