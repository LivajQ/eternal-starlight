package cn.leolezury.eternalstarlight.common.enchantment;

import cn.leolezury.eternalstarlight.common.registry.ESDataAttachments;
import cn.leolezury.eternalstarlight.common.util.ESTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class AbyssalTouchEnchantment extends Enchantment {

	public AbyssalTouchEnchantment() {
		super(Rarity.UNCOMMON, EnchantmentCategory.WEAPON, new EquipmentSlot[]{ EquipmentSlot.MAINHAND });
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
		return 2;
	}

	@Override
	public boolean canEnchant(ItemStack stack) {
		return stack.is(ESTags.Items.WHIP_ENCHANTABLE) || super.canEnchant(stack);
	}

	@Override
	public void doPostAttack(LivingEntity attacker, Entity target, int level) {
		if (!(target instanceof LivingEntity living)) return;

		int ticks = (int)(5.0F * level * 20);

		int current = ESDataAttachments.ABYSSAL_FIRE_TICKS.getData(living);
		ESDataAttachments.ABYSSAL_FIRE_TICKS.setData(living, Math.max(current, ticks));

		super.doPostAttack(attacker, target, level);
	}
}
