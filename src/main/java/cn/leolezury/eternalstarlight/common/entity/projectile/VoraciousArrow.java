package cn.leolezury.eternalstarlight.common.entity.projectile;

import cn.leolezury.eternalstarlight.common.registry.ESEntities;
import cn.leolezury.eternalstarlight.common.registry.ESItems;
import net.minecraft.core.particles.DustColorTransitionOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.joml.Vector3f;

public class VoraciousArrow extends AbstractArrow {

	private static final String TAG_DURATION = "duration";
	private int duration = 200;

	public VoraciousArrow(EntityType<? extends VoraciousArrow> type, Level level) {
		super(type, level);
	}

	public VoraciousArrow(Level level, LivingEntity shooter) {
		super(ESEntities.VORACIOUS_ARROW.get(), shooter, level);
	}

	public VoraciousArrow(Level level, double x, double y, double z) {
		super(ESEntities.VORACIOUS_ARROW.get(), x, y, z, level);
	}

	@Override
	public void tick() {
		super.tick();
		if (this.level().isClientSide && !this.inGround) {
			this.level().addParticle(
				new DustColorTransitionOptions(
					new Vector3f(127 / 255f, 99 / 255f, 129 / 255f),
					new Vector3f(85 / 255f, 71 / 255f, 87 / 255f),
					1f
				),
				this.getX(), this.getY(), this.getZ(),
				0.0, 0.0, 0.0
			);
		}
	}

	@Override
	protected void doPostHurtEffects(LivingEntity target) {
		super.doPostHurtEffects(target);

		target.addEffect(
			new MobEffectInstance(MobEffects.HUNGER, this.duration, 0),
			this.getEffectSource()
		);

		if (getOwner() instanceof Player player) {
			player.getFoodData().eat(3, 0);

			for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
				ItemStack stack = player.getInventory().getItem(i);

				if (stack.is(ESItems.DAGGER_OF_HUNGER.get())) {
					CompoundTag tag = stack.getOrCreateTag();

					float hungerLevel = Mth.clamp(tag.getFloat("HungerLevel"), -1f, 1f);
					float newHungerLevel = Math.min(1f, hungerLevel + 0.05f);

					tag.putFloat("HungerLevel", newHungerLevel);
				}
			}
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
	public ItemStack getPickupItem() {
		return ESItems.VORACIOUS_ARROW.get().getDefaultInstance();
	}
}
