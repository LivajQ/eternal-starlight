package cn.leolezury.eternalstarlight.common.datagen;

import cn.leolezury.eternalstarlight.common.EternalStarlight;
import cn.leolezury.eternalstarlight.common.datagen.provider.*;
import cn.leolezury.eternalstarlight.common.datagen.provider.advancement.ESAdvancementProvider;
import cn.leolezury.eternalstarlight.common.datagen.provider.book.ESBookDefinitionProvider;
import cn.leolezury.eternalstarlight.common.datagen.provider.loot.ESLootProvider;
import cn.leolezury.eternalstarlight.common.datagen.provider.model.ESBlockStateProvider;
import cn.leolezury.eternalstarlight.common.datagen.provider.model.ESItemModelProvider;
import cn.leolezury.eternalstarlight.common.datagen.provider.tags.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = EternalStarlight.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
	@SubscribeEvent
	public static void onGatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		PackOutput output = generator.getPackOutput();
		CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
		ExistingFileHelper helper = event.getExistingFileHelper();

		generator.addProvider(event.includeClient(), new ESBlockStateProvider(output, helper));
		generator.addProvider(event.includeClient(), new ESItemModelProvider(output, helper));
		generator.addProvider(event.includeClient(), new ESAtlasProvider(output, lookupProvider, helper));
		generator.addProvider(event.includeClient(), new ESParticleDescriptionProvider(output, helper));
		generator.addProvider(event.includeClient(), new ESSoundProvider(output, helper));
		generator.addProvider(event.includeClient(), new ESBookDefinitionProvider(output, lookupProvider, helper));

		ESBlockTagsProvider blockTagsProvider = new ESBlockTagsProvider(output, lookupProvider, helper);
		generator.addProvider(event.includeServer(), blockTagsProvider);
		generator.addProvider(event.includeServer(), new ESItemTagsProvider(output, lookupProvider, blockTagsProvider.contentsGetter(), helper));
		generator.addProvider(event.includeServer(), new ESEntityTypeTagsProvider(output, lookupProvider, helper));
		generator.addProvider(event.includeServer(), new ESFluidTagsProvider(output, lookupProvider, helper));
		generator.addProvider(event.includeServer(), new ESMobEffectTagsProvider(output, lookupProvider, helper));

		DatapackBuiltinEntriesProvider registryProvider = new ESRegistryProvider(output, lookupProvider);
		CompletableFuture<HolderLookup.Provider> lookup = registryProvider.getRegistryProvider();
		generator.addProvider(event.includeServer(), registryProvider);
		generator.addProvider(event.includeServer(), new ESDamageTypeTagsProvider(output, lookup, helper));
		generator.addProvider(event.includeServer(), new ESBiomeTagsProvider(output, lookup, helper));
		generator.addProvider(event.includeServer(), new ESStructureTagsProvider(output, lookup, helper));
		//generator.addProvider(event.includeServer(), new ESEnchantmentTagsProvider(output, lookup, helper));
		generator.addProvider(event.includeServer(), new ESPaintingVariantTagsProvider(output, lookup, helper));

		generator.addProvider(event.includeServer(), new ESLootProvider(output, lookup));
		generator.addProvider(event.includeServer(), new ESAdvancementProvider(output, lookup, helper));
		generator.addProvider(event.includeServer(), new ESRecipeProvider(output));
	}
}
