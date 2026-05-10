package cn.leolezury.eternalstarlight.common.particle;

import cn.leolezury.eternalstarlight.common.registry.ESParticles;
import com.mojang.brigadier.StringReader;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;

public record GatheringTrailParticleOptions(ParticleType<GatheringTrailParticleOptions> type, float trailWidth, float trailLength, float speedScale, float rotSpeedScale) implements ParticleOptions {
	public static final GatheringTrailParticleOptions ENERGY = new GatheringTrailParticleOptions(ESParticles.GATHERING_ENERGY.get(), 0.125f, 5, 1.25f, 0.25f);
	public static final GatheringTrailParticleOptions SOUL = new GatheringTrailParticleOptions(ESParticles.GATHERING_SOUL.get(), 0.125f, 3, 1.25f, 1.25f);
	public static final GatheringTrailParticleOptions SOUL_THIN = new GatheringTrailParticleOptions(ESParticles.GATHERING_SOUL.get(), 0.06f, 2, 1.5f, 0.35f);
	public static final GatheringTrailParticleOptions FLARE = new GatheringTrailParticleOptions(ESParticles.GATHERING_FLARE.get(), 0.125f, 2, 1, 1);

	public static MapCodec<GatheringTrailParticleOptions> codec(ParticleType<GatheringTrailParticleOptions> type) {
		return RecordCodecBuilder.mapCodec(instance -> instance.group(
			Codec.FLOAT.fieldOf("trail_width").forGetter(GatheringTrailParticleOptions::trailWidth),
			Codec.FLOAT.fieldOf("trail_length").forGetter(GatheringTrailParticleOptions::trailLength),
			Codec.FLOAT.fieldOf("speed_scale").forGetter(GatheringTrailParticleOptions::speedScale),
			Codec.FLOAT.fieldOf("rot_speed_scale").forGetter(GatheringTrailParticleOptions::rotSpeedScale)
		).apply(instance, (trailWidth, trailLength, speedScale, rotSpeedScale) ->
			new GatheringTrailParticleOptions(type, trailWidth, trailLength, speedScale, rotSpeedScale)));
	}

	public static final Deserializer<GatheringTrailParticleOptions> DESERIALIZER =
		new Deserializer<>() {

			@Override
			public GatheringTrailParticleOptions fromCommand(ParticleType<GatheringTrailParticleOptions> type, StringReader reader) {
				return new GatheringTrailParticleOptions(
					type,
					1F,
					1F,
					1F,
					1F
				);
			}

			@Override
			public GatheringTrailParticleOptions fromNetwork(ParticleType<GatheringTrailParticleOptions> type, FriendlyByteBuf buf) {
				return new GatheringTrailParticleOptions(
					type,
					buf.readFloat(),
					buf.readFloat(),
					buf.readFloat(),
					buf.readFloat()
				);
			}
		};


	@Override
	public void writeToNetwork(FriendlyByteBuf buf) {
		buf.writeFloat(trailWidth);
		buf.writeFloat(trailLength);
		buf.writeFloat(speedScale);
		buf.writeFloat(rotSpeedScale);
	}

	@Override
	public String writeToString() {
		return BuiltInRegistries.PARTICLE_TYPE.getKey(getType()).toString();
	}

	@Override
	public ParticleType<GatheringTrailParticleOptions> getType() {
		return type;
	}
}
