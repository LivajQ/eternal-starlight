package cn.leolezury.eternalstarlight.common.item.combat;

import cn.leolezury.eternalstarlight.common.entity.projectile.ThrownBoomerang;
import cn.leolezury.eternalstarlight.common.entity.projectile.ThrownEnergyBoomerang;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class EnergyBoomerangItem extends BoomerangItem {

	public EnergyBoomerangItem(Tier tier, int damage, float speed, Properties props) {
		super(tier, damage, speed, props);
	}

	@Override
	public ThrownBoomerang createBoomerang(Level level, @Nullable LivingEntity owner, double x, double y, double z, ItemStack pickupItemStack) {
		return new ThrownEnergyBoomerang(level, owner, x, y, z, pickupItemStack);
	}
}
