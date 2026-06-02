package cn.leolezury.eternalstarlight.forge;

import cn.leolezury.eternalstarlight.common.EternalStarlight;
import cn.leolezury.eternalstarlight.common.handler.ESCommonSetupHandler;
import cn.leolezury.eternalstarlight.forge.platform.ESForgePlatform;
import cn.leolezury.eternalstarlight.forge.registry.ESFluidTypes;
import net.minecraft.core.registries.Registries;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.*;

import java.util.function.Supplier;

@Mod(EternalStarlight.ID)
public class ESForgeEntrypoint {

	public ESForgeEntrypoint(FMLJavaModLoadingContext context) {
		IEventBus modBus = context.getModEventBus();
		ESForgePlatform.init(modBus);
		ESFluidTypes.loadClass();
		EternalStarlight.init();
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
		for (ESForgePlatform.CustomRegistryEntry<?> entry : ESForgePlatform.CUSTOM_REGISTRIES) {
			bindEntry(event, entry);
		}
	}

	private <T> void bindEntry(NewRegistryEvent event, ESForgePlatform.CustomRegistryEntry<T> entry) {
		Supplier<IForgeRegistry<T>> sup = event.create(entry.builder);
		entry.provider.bind(sup);
	}
}