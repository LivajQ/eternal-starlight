package cn.leolezury.eternalstarlight.common.particle;

import cn.leolezury.eternalstarlight.common.registry.ESParticles;
import com.mojang.brigadier.StringReader;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.ExtraCodecs;
import org.joml.Vector3f;

public record GeyserParticleOptions(ParticleType<GeyserParticleOptions> type, Vector3f color, int strength) implements ParticleOptions {

	public static GeyserParticleOptions getAbyssalGeyser(int strength) {
		return fromIntColor(ESParticles.GEYSER.get(), new Vector3f(55, 36, 55), strength);
	}

	public static GeyserParticleOptions fromIntColor(ParticleType<GeyserParticleOptions> type, Vector3f color, int strength) {
		return new GeyserParticleOptions(
			type,
			new Vector3f(color).div(255f),
			strength
		);
	}

	public static MapCodec<GeyserParticleOptions> codec(final ParticleType<GeyserParticleOptions> type) {
		return RecordCodecBuilder.mapCodec(instance -> instance.group(
			ExtraCodecs.VECTOR3F.fieldOf("color").forGetter(GeyserParticleOptions::color),
			ExtraCodecs.POSITIVE_INT.fieldOf("strength").forGetter(GeyserParticleOptions::strength)
		).apply(instance, (color, strength) ->
			new GeyserParticleOptions(type, color, strength)));
	}

	public static final Deserializer<GeyserParticleOptions> DESERIALIZER =
		new Deserializer<>() {

			@Override
			public GeyserParticleOptions fromCommand(ParticleType<GeyserParticleOptions> type, StringReader reader) {
				return new GeyserParticleOptions(
					type,
					new Vector3f(1, 1, 1),
					1
				);
			}

			@Override
			public GeyserParticleOptions fromNetwork(ParticleType<GeyserParticleOptions> type, FriendlyByteBuf buf) {
				return new GeyserParticleOptions(
					type,
					buf.readVector3f(),
					buf.readInt()
				);
			}
		};

	@Override
	public void writeToNetwork(FriendlyByteBuf buf) {
		buf.writeVector3f(color);
		buf.writeInt(strength);
	}

	@Override
	public String writeToString() {
		return BuiltInRegistries.PARTICLE_TYPE.getKey(getType()).toString();
	}

	@Override
	public ParticleType<GeyserParticleOptions> getType() {
		return type;
	}
}
