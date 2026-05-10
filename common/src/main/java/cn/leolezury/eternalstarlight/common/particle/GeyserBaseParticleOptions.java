package cn.leolezury.eternalstarlight.common.particle;

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

public record GeyserBaseParticleOptions(ParticleType<GeyserBaseParticleOptions> type, Vector3f color, int strength, float burstImpulseBase) implements ParticleOptions {

	public static MapCodec<GeyserBaseParticleOptions> codec(final ParticleType<GeyserBaseParticleOptions> type) {
		return RecordCodecBuilder.mapCodec(instance -> instance.group(
			ExtraCodecs.VECTOR3F.fieldOf("color").forGetter(GeyserBaseParticleOptions::color),
			ExtraCodecs.POSITIVE_INT.fieldOf("strength").forGetter(GeyserBaseParticleOptions::strength),
			Codec.FLOAT.fieldOf("burst_impulse_base").forGetter(GeyserBaseParticleOptions::burstImpulseBase)
		).apply(instance, (color, strength, burstImpulseBase) ->
			new GeyserBaseParticleOptions(type, color, strength, burstImpulseBase)));
	}

	public static final Deserializer<GeyserBaseParticleOptions> DESERIALIZER =
		new Deserializer<>() {

			@Override
			public GeyserBaseParticleOptions fromCommand(ParticleType<GeyserBaseParticleOptions> type, StringReader reader) {
				return new GeyserBaseParticleOptions(
					type,
					new Vector3f(1, 1, 1),
					1,
					1F
				);
			}

			@Override
			public GeyserBaseParticleOptions fromNetwork(ParticleType<GeyserBaseParticleOptions> type, FriendlyByteBuf buf) {
				return new GeyserBaseParticleOptions(
					type,
					buf.readVector3f(),
					buf.readInt(),
					buf.readFloat()
				);
			}
		};

	@Override
	public void writeToNetwork(FriendlyByteBuf buf) {
		buf.writeVector3f(color);
		buf.writeInt(strength);
		buf.writeFloat(burstImpulseBase);
	}

	@Override
	public String writeToString() {
		return BuiltInRegistries.PARTICLE_TYPE.getKey(getType()).toString();
	}

	@Override
	public ParticleType<GeyserBaseParticleOptions> getType() {
		return type;
	}
}
