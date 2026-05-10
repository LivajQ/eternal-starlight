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
import net.minecraft.util.ExtraCodecs;
import org.joml.Vector3f;

public record ESExplosionParticleOptions(ParticleType<ESExplosionParticleOptions> type, Vector3f fromColor, Vector3f toColor, float lifeScale) implements ParticleOptions {
	public static final ESExplosionParticleOptions BLAST = fromIntColor(ESParticles.BLAST.get(), new Vector3f(255, 255, 255), new Vector3f(255, 255, 255), 0.5f);
	public static final ESExplosionParticleOptions ENERGY = fromIntColor(ESParticles.EXPLOSION.get(), new Vector3f(255, 255, 255), new Vector3f(129, 212, 250), 1);
	public static final ESExplosionParticleOptions ENERGY_BLAST = fromIntColor(ESParticles.BLAST.get(), new Vector3f(255, 255, 255), new Vector3f(129, 212, 250), 0.5f);
	public static final ESExplosionParticleOptions LAVA = fromIntColor(ESParticles.EXPLOSION.get(), new Vector3f(217, 168, 74), new Vector3f(174, 76, 18), 1);
	public static final ESExplosionParticleOptions AETHERSENT = fromIntColor(ESParticles.EXPLOSION.get(), new Vector3f(255, 255, 255), new Vector3f(233, 173, 237), 1);
	public static final ESExplosionParticleOptions LUNAR = fromIntColor(ESParticles.EXPLOSION.get(), new Vector3f(66, 66, 115), new Vector3f(32, 32, 64), 1);
	public static final ESExplosionParticleOptions FROZEN = fromIntColor(ESParticles.EXPLOSION.get(), new Vector3f(121, 178, 209), new Vector3f(192, 251, 255), 1);
	public static final ESExplosionParticleOptions FROZEN_BLAST = fromIntColor(ESParticles.BLAST.get(), new Vector3f(121, 178, 209), new Vector3f(192, 251, 255), 0.5f);

	public static final Deserializer<ESExplosionParticleOptions> DESERIALIZER =
		new Deserializer<>() {

			@Override
			public ESExplosionParticleOptions fromCommand(ParticleType<ESExplosionParticleOptions> type, StringReader reader) {
				return new ESExplosionParticleOptions(
					type,
					new Vector3f(1,1,1),
					new Vector3f(1,1,1),
					1F
				);
			}

			@Override
			public ESExplosionParticleOptions fromNetwork(ParticleType<ESExplosionParticleOptions> type, FriendlyByteBuf buf) {
				return new ESExplosionParticleOptions(
					type,
					buf.readVector3f(),
					buf.readVector3f(),
					buf.readFloat()
				);
			}
		};

	public static ESExplosionParticleOptions fromIntColor(ParticleType<ESExplosionParticleOptions> type, Vector3f fromColor, Vector3f toColor, float lifeScale) {
		return new ESExplosionParticleOptions(type, new Vector3f(fromColor).div(255f), new Vector3f(toColor).div(255f), lifeScale);
	}

	@Override
	public void writeToNetwork(FriendlyByteBuf buf) {
		buf.writeVector3f(fromColor);
		buf.writeVector3f(toColor);
		buf.writeFloat(lifeScale);
	}

	@Override
	public String writeToString() {
		return BuiltInRegistries.PARTICLE_TYPE.getKey(this.type()).toString();
	}

	public static MapCodec<ESExplosionParticleOptions> codec(ParticleType<ESExplosionParticleOptions> type) {
		return RecordCodecBuilder.mapCodec((instance) -> instance.group(
			ExtraCodecs.VECTOR3F.fieldOf("from_color").forGetter(ESExplosionParticleOptions::fromColor),
			ExtraCodecs.VECTOR3F.fieldOf("to_color").forGetter(ESExplosionParticleOptions::toColor),
			Codec.FLOAT.fieldOf("life_scale").forGetter(ESExplosionParticleOptions::lifeScale)
		).apply(instance, (from, to, lifeScale) -> new ESExplosionParticleOptions(type, from, to, lifeScale)));
	}

	@Override
	public ParticleType<ESExplosionParticleOptions> getType() {
		return type();
	}
}
