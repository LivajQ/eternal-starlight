package cn.leolezury.eternalstarlight.common.entity.projectile;

import cn.leolezury.eternalstarlight.common.registry.ESEntities;
import cn.leolezury.eternalstarlight.common.registry.ESItems;
import net.minecraft.core.particles.DustColorTransitionOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.joml.Vector3f;

public class MalariteArrow extends AbstractArrow {

	private static final String TAG_DURATION = "duration";
	private int duration = 200;

	public MalariteArrow(EntityType<? extends MalariteArrow> type, Level level) {
		super(type, level);
	}

	public MalariteArrow(Level level, LivingEntity shooter) {
		super(ESEntities.MALARITE_ARROW.get(), shooter, level);
	}

	public MalariteArrow(Level level, double x, double y, double z) {
		super(ESEntities.MALARITE_ARROW.get(), x, y, z, level);
	}

	@Override
	public void tick() {
		super.tick();
		if (this.level().isClientSide && !this.inGround) {
			this.level().addParticle(
				new DustColorTransitionOptions(
					new Vector3f(170 / 255f, 81 / 255f, 158 / 255f),
					new Vector3f(42 / 255f, 61 / 255f, 56 / 255f),
					1f
				),
				this.getX(), this.getY(), this.getZ(),
				0.0, 0.0, 0.0
			);
		}
	}

	@Override
	protected void doPostHurtEffects(LivingEntity target) {
		target.addEffect(
			new MobEffectInstance(MobEffects.POISON, this.duration, 0),
			this.getEffectSource()
		);
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
		return ESItems.MALARITE_ARROW.get().getDefaultInstance();
	}
}
