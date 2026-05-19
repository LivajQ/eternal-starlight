package cn.leolezury.eternalstarlight.common.entity.projectile;

import cn.leolezury.eternalstarlight.common.registry.ESEntities;
import cn.leolezury.eternalstarlight.common.registry.ESItems;
import cn.leolezury.eternalstarlight.common.registry.ESParticles;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class AirSacArrow extends AbstractArrow {

	public AirSacArrow(EntityType<? extends AirSacArrow> type, Level level) {
		super(type, level);
	}

	public AirSacArrow(Level level, LivingEntity shooter) {
		super(ESEntities.AIR_SAC_ARROW.get(), shooter, level);
	}

	public AirSacArrow(Level level, double x, double y, double z) {
		super(ESEntities.AIR_SAC_ARROW.get(), x, y, z, level);
	}

	@Override
	protected float getWaterInertia() {
		return 0.99f;
	}

	@Override
	public void tick() {
		super.tick();

		if (!this.inGround && this.isInWater()) {
			Vec3 motion = this.getDeltaMovement();
			this.setDeltaMovement(motion.x, motion.y - 0.025D, motion.z);
		}

		if (this.level().isClientSide && !this.inGround && isInWater()) {

			Vec3 center = getBoundingBox().getCenter();
			Vec3 pos = new Vec3(center.x, getBoundingBox().minY, center.z)
				.add(level().getRandom().nextFloat() * getBbWidth() - getBbWidth() * 0.5,
					0,
					level().getRandom().nextFloat() * getBbWidth() - getBbWidth() * 0.5);

			Vec3 speed = getDeltaMovement().normalize()
				.add((level().getRandom().nextFloat() - 0.5) * 0.3,
					(level().getRandom().nextFloat() - 0.5) * 0.3,
					(level().getRandom().nextFloat() - 0.5) * 0.3)
				.scale(-0.2);

			level().addParticle(ESParticles.COLORED_INK.get(), pos.x, pos.y, pos.z, speed.x, speed.y, speed.z);
		}
	}

	@Override
	protected ItemStack getPickupItem() {
		return ESItems.AIR_SAC_ARROW.get().getDefaultInstance();
	}
}
