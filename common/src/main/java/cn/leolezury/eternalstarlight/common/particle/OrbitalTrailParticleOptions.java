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

public record OrbitalTrailParticleOptions(Vector3f fromColor, Vector3f toColor, float radius, float rotSpeed, float alpha, int lifetime, int owner) implements ParticleOptions {

	public static OrbitalTrailParticleOptions fromIntColor(Vector3f fromColor, Vector3f toColor, float radius, float rotSpeed, float alpha, int lifetime, int owner) {
		return new OrbitalTrailParticleOptions(
			new Vector3f(fromColor).div(255f),
			new Vector3f(toColor).div(255f),
			radius,
			rotSpeed,
			alpha,
			lifetime,
			owner
		);
	}

	public static final MapCodec<OrbitalTrailParticleOptions> CODEC =
		RecordCodecBuilder.mapCodec(instance -> instance.group(
			ExtraCodecs.VECTOR3F.fieldOf("from_color").forGetter(OrbitalTrailParticleOptions::fromColor),
			ExtraCodecs.VECTOR3F.fieldOf("to_color").forGetter(OrbitalTrailParticleOptions::toColor),
			Codec.FLOAT.fieldOf("radius").forGetter(OrbitalTrailParticleOptions::radius),
			Codec.FLOAT.fieldOf("rot_speed").forGetter(OrbitalTrailParticleOptions::rotSpeed),
			Codec.FLOAT.fieldOf("alpha").forGetter(OrbitalTrailParticleOptions::alpha),
			Codec.INT.fieldOf("lifetime").forGetter(OrbitalTrailParticleOptions::lifetime),
			Codec.INT.fieldOf("owner").forGetter(OrbitalTrailParticleOptions::owner)
		).apply(instance, OrbitalTrailParticleOptions::new));

	public static final Deserializer<OrbitalTrailParticleOptions> DESERIALIZER =
		new Deserializer<>() {

			@Override
			public OrbitalTrailParticleOptions fromCommand(ParticleType<OrbitalTrailParticleOptions> type, StringReader reader) {
				return new OrbitalTrailParticleOptions(
					new Vector3f(1, 1, 1),
					new Vector3f(1, 1, 1),
					1F,
					1F,
					1F,
					20,
					0
				);
			}

			@Override
			public OrbitalTrailParticleOptions fromNetwork(ParticleType<OrbitalTrailParticleOptions> type, FriendlyByteBuf buf) {
				return new OrbitalTrailParticleOptions(
					buf.readVector3f(),
					buf.readVector3f(),
					buf.readFloat(),
					buf.readFloat(),
					buf.readFloat(),
					buf.readInt(),
					buf.readInt()
				);
			}
		};

	@Override
	public void writeToNetwork(FriendlyByteBuf buf) {
		buf.writeVector3f(fromColor);
		buf.writeVector3f(toColor);
		buf.writeFloat(radius);
		buf.writeFloat(rotSpeed);
		buf.writeFloat(alpha);
		buf.writeInt(lifetime);
		buf.writeInt(owner);
	}

	@Override
	public String writeToString() {
		return BuiltInRegistries.PARTICLE_TYPE.getKey(getType()).toString();
	}

	@Override
	public ParticleType<OrbitalTrailParticleOptions> getType() {
		return ESParticles.ORBITAL_TRAIL.get();
	}
}
