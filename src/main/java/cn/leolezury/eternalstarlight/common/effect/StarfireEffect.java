package cn.leolezury.eternalstarlight.common.effect;

import cn.leolezury.eternalstarlight.common.registry.ESParticles;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class StarfireEffect extends MobEffect {
	public StarfireEffect(MobEffectCategory category, int color) {
		super(category, color);
	}

	/* no innate support
	@Override
	public ParticleOptions createParticleOptions(MobEffectInstance instance) {
		return ESParticles.STARFIRE.get();
	}
	 */

	//crude but eh
	@Override
	public void applyEffectTick(LivingEntity entity, int amplifier) {
		if (entity.level().isClientSide) {
			entity.level().addParticle(
				ESParticles.STARFIRE.get(),
				entity.getX(),
				entity.getY() + entity.getBbHeight() * 0.5,
				entity.getZ(),
				0, 0, 0
			);
		}
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return duration % 4 == 0;
	}
}