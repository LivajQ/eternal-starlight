package cn.leolezury.eternalstarlight.common.enchantment;

import cn.leolezury.eternalstarlight.common.entity.projectile.ChainOfSouls;
import cn.leolezury.eternalstarlight.common.util.ESTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class SoulSnatcherEnchantment extends Enchantment {

	public SoulSnatcherEnchantment() {
		super(Rarity.COMMON, EnchantmentCategory.WEAPON, new EquipmentSlot[]{ EquipmentSlot.MAINHAND });
	}

	@Override
	public int getMinCost(int level) {
		return 25 + (level - 1) * 25;
	}

	@Override
	public int getMaxCost(int level) {
		return 75 + (level - 1) * 25;
	}

	@Override
	public int getMaxLevel() {
		return 3;
	}

	@Override
	public boolean canEnchant(ItemStack stack) {
		return stack.is(ESTags.Items.CHAIN_OF_SOULS_ENCHANTABLE)
			|| super.canEnchant(stack);
	}

	@Override
	public void doPostAttack(LivingEntity attacker, Entity target, int level) {
		if (!(target instanceof LivingEntity victim)) return;

		DamageSource source = victim.getLastDamageSource();
		if (source == null) return;

		Entity direct = source.getDirectEntity();
		if (!(direct instanceof ChainOfSouls)) return;

		float bonus = 0.5F * level;

		victim.hurt(source, bonus);

		super.doPostAttack(attacker, target, level);
	}

}
