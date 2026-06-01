package cn.leolezury.eternalstarlight.common.enchantment;

import cn.leolezury.eternalstarlight.common.data.ESEnchantments;
import cn.leolezury.eternalstarlight.common.util.ESTags;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

public class PrecisionEnchantment extends Enchantment {

	public PrecisionEnchantment() {
		super(Rarity.COMMON, EnchantmentCategory.WEAPON, new EquipmentSlot[]{ EquipmentSlot.MAINHAND });
	}

	@Override
	public int getMinCost(int level) {
		return 1 + (level - 1) * 11;
	}

	@Override
	public int getMaxCost(int level) {
		return 21 + (level - 1) * 11;
	}

	@Override
	public int getMaxLevel() {
		return 5;
	}

	@Override
	public boolean canEnchant(ItemStack stack) {
		return stack.is(ESTags.Items.BOOMERANG_ENCHANTABLE)
			|| super.canEnchant(stack);
	}

	public static float getCritChanceBonus(ItemStack stack) {
		Enchantment ench = BuiltInRegistries.ENCHANTMENT.get(ESEnchantments.PRECISION.getResourceKey());
		int level = EnchantmentHelper.getItemEnchantmentLevel(ench, stack);

		if (level <= 0) return 0F;

		return 0.16F * level;
	}
}
