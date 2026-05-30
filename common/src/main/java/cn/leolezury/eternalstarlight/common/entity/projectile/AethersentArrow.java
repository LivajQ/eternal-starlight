package cn.leolezury.eternalstarlight.common.entity.projectile;

import cn.leolezury.eternalstarlight.common.network.ParticlePacket;
import cn.leolezury.eternalstarlight.common.particle.ExplosionShockParticleOptions;
import cn.leolezury.eternalstarlight.common.platform.ESPlatform;
import cn.leolezury.eternalstarlight.common.registry.ESEntities;
import cn.leolezury.eternalstarlight.common.registry.ESItems;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class AethersentArrow extends AbstractArrow {
	public AethersentArrow(EntityType<? extends AethersentArrow> entityType, Level level) {
		super(entityType, level);
	}

	public AethersentArrow(Level level, LivingEntity shooter) {
		super(ESEntities.AETHERSENT_ARROW.get(), shooter, level);
		setPierceLevel((byte) (getPierceLevel() + 3));
	}

	public AethersentArrow(Level level, double x, double y, double z) {
		super(ESEntities.AETHERSENT_ARROW.get(), x, y, z, level);
		setPierceLevel((byte) (getPierceLevel() + 3));
	}

	@Override
	protected void doPostHurtEffects(LivingEntity livingEntity) {
		super.doPostHurtEffects(livingEntity);
		if (level() instanceof ServerLevel serverLevel) {
			for (int i = 0; i < 5; i++) {
				Vec3 speed = new Vec3((this.random.nextFloat() - this.random.nextFloat()) * 0.1F, this.random.nextFloat() * 0.05F, (this.random.nextFloat() - this.random.nextFloat()) * 0.1F).normalize();
				ESPlatform.INSTANCE.sendToAllClients(serverLevel, new ParticlePacket(ExplosionShockParticleOptions.AETHERSENT, position().x + speed.x * 0.25, position().y + speed.y * 0.25, position().z + speed.z * 0.25, speed.x, speed.y, speed.z));
			}
		}
	}

	@Override
	public ItemStack getPickupItem() {
		return ESItems.AETHERSENT_ARROW.get().getDefaultInstance();
	}

}
