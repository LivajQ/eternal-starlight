package cn.leolezury.eternalstarlight.forge;

import cn.leolezury.eternalstarlight.common.EternalStarlight;
import cn.leolezury.eternalstarlight.common.handler.ESCommonSetupHandler;
import cn.leolezury.eternalstarlight.forge.platform.ESForgePlatform;
import cn.leolezury.eternalstarlight.forge.registry.ESFluidTypes;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.NewRegistryEvent;
import net.minecraftforge.registries.RegisterEvent;

@Mod(EternalStarlight.ID)
public class ESForgeEntrypoint {

	public ESForgeEntrypoint(FMLJavaModLoadingContext context) {
		IEventBus modBus = context.getModEventBus();
		modBus.addListener(this::onCommonSetup);
		modBus.addListener(this::onRegister);
		modBus.addListener(this::onNewRegistry);
		for (DeferredRegister<?> register : ESForgePlatform.REGISTERS) {
			register.register(modBus);
		}
		//ESForgePlatform.ATTACHMENT_TYPE_REGISTER.register(modBus);
		//ESRegistryRemapper.addAliases();
	}

	private void onCommonSetup(FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			ESFluidTypes.loadClass();
			EternalStarlight.init();
		});
	}

	private void onRegister(RegisterEvent event) {
		if (event.getRegistryKey().equals(Registries.CHUNK_GENERATOR)) {
			ESCommonSetupHandler.registerChunkGenerator();
		} else if (event.getRegistryKey().equals(Registries.BIOME_SOURCE)) {
			ESCommonSetupHandler.registerBiomeSource();
		}
	}

	private void onNewRegistry(NewRegistryEvent event) {
		for (Pair<ResourceKey<? extends Registry<?>>, Registry<?>> pair : ESForgePlatform.PENDING_NEW_REGISTRIES) {
			ESForgePlatform.registerIntoRoot(pair.getFirst(), pair.getSecond());
		}
	}
}