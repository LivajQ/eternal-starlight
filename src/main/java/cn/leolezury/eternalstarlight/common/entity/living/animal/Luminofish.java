package cn.leolezury.eternalstarlight.common.entity.living.animal;

import cn.leolezury.eternalstarlight.common.config.ESConfig;
import cn.leolezury.eternalstarlight.common.registry.ESItems;
import cn.leolezury.eternalstarlight.common.registry.ESSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.AbstractSchoolingFish;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.Nullable;

public class Luminofish extends AbstractSchoolingFish {
	public Luminofish(EntityType<? extends Luminofish> entityType, Level level) {
		super(entityType, level);
	}

	protected static final EntityDataAccessor<Integer> SWELL_TICKS = SynchedEntityData.defineId(Luminofish.class, EntityDataSerializers.INT);

	public int getSwellTicks() {
		return this.getEntityData().get(SWELL_TICKS);
	}

	public void setSwellTicks(int swellTicks) {
		this.getEntityData().set(SWELL_TICKS, swellTicks);
	}

	public AnimationState swimAnimationState = new AnimationState();
	public AnimationState swellAnimationState = new AnimationState();

	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes()
			.add(Attributes.MAX_HEALTH, 1.0)
			.add(Attributes.ARMOR, 0.0)
			.add(Attributes.ATTACK_DAMAGE, 1.0)
			.add(Attributes.FOLLOW_RANGE, 16.0);
	}

	@Nullable
	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData, @Nullable CompoundTag dataTag) {
		SpawnGroupData data = super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData, dataTag);

		this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(ESConfig.luminofish.maxHealth.get());
		this.getAttribute(Attributes.ARMOR).setBaseValue(ESConfig.luminofish.armor.get());
		this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(ESConfig.luminofish.attackDamage.get());
		this.getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(ESConfig.luminofish.followRange.get());

		this.setHealth(this.getMaxHealth());

		return data;
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(SWELL_TICKS, 0);
	}

	@Override
	public void aiStep() {
		super.aiStep();
		if (!level().isClientSide) {
			int swellTicks = getSwellTicks();
			if (swellTicks > 0) {
				setSwellTicks(swellTicks - 1);
				LivingEntity target = getTarget();
				if (swellTicks == 10 && target != null) {
					if (target.position().distanceTo(position()) < 3) {
						doHurtTarget(target);
						target.addEffect(new MobEffectInstance(MobEffects.POISON, 60));
					}
				}
			}
		} else {
			swimAnimationState.startIfStopped(tickCount);
		}
	}

	@Override
	public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
		super.onSyncedDataUpdated(accessor);
		if (accessor.equals(SWELL_TICKS) && getSwellTicks() == 20) {
			swellAnimationState.start(tickCount);
		}
	}

	@Override
	public boolean isInvulnerableTo(DamageSource damageSource) {
		return super.isInvulnerableTo(damageSource) || damageSource.is(DamageTypes.HOT_FLOOR);
	}

	@Override
	public boolean hurt(DamageSource damageSource, float amount) {
		boolean flag = super.hurt(damageSource, amount);
		Entity entity = damageSource.getDirectEntity();
		if (flag && entity instanceof LivingEntity living && getSwellTicks() <= 0 && !level().isClientSide) {
			setSwellTicks(20);
			this.setTarget(living);
		}
		return flag;
	}

	@Nullable
	@Override
	protected SoundEvent getHurtSound(DamageSource damageSource) {
		return ESSoundEvents.LUMINOFISH_HURT.get();
	}

	@Nullable
	@Override
	protected SoundEvent getDeathSound() {
		return ESSoundEvents.LUMINOFISH_DEATH.get();
	}

	@Override
	protected SoundEvent getFlopSound() {
		return ESSoundEvents.LUMINOFISH_FLOP.get();
	}

	@Override
	public ItemStack getBucketItemStack() {
		return ESItems.LUMINOFISH_BUCKET.get().getDefaultInstance();
	}

	public static boolean checkLuminoFishSpawnRules(EntityType<? extends Luminofish> entityType, LevelAccessor levelAccessor, MobSpawnType mobSpawnType, BlockPos blockPos, RandomSource randomSource) {
		int seaLevel = levelAccessor.getSeaLevel();
		return blockPos.getY() <= seaLevel - 40 && levelAccessor.getFluidState(blockPos.below()).is(FluidTags.WATER) && levelAccessor.getBlockState(blockPos.above()).is(Blocks.WATER) && ESConfig.luminofish.canSpawn.get();
	}

	@Override
	public void saveToBucketTag(ItemStack stack) {
		super.saveToBucketTag(stack);
		CompoundTag tag = stack.getOrCreateTag();
		tag.putInt("SwellTicks", this.getSwellTicks());
	}

	@Override
	public void loadFromBucketTag(CompoundTag tag) {
		super.loadFromBucketTag(tag);
		if (tag.contains("SwellTicks")) {
			this.setSwellTicks(tag.getInt("SwellTicks"));
		}
	}

}
