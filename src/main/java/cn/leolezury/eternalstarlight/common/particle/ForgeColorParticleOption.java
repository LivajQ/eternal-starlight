package cn.leolezury.eternalstarlight.common.particle;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.FastColor;

public class ForgeColorParticleOption implements ParticleOptions {

	public static MapCodec<ForgeColorParticleOption> codec(ParticleType<ForgeColorParticleOption> type) {
		return RecordCodecBuilder.mapCodec(instance -> instance.group(
			Codec.FLOAT.fieldOf("r").forGetter(o -> o.r),
			Codec.FLOAT.fieldOf("g").forGetter(o -> o.g),
			Codec.FLOAT.fieldOf("b").forGetter(o -> o.b)
		).apply(instance, (r, g, b) -> new ForgeColorParticleOption(type, r, g, b)));
	}

	public static final Deserializer<ForgeColorParticleOption> DESERIALIZER = new Deserializer<>() {
		@Override
		public ForgeColorParticleOption fromCommand(ParticleType<ForgeColorParticleOption> type, StringReader reader) throws CommandSyntaxException {
			reader.expect(' ');
			float r = (float) reader.readDouble();
			reader.expect(' ');
			float g = (float) reader.readDouble();
			reader.expect(' ');
			float b = (float) reader.readDouble();
			return new ForgeColorParticleOption(type, r, g, b);
		}

		@Override
		public ForgeColorParticleOption fromNetwork(ParticleType<ForgeColorParticleOption> type, FriendlyByteBuf buf) {
			return new ForgeColorParticleOption(type, buf.readFloat(), buf.readFloat(), buf.readFloat());
		}
	};

	private final ParticleType<ForgeColorParticleOption> type;
	private final float r, g, b;

	public ForgeColorParticleOption(ParticleType<ForgeColorParticleOption> type, float r, float g, float b) {
		this.type = type;
		this.r = r;
		this.g = g;
		this.b = b;
	}

	@Override
	public ParticleType<?> getType() { return type; }

	@Override
	public void writeToNetwork(FriendlyByteBuf buf) {
		buf.writeFloat(r);
		buf.writeFloat(g);
		buf.writeFloat(b);
	}

	@Override
	public String writeToString() {
		return String.format("%s %.2f %.2f %.2f", BuiltInRegistries.PARTICLE_TYPE.getKey(type), r, g, b);
	}

	public float getR() { return r; }
	public float getG() { return g; }
	public float getB() { return b; }

	public static ForgeColorParticleOption create(ParticleType<ForgeColorParticleOption> type, int argb) {
		return new ForgeColorParticleOption(type,
			FastColor.ARGB32.red(argb) / 255f,
			FastColor.ARGB32.green(argb) / 255f,
			FastColor.ARGB32.blue(argb) / 255f);
	}
}