package cn.leolezury.eternalstarlight.common.entity.misc;

import cn.leolezury.eternalstarlight.common.registry.ESEntities;
import cn.leolezury.eternalstarlight.common.registry.ESMobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class TearBomb extends PrimedTnt {

	public TearBomb(EntityType<? extends TearBomb> type, Level level) {
		super(type, level);
	}

	public TearBomb(Level level, double x, double y, double z, @Nullable LivingEntity igniter) {
		this(ESEntities.TEAR_BOMB.get(), level);
		this.setPos(x, y, z);

		double d0 = level.getRandom().nextDouble() * (Math.PI * 2F);
		this.setDeltaMovement(-Math.sin(d0) * 0.02D, 0.2F, -Math.cos(d0) * 0.02D);

		this.setFuse(60);
		this.owner = igniter;

		this.xo = x;
		this.yo = y;
		this.zo = z;
	}

	@Override
	public void explode() {
		this.level().explode(this, this.getX(), this.getY(0.0625F), this.getZ(), 3.0F, Level.ExplosionInteraction.TNT);

		AreaEffectCloud cloud = new AreaEffectCloud(this.level(), this.getX(), this.getY(), this.getZ());
		cloud.setRadius(5F);
		cloud.setRadiusOnUse(-0.5F);
		cloud.setWaitTime(10);
		cloud.setRadiusPerTick(-cloud.getRadius() / (float) cloud.getDuration());
		cloud.addEffect(new MobEffectInstance(MobEffects.POISON, 120));
		cloud.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 120));
		cloud.addEffect(new MobEffectInstance(ESMobEffects.TEARY.get(), 120));
		this.level().addFreshEntity(cloud);
	}

	@Override
	public float getEyeHeight(Pose pose, EntityDimensions size) {
		return 0.15F;
	}
}
