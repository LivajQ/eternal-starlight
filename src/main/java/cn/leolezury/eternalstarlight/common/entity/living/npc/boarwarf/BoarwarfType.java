package cn.leolezury.eternalstarlight.common.entity.living.npc.boarwarf;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;

public record BoarwarfType(Holder<Biome> biome, ResourceLocation texture, ResourceLocation textureFull) {
	public static final Codec<BoarwarfType> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		Biome.CODEC.fieldOf("biome").forGetter(BoarwarfType::biome),
		ResourceLocation.CODEC.fieldOf("texture").forGetter(BoarwarfType::texture)
	).apply(instance, BoarwarfType::new));

	public static final Codec<BoarwarfType> NETWORK_CODEC =
		RecordCodecBuilder.create(instance -> instance.group(
			ResourceLocation.CODEC.fieldOf("texture").forGetter(BoarwarfType::texture),
			ResourceLocation.CODEC.fieldOf("texture_full").forGetter(BoarwarfType::textureFull)
		).apply(instance, (texture, textureFull) ->
			new BoarwarfType(null, texture, textureFull)
		));


	public BoarwarfType(Holder<Biome> biome, ResourceLocation texture) {
		this(biome, texture, fullTextureId(texture));
	}

	private static ResourceLocation fullTextureId(ResourceLocation location) {
		return location.withPath((string) -> "textures/" + string + ".png");
	}
}
