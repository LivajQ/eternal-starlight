package cn.leolezury.eternalstarlight.forge.event;

import cn.leolezury.eternalstarlight.common.EternalStarlight;
import cn.leolezury.eternalstarlight.common.handler.ESCommonSetupHandler;
import cn.leolezury.eternalstarlight.common.registry.ESBlocks;
import cn.leolezury.eternalstarlight.forge.registry.ESFluidTypes;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidInteractionRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class ForgeCommonSetupEvents {

	@Mod.EventBusSubscriber(modid = EternalStarlight.ID)
	public static class ForgeEvents {

		@SubscribeEvent
		public static void onSetup(FMLCommonSetupEvent event) {
			event.enqueueWork(() -> FluidInteractionRegistry.addInteraction(ESFluidTypes.ETHER.get(), new FluidInteractionRegistry.InteractionInformation((level, blockPos, relativePos, fluidState) -> level.getFluidState(relativePos).is(FluidTags.LAVA) && !level.getBlockState(relativePos).is(ESBlocks.ETHER.get()), ESBlocks.MOLTEN_STELLAGMITE.get().defaultBlockState())));
			event.enqueueWork(() -> FluidInteractionRegistry.addInteraction(ESFluidTypes.ETHER.get(), new FluidInteractionRegistry.InteractionInformation((level, blockPos, relativePos, fluidState) -> !level.getFluidState(relativePos).isEmpty() && !level.getFluidState(relativePos).is(FluidTags.LAVA) && !level.getBlockState(relativePos).is(ESBlocks.ETHER.get()), ESBlocks.THIOQUARTZ_BLOCK.get().defaultBlockState())));
			event.enqueueWork(ESCommonSetupHandler::commonSetup);
		}
	}

	@Mod.EventBusSubscriber(modid = EternalStarlight.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class ModEvents {

		@SubscribeEvent
		public static void onAttributeCreate(EntityAttributeCreationEvent event) {
			ESCommonSetupHandler.createAttributes(event::put);
		}

		@SubscribeEvent
		public static void onSpawnPlacementRegister(SpawnPlacementRegisterEvent event) {

			ESCommonSetupHandler.SpawnPlacementRegisterStrategy strategy =
				new ESCommonSetupHandler.SpawnPlacementRegisterStrategy() {
					@Override
					public <T extends Mob> void register(
						EntityType<T> entityType,
						SpawnPlacements.Type placementType,
						Heightmap.Types heightmap,
						SpawnPlacements.SpawnPredicate<T> predicate
					) {
						event.register(entityType, placementType, heightmap, predicate, SpawnPlacementRegisterEvent.Operation.AND);
					}
				};

			ESCommonSetupHandler.registerSpawnPlacements(strategy);
		}
	}
}
