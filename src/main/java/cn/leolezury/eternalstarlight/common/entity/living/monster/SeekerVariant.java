package cn.leolezury.eternalstarlight.common.entity.living.monster;

import cn.leolezury.eternalstarlight.common.data.ESRegistries;
import cn.leolezury.eternalstarlight.common.data.ESSeekerVariants;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.biome.Biome;

import java.util.Optional;

public record SeekerVariant(ResourceLocation texture, ResourceLocation textureFull, ResourceLocation glowTexture, ResourceLocation glowTextureFull, ResourceLocation tentacleTexture, ResourceLocation tentacleTextureFull, ResourceLocation tentacleEndTexture, ResourceLocation tentacleEndTextureFull, int particleColor, HolderSet<Biome> biomes) {
	public static final Codec<SeekerVariant> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		ResourceLocation.CODEC.fieldOf("texture").forGetter(SeekerVariant::texture),
		ResourceLocation.CODEC.fieldOf("glow_texture").forGetter(SeekerVariant::glowTexture),
		ResourceLocation.CODEC.fieldOf("tentacle_texture").forGetter(SeekerVariant::tentacleTexture),
		ResourceLocation.CODEC.fieldOf("tentacle_end_texture").forGetter(SeekerVariant::tentacleEndTexture),
		Codec.INT.fieldOf("particle_color").forGetter(SeekerVariant::particleColor),
		RegistryCodecs.homogeneousList(Registries.BIOME).fieldOf("biomes").forGetter(SeekerVariant::biomes)
	).apply(instance, SeekerVariant::new));

	public static final Codec<SeekerVariant> NETWORK_CODEC =
		RecordCodecBuilder.create(instance -> instance.group(
			ResourceLocation.CODEC.fieldOf("texture").forGetter(SeekerVariant::texture),
			ResourceLocation.CODEC.fieldOf("texture_full").forGetter(SeekerVariant::textureFull),
			ResourceLocation.CODEC.fieldOf("glow_texture").forGetter(SeekerVariant::glowTexture),
			ResourceLocation.CODEC.fieldOf("glow_texture_full").forGetter(SeekerVariant::glowTextureFull),
			ResourceLocation.CODEC.fieldOf("tentacle_texture").forGetter(SeekerVariant::tentacleTexture),
			ResourceLocation.CODEC.fieldOf("tentacle_texture_full").forGetter(SeekerVariant::tentacleTextureFull),
			ResourceLocation.CODEC.fieldOf("tentacle_end_texture").forGetter(SeekerVariant::tentacleEndTexture),
			ResourceLocation.CODEC.fieldOf("tentacle_end_texture_full").forGetter(SeekerVariant::tentacleEndTextureFull),
			Codec.INT.fieldOf("particle_color").forGetter(SeekerVariant::particleColor)
		).apply(instance, (texture, textureFull, glow, glowFull, tent, tentFull, end, endFull, color) ->
			new SeekerVariant(texture, textureFull, glow, glowFull, tent, tentFull, end, endFull, color, HolderSet.direct())
		));


	public SeekerVariant(ResourceLocation texture, ResourceLocation glowTexture, ResourceLocation tentacleTexture, ResourceLocation tentacleEndTexture, int particleColor, HolderSet<Biome> biomes) {
		this(texture, fullTextureId(texture), glowTexture, fullTextureId(glowTexture), tentacleTexture, fullTextureId(tentacleTexture), tentacleEndTexture, fullTextureId(tentacleEndTexture), particleColor, biomes);
	}

	private static ResourceLocation fullTextureId(ResourceLocation location) {
		return location.withPath((string) -> "textures/" + string + ".png");
	}

	public static Holder<SeekerVariant> getSpawnVariant(RegistryAccess registryAccess, Holder<Biome> holder) {
		Registry<SeekerVariant> registry = registryAccess.registryOrThrow(ESRegistries.SEEKER_VARIANT);
		Optional<Holder.Reference<SeekerVariant>> optional = registry.holders().filter(ref -> ref.value().biomes().contains(holder)).findFirst().or(() -> registry.getHolder(ESSeekerVariants.LUNAR));
		return optional.or(() -> registry.getRandom(RandomSource.create())).orElseThrow();
	}

}
