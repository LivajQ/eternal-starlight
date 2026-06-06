package cn.leolezury.eternalstarlight.common.enchantment;

import cn.leolezury.eternalstarlight.common.data.ESEnchantments;
import cn.leolezury.eternalstarlight.common.util.ESTags;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

public class OverheatEnchantment extends Enchantment {

	public OverheatEnchantment() {
		super(Rarity.UNCOMMON, EnchantmentCategory.WEAPON, new EquipmentSlot[]{ EquipmentSlot.MAINHAND });
	}

	@Override
	public int getMinCost(int level) {
		return 20 + (level - 1) * 11;
	}

	@Override
	public int getMaxCost(int level) {
		return 30 + (level - 1) * 11;
	}

	@Override
	public int getMaxLevel() {
		return 2;
	}

	@Override
	public boolean canEnchant(ItemStack stack) {
		return stack.is(ESTags.Items.SEEDS_LAUNCHER_ENCHANTABLE)
			|| super.canEnchant(stack);
	}

	@Override
	protected boolean checkCompatibility(Enchantment other) {

		boolean isExclusive = BuiltInRegistries.ENCHANTMENT
			.getTag(ESTags.Enchantments.SEEDS_LAUNCHER_EXCLUSIVE)
			.map(tag -> tag.stream().anyMatch(h -> h.is(BuiltInRegistries.ENCHANTMENT.getKey(other))))
			.orElse(false);

		if (isExclusive) {
			return false;
		}

		return super.checkCompatibility(other);
	}

	public static void applyIgniteEffect(ItemStack launcher, Entity projectile, int level) {
		Enchantment ench = BuiltInRegistries.ENCHANTMENT.get(ESEnchantments.OVERHEAT.getResourceKey());
		int enchLevel = EnchantmentHelper.getItemEnchantmentLevel(ench, launcher);

		if (enchLevel <= 0) return;

		int ticks = 100 * enchLevel;

		projectile.setSecondsOnFire(ticks / 20);
	}
}
