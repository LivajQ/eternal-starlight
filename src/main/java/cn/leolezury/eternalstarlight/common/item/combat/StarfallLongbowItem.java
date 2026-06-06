package cn.leolezury.eternalstarlight.common.item.combat;

import cn.leolezury.eternalstarlight.common.util.ESConventionalTags;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;

public class StarfallLongbowItem extends BowItem {
	public StarfallLongbowItem(Properties properties) {
		super(properties);
	}

	/*
	@Override
	protected Projectile createProjectile(Level level, LivingEntity shooter, ItemStack weapon, ItemStack ammo, boolean isCrit) {
		Projectile projectile = super.createProjectile(level, shooter, weapon, ammo, isCrit);
		ESDataAttachments.ARROW_TYPE.setData(projectile, ESCommonHandler.STARFALL_ARROW);
		return projectile;
	}
	 */

	@Override
	public boolean isValidRepairItem(ItemStack stack, ItemStack repairCandidate) {
		return repairCandidate.is(ESConventionalTags.Items.INGOTS_AETHERSENT) || super.isValidRepairItem(stack, repairCandidate);
	}
}
