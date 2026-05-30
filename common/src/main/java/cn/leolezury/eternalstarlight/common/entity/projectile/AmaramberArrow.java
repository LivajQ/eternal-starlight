package cn.leolezury.eternalstarlight.common.entity.projectile;

import cn.leolezury.eternalstarlight.common.registry.ESEntities;
import cn.leolezury.eternalstarlight.common.registry.ESItems;
import cn.leolezury.eternalstarlight.common.registry.ESMobEffects;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class AmaramberArrow extends AbstractArrow {

	private static final String TAG_DURATION = "duration";
	private int duration = 400;

	public AmaramberArrow(EntityType<? extends AmaramberArrow> type, Level level) {
		super(type, level);
	}

	public AmaramberArrow(Level level, LivingEntity shooter) {
		super(ESEntities.AMARAMBER_ARROW.get(), shooter, level);
	}

	public AmaramberArrow(Level level, double x, double y, double z) {
		super(ESEntities.AMARAMBER_ARROW.get(), x, y, z, level);
	}

	@Override
	public void tick() {
		super.tick();
		if (this.level().isClientSide && !this.inGround) {
			this.level().addParticle(
				ParticleTypes.CRIMSON_SPORE,
				this.getX(), this.getY(), this.getZ(),
				0.0, 0.0, 0.0
			);
		}
	}

	@Override
	protected void doPostHurtEffects(LivingEntity target) {
		super.doPostHurtEffects(target);
		MobEffectInstance effect = new MobEffectInstance(
			ESMobEffects.FLAMMABLE.get(),
			this.duration,
			0
		);
		target.addEffect(effect, this.getEffectSource());
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
	public ItemStack getPickupItem() {
		return ESItems.AMARAMBER_ARROW.get().getDefaultInstance();
	}
}
