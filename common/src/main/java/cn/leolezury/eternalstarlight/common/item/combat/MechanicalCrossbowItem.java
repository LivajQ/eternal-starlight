package cn.leolezury.eternalstarlight.common.item.combat;

import cn.leolezury.eternalstarlight.common.util.ESConventionalTags;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;

public class MechanicalCrossbowItem extends CrossbowItem {
	public MechanicalCrossbowItem(Properties properties) {
		super(properties);
	}

	/*
	@Override
	protected Projectile createProjectile(Level level, LivingEntity shooter, ItemStack weapon, ItemStack ammo, boolean isCrit) {
		Projectile projectile = super.createProjectile(level, shooter, weapon, ammo, isCrit);
		ESDataAttachments.ARROW_TYPE.setData(projectile, ESCommonHandler.MECHANICAL_ARROW);
		if (projectile instanceof AbstractArrow arrow) {
			arrow.setPierceLevel((byte) ((int) arrow.getPierceLevel() + 1));
		}
		return projectile;
	}

	@Override
	protected void shootProjectile(LivingEntity shooter, Projectile projectile, int index, float velocity, float inaccuracy, float angle, @Nullable LivingEntity target) {
		super.shootProjectile(shooter, projectile, index, velocity, inaccuracy, angle, target);
		projectile.setDeltaMovement(projectile.getDeltaMovement().scale(1.75));
	}
	 */

	@Override
	public boolean isValidRepairItem(ItemStack stack, ItemStack repairCandidate) {
		return repairCandidate.is(ESConventionalTags.Items.INGOTS_GOLEM_STEEL) || super.isValidRepairItem(stack, repairCandidate);
	}
}
