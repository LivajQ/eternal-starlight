package cn.leolezury.eternalstarlight.common.enchantment;

import cn.leolezury.eternalstarlight.common.entity.projectile.ChainOfSouls;
import cn.leolezury.eternalstarlight.common.util.ESTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class TracingEnchantment extends Enchantment {

	public TracingEnchantment() {
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

		float seconds = 4.5F + 2.5F * (level - 1);
		int ticks = (int)(seconds * 20);

		victim.addEffect(new MobEffectInstance(
			MobEffects.GLOWING,
			ticks,
			0
		));

		super.doPostAttack(attacker, target, level);
	}
}
