package cn.leolezury.eternalstarlight.common.enchantment;

import cn.leolezury.eternalstarlight.common.entity.projectile.ShotSeeds;
import cn.leolezury.eternalstarlight.common.util.ESTags;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class GlacialSowingEnchantment extends Enchantment {

	public GlacialSowingEnchantment() {
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


	@Override
	public void doPostAttack(LivingEntity attacker, Entity target, int level) {
		if (!(target instanceof LivingEntity victim)) return;

		DamageSource source = victim.getLastDamageSource();
		if (source == null) return;

		Entity direct = source.getDirectEntity();
		if (!(direct instanceof ShotSeeds)) return;

		int ticks = (int)(4.0F * level * 20);

		int current = victim.getTicksFrozen();
		victim.setTicksFrozen(Math.min(current + ticks, 600));

		super.doPostAttack(attacker, target, level);
	}
}
