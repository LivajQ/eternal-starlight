package cn.leolezury.eternalstarlight.common.enchantment;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class PoisoningEnchantment extends Enchantment {

	public PoisoningEnchantment() {
		super(Rarity.COMMON, EnchantmentCategory.ARMOR, new EquipmentSlot[]{
				EquipmentSlot.HEAD,
				EquipmentSlot.CHEST,
				EquipmentSlot.LEGS,
				EquipmentSlot.FEET
			}
		);
	}

	@Override
	public int getMinCost(int level) {
		return 1 + (level - 1) * 11;
	}

	@Override
	public int getMaxCost(int level) {
		return 12 + (level - 1) * 11;
	}

	@Override
	public int getMaxLevel() {
		return 4;
	}

	@Override
	public boolean canEnchant(ItemStack stack) {
		return stack.getItem() instanceof ArmorItem || super.canEnchant(stack);
	}

	@Override
	public void doPostHurt(LivingEntity wearer, Entity attackerEntity, int level) {
		if (!(attackerEntity instanceof LivingEntity attacker)) return;

		int durationTicks = (int)((2.5F + 0.5F * (level - 1)) * 20);

		int amplifier = level;

		attacker.addEffect(new MobEffectInstance(
			MobEffects.POISON,
			durationTicks,
			amplifier
		));

		super.doPostHurt(wearer, attackerEntity, level);
	}
}
