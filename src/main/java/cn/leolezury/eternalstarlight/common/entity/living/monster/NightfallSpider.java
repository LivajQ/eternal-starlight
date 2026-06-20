package cn.leolezury.eternalstarlight.common.entity.living.monster;

import cn.leolezury.eternalstarlight.common.config.ESConfig;
import cn.leolezury.eternalstarlight.common.registry.ESEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;

public class NightfallSpider extends Spider {
	public NightfallSpider(EntityType<? extends NightfallSpider> type, Level level) {
		super(type, level);
	}

	public static AttributeSupplier.Builder createNightfallSpider() {
		return Spider.createAttributes()
			.add(Attributes.MAX_HEALTH, 1.0)
			.add(Attributes.ARMOR, 0.0)
			.add(Attributes.ATTACK_DAMAGE, 1.0)
			.add(Attributes.FOLLOW_RANGE, 16.0);
	}

	@Nullable
	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag dataTag) {
		SpawnGroupData data = super.finalizeSpawn(level, difficulty, spawnType, spawnData, dataTag);

		getPassengers().forEach(entity -> {
			if (entity instanceof Skeleton s) {
				s.discard();

				LonestarSkeleton skeleton = ESEntities.LONESTAR_SKELETON.get().create(this.level());
				if (skeleton != null) {
					skeleton.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
					skeleton.finalizeSpawn(level, difficulty, spawnType, null, null);
					skeleton.startRiding(this);
				}
			}
		});

		this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(ESConfig.nightfallSpider.maxHealth.get());
		this.getAttribute(Attributes.ARMOR).setBaseValue(ESConfig.nightfallSpider.armor.get());
		this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(ESConfig.nightfallSpider.attackDamage.get());
		this.getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(ESConfig.nightfallSpider.followRange.get());

		this.setHealth(this.getMaxHealth());

		return data;
	}

	@Override
	public boolean doHurtTarget(Entity entity) {
		if (super.doHurtTarget(entity)) {
			if (entity instanceof LivingEntity) {
				int effectTime = 0;
				if (this.level().getDifficulty() == Difficulty.NORMAL) {
					effectTime = 7;
				} else if (this.level().getDifficulty() == Difficulty.HARD) {
					effectTime = 15;
				}
				if (effectTime > 0) {
					((LivingEntity) entity).addEffect(new MobEffectInstance(MobEffects.GLOWING, effectTime * 20, 0), this);
				}
			}
			return true;
		} else {
			return false;
		}
	}

	/*
	@Override
	public Vec3 getVehicleAttachmentPoint(Entity entity) {
		return entity.getBbWidth() <= this.getBbWidth() ? new Vec3(0.0, 0.21875 * (double) this.getScale(), 0.0) : super.getVehicleAttachmentPoint(entity);
	}
	 */

	public static boolean checkNightfallSpiderSpawnRules(EntityType<? extends NightfallSpider> type, LevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
		return checkAnyLightMonsterSpawnRules(type, level, spawnType, pos, random) && ESConfig.nightfallSpider.canSpawn.get();
	}
}
