package cn.leolezury.eternalstarlight.common.enchantment;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.phys.Vec3;

public class FearlessEnchantment extends Enchantment {

	public FearlessEnchantment() {
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
		return 2;
	}

	@Override
	public void doPostAttack(LivingEntity attacker, Entity target, int level) {
		if (!(target instanceof LivingEntity victim)) return;

		float knockback = 0.5F * level;

		victim.knockback(knockback,
			attacker.getX() - victim.getX(),
			attacker.getZ() - victim.getZ());

		float minSpeed = 0.1F;
		float maxSpeed = 0.1F + 0.2F * (level - 1);

		float speed = Mth.randomBetween(attacker.getRandom(), minSpeed, maxSpeed);

		Vec3 velocity = victim.position()
			.subtract(attacker.position())
			.normalize()
			.scale(speed);

		attacker.hurtMarked = true;
		attacker.addDeltaMovement(velocity);

		super.doPostAttack(attacker, target, level);
	}
}
