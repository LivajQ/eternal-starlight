package cn.leolezury.eternalstarlight.common.entity.projectile;

import cn.leolezury.eternalstarlight.common.registry.ESEntities;
import cn.leolezury.eternalstarlight.common.registry.ESItems;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class GlaciteArrow extends AbstractArrow {

	private static final String TAG_DURATION = "duration";
	private int duration = 200;

	public GlaciteArrow(EntityType<? extends GlaciteArrow> type, Level level) {
		super(type, level);
	}

	public GlaciteArrow(Level level, LivingEntity shooter) {
		super(ESEntities.GLACITE_ARROW.get(), shooter, level);
	}

	public GlaciteArrow(Level level, double x, double y, double z) {
		super(ESEntities.GLACITE_ARROW.get(), x, y, z, level);
	}

	@Override
	public void tick() {
		super.tick();
		if (this.level().isClientSide && !this.inGround) {
			this.level().addParticle(
				ParticleTypes.SNOWFLAKE,
				this.getX(), this.getY(), this.getZ(),
				0.0, 0.0, 0.0
			);
		}
	}

	@Override
	protected void doPostHurtEffects(LivingEntity target) {
		super.doPostHurtEffects(target);
		if (target.canFreeze()) {
			target.setTicksFrozen(
				Math.min(target.getTicksFrozen() + duration, 300)
			);
		}
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		if (tag.contains(TAG_DURATION)) {
			this.duration = tag.getInt(TAG_DURATION);
		}
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.putInt(TAG_DURATION, this.duration);
	}

	@Override
	protected ItemStack getPickupItem() {
		return ESItems.GLACITE_ARROW.get().getDefaultInstance();
	}
}
