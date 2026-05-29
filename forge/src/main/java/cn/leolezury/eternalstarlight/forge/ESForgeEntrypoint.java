package cn.leolezury.eternalstarlight.forge;

import cn.leolezury.eternalstarlight.common.EternalStarlight;
import cn.leolezury.eternalstarlight.common.handler.ESCommonSetupHandler;
import cn.leolezury.eternalstarlight.forge.registry.ESFluidTypes;
import net.minecraft.core.registries.Registries;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegisterEvent;

@Mod(EternalStarlight.ID)
public class ESForgeEntrypoint {

	public ESForgeEntrypoint(IEventBus modBus) {
		ESFluidTypes.loadClass();
		EternalStarlight.init();
		modBus.addListener(this::onRegister);
		//modBus.addListener(this::onNewRegistry);
		//for (DeferredRegister<?> register : ESForgePlatform.REGISTERS) {
		//	register.register(modBus);
		//}
		//ESForgePlatform.ATTACHMENT_TYPE_REGISTER.register(modBus);
		//ESRegistryRemapper.addAliases();
	}

	private void onRegister(RegisterEvent event) {
		if (event.getRegistryKey().equals(Registries.CHUNK_GENERATOR)) {
			ESCommonSetupHandler.registerChunkGenerator();
		} else if (event.getRegistryKey().equals(Registries.BIOME_SOURCE)) {
			ESCommonSetupHandler.registerBiomeSource();
		}
	}

	/*
	private void onNewRegistry(NewRegistryEvent event) {
		for (Registry<?> registry : ESForgePlatform.NEW_REGISTRIES) {
			event.register(registry);
		}
	}
	 */

}