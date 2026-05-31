package cn.leolezury.eternalstarlight.forge.datagen.provider;

import cn.leolezury.eternalstarlight.common.EternalStarlight;
import net.minecraft.client.renderer.texture.atlas.sources.DirectoryLister;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.SpriteSourceProvider;

import java.util.concurrent.CompletableFuture;

public class ESAtlasProvider extends SpriteSourceProvider {
	public ESAtlasProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
		super(output, existingFileHelper, EternalStarlight.ID);
	}

	@Override
	protected void addSources() {
		this.atlas(SHIELD_PATTERNS_ATLAS).addSource(new DirectoryLister("entity/shields", "entity/shields/"));
	}
}