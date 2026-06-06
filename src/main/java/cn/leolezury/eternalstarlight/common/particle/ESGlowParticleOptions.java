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
import net.minecraft.util.RandomSource;
import org.joml.Vector3f;

import java.util.List;

public record ESGlowParticleOptions(Vector3f fromColor, Vector3f toColor, float alpha, float lifeScale) implements ParticleOptions {
	private static final List<Vector3f> SEEK_COLORS = List.of(
		new Vector3f(49, 177, 204),
		new Vector3f(89, 47, 108),
		new Vector3f(22, 7, 78),
		new Vector3f(209, 107, 187),
		new Vector3f(107, 194, 209),
		new Vector3f(67, 113, 145),
		new Vector3f(107, 101, 155)
	);

	public static ESGlowParticleOptions fromIntColor(Vector3f fromColor, Vector3f toColor, float alpha, float lifeScale) {
		return new ESGlowParticleOptions(
			new Vector3f(fromColor).div(255f),
			new Vector3f(toColor).div(255f),
			alpha,
			lifeScale
		);
	}

	public static final MapCodec<ESGlowParticleOptions> CODEC =
		RecordCodecBuilder.mapCodec(instance -> instance.group(
			ExtraCodecs.VECTOR3F.fieldOf("from_color").forGetter(ESGlowParticleOptions::fromColor),
			ExtraCodecs.VECTOR3F.fieldOf("to_color").forGetter(ESGlowParticleOptions::toColor),
			Codec.FLOAT.fieldOf("alpha").forGetter(ESGlowParticleOptions::alpha),
			Codec.FLOAT.fieldOf("life_scale").forGetter(ESGlowParticleOptions::lifeScale)
		).apply(instance, ESGlowParticleOptions::new));

	public static final Deserializer<ESGlowParticleOptions> DESERIALIZER =
		new Deserializer<>() {

			@Override
			public ESGlowParticleOptions fromCommand(ParticleType<ESGlowParticleOptions> type, StringReader reader) {
				return new ESGlowParticleOptions(
					new Vector3f(1, 1, 1),
					new Vector3f(1, 1, 1),
					1F,
					1F
				);
			}

			@Override
			public ESGlowParticleOptions fromNetwork(ParticleType<ESGlowParticleOptions> type, FriendlyByteBuf buf) {
				return new ESGlowParticleOptions(
					buf.readVector3f(),
					buf.readVector3f(),
					buf.readFloat(),
					buf.readFloat()
				);
			}
		};

	public static ESGlowParticleOptions getSeek(RandomSource random, boolean lowTransparency, boolean extendedLife) {
		return fromIntColor(
			SEEK_COLORS.get(random.nextInt(SEEK_COLORS.size())),
			SEEK_COLORS.get(random.nextInt(SEEK_COLORS.size())),
			lowTransparency ? 1f : 0.5f + (random.nextFloat() - 0.5f) * 0.2f,
			extendedLife ? 1.5f : 0.4f
		);
	}

	@Override
	public void writeToNetwork(FriendlyByteBuf buf) {
		buf.writeVector3f(fromColor);
		buf.writeVector3f(toColor);
		buf.writeFloat(alpha);
		buf.writeFloat(lifeScale);
	}

	@Override
	public String writeToString() {
		return BuiltInRegistries.PARTICLE_TYPE.getKey(getType()).toString();
	}

	@Override
	public ParticleType<ESGlowParticleOptions> getType() {
		return ESParticles.GLOW.get();
	}
}
