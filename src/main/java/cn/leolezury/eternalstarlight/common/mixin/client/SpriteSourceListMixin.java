package cn.leolezury.eternalstarlight.common.mixin.client;

import net.minecraft.client.renderer.texture.atlas.SpriteSources;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SpriteSources.class)
public abstract class SpriteSourceListMixin {

	/* TODO sth
	@Inject(method = "<init>", at = @At("RETURN"))
	private void init(List<SpriteSource> list, CallbackInfo ci) {
		for (SpriteSource source : list) {
			if (source instanceof PalettedPermutationsAccessor permutations && permutations.getPaletteKey().getPath().equals("trims/color_palettes/trim_palette")) {
				List<ResourceLocation> textures = new ArrayList<>(permutations.getTextures());
				for (ResourceKey<TrimPattern> key : ESTrimPatterns.TRIM_PATTERNS) {
					textures.add(key.location().withPrefix("trims/models/armor/"));
					textures.add(key.location().withPrefix("trims/models/armor/").withSuffix("_leggings"));
				}
				permutations.setTextures(textures);
				Map<String, ResourceLocation> map = new HashMap<>(permutations.getPermutations());
				for (ResourceKey<TrimMaterial> key : ESTrimMaterials.TRIM_MATERIALS) {
					map.put(key.location().getPath(), key.location().withPrefix("trims/color_palettes/"));
				}
				permutations.setPermutations(map);
			}
		}
	}

	@Mixin(PalettedPermutations.class)
	private interface PalettedPermutationsAccessor {
		@Accessor
		List<ResourceLocation> getTextures();

		@Accessor("textures")
		void setTextures(List<ResourceLocation> value);

		@Accessor
		Map<String, ResourceLocation> getPermutations();

		@Accessor("permutations")
		void setPermutations(Map<String, ResourceLocation> value);

		@Accessor
		ResourceLocation getPaletteKey();
	}
	 */
}
