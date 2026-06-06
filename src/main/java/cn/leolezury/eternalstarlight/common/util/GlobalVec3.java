package cn.leolezury.eternalstarlight.common.util;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public record GlobalVec3(ResourceKey<Level> dimension, Vec3 pos) {
	public static final MapCodec<GlobalVec3> MAP_CODEC = RecordCodecBuilder.mapCodec(
		instance -> instance.group(
				Level.RESOURCE_KEY_CODEC.fieldOf("dimension").forGetter(GlobalVec3::dimension), Vec3.CODEC.fieldOf("pos").forGetter(GlobalVec3::pos)
			)
			.apply(instance, GlobalVec3::of)
	);

	public static GlobalVec3 of(ResourceKey<Level> dimension, Vec3 pos) {
		return new GlobalVec3(dimension, pos);
	}

	@Override
	public String toString() {
		return this.dimension + " " + this.pos;
	}
}
