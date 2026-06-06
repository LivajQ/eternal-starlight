package cn.leolezury.eternalstarlight.common.item.combat;

import net.minecraft.world.item.BowItem;

public class GlisteringBowItem extends BowItem {
	public GlisteringBowItem(Properties properties) {
		super(properties);
	}

	/*
	@Override
	protected Projectile createProjectile(Level level, LivingEntity shooter, ItemStack weapon, ItemStack ammo, boolean isCrit) {
		Projectile projectile = super.createProjectile(level, shooter, weapon, ammo, isCrit);
		if (projectile instanceof AbstractArrow arrow) {
			arrow.setBaseDamage(arrow.getBaseDamage() + 0.3);
		}
		return projectile;
	}
	 */

}
