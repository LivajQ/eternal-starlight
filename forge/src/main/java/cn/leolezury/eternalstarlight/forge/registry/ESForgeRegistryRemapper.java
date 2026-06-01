package cn.leolezury.eternalstarlight.forge.registry;

import cn.leolezury.eternalstarlight.common.EternalStarlight;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.MissingMappingsEvent;

@Mod.EventBusSubscriber(modid = EternalStarlight.ID)
public class ESForgeRegistryRemapper {

	@SubscribeEvent
	public static void onMissingMappings(MissingMappingsEvent event) {

		// ITEMS
		for (MissingMappingsEvent.Mapping<Item> mapping :
			event.getMappings(ForgeRegistries.ITEMS.getRegistryKey(), EternalStarlight.ID)) {

			remapItem(mapping, "starlight_mangrove_sapling", "banyin_sapling");
			remapItem(mapping, "starlight_mangrove_leaves", "banyin_leaves");
			remapItem(mapping, "starlight_mangrove_log", "banyin_log");
			remapItem(mapping, "starlight_mangrove_wood", "banyin_wood");
			remapItem(mapping, "starlight_mangrove_planks", "banyin_planks");
			remapItem(mapping, "stripped_starlight_mangrove_log", "stripped_banyin_log");
			remapItem(mapping, "stripped_starlight_mangrove_wood", "stripped_banyin_wood");
			remapItem(mapping, "starlight_mangrove_door", "banyin_door");
			remapItem(mapping, "starlight_mangrove_trapdoor", "banyin_trapdoor");
			remapItem(mapping, "starlight_mangrove_pressure_plate", "banyin_pressure_plate");
			remapItem(mapping, "starlight_mangrove_button", "banyin_button");
			remapItem(mapping, "starlight_mangrove_fence", "banyin_fence");
			remapItem(mapping, "starlight_mangrove_fence_gate", "banyin_fence_gate");
			remapItem(mapping, "starlight_mangrove_slab", "banyin_slab");
			remapItem(mapping, "starlight_mangrove_stairs", "banyin_stairs");
			remapItem(mapping, "starlight_mangrove_roots", "banyin_roots");
			remapItem(mapping, "muddy_starlight_mangrove_roots", "muddy_banyin_roots");
			remapItem(mapping, "starlight_mangrove_sign", "banyin_sign");
			remapItem(mapping, "starlight_mangrove_hanging_sign", "banyin_hanging_sign");
			remapItem(mapping, "starlight_mangrove_boat", "banyin_boat");
			remapItem(mapping, "starlight_mangrove_chest_boat", "banyin_chest_boat");
			remapItem(mapping, "starlight_mangrove_starfire_bird_aviary", "banyin_starfire_bird_aviary");

			// starcore
			remapItem(mapping, "atalphaite_block", "starcore_block");
			remapItem(mapping, "blazing_atalphaite_block", "blazing_starcore_block");
			remapItem(mapping, "atalphaite_light", "starcore_light");
			remapItem(mapping, "grimstone_atalphaite_ore", "grimstone_starcore_ore");
			remapItem(mapping, "voidstone_atalphaite_ore", "voidstone_starcore_ore");
			remapItem(mapping, "eternal_ice_atalphaite_ore", "eternal_ice_starcore_ore");
			remapItem(mapping, "haze_ice_atalphaite_ore", "haze_ice_starcore_ore");
			remapItem(mapping, "atalphaite", "starcore");

			// deepsilver
			remapItem(mapping, "swamp_silver_ore", "nightfall_mud_deepsilver_ore");
			remapItem(mapping, "swamp_silver_block", "deepsilver_block");
			remapItem(mapping, "swamp_silver_ingot", "deepsilver_ingot");
			remapItem(mapping, "swamp_silver_nugget", "deepsilver_nugget");
			remapItem(mapping, "swamp_silver_sword", "deepsilver_sword");
			remapItem(mapping, "swamp_silver_pickaxe", "deepsilver_pickaxe");
			remapItem(mapping, "swamp_silver_axe", "deepsilver_axe");
			remapItem(mapping, "swamp_silver_sickle", "deepsilver_sickle");
			remapItem(mapping, "swamp_silver_helmet", "deepsilver_helmet");
			remapItem(mapping, "swamp_silver_chestplate", "deepsilver_chestplate");
			remapItem(mapping, "swamp_silver_leggings", "deepsilver_leggings");
			remapItem(mapping, "swamp_silver_boots", "deepsilver_boots");

			// soul dew
			remapItem(mapping, "trapped_soul", "soul_dew");

			// crest pot
			remapItem(mapping, "crest_pot", "flower_pot");
		}

		// BLOCKS
		for (MissingMappingsEvent.Mapping<Block> mapping :
			event.getMappings(ForgeRegistries.BLOCKS.getRegistryKey(), EternalStarlight.ID)) {

			remapBlock(mapping, "starlight_mangrove_sapling", "banyin_sapling");
			remapBlock(mapping, "starlight_mangrove_leaves", "banyin_leaves");
			remapBlock(mapping, "starlight_mangrove_log", "banyin_log");
			remapBlock(mapping, "starlight_mangrove_wood", "banyin_wood");
			remapBlock(mapping, "starlight_mangrove_planks", "banyin_planks");
			remapBlock(mapping, "stripped_starlight_mangrove_log", "stripped_banyin_log");
			remapBlock(mapping, "stripped_starlight_mangrove_wood", "stripped_banyin_wood");
			remapBlock(mapping, "starlight_mangrove_door", "banyin_door");
			remapBlock(mapping, "starlight_mangrove_trapdoor", "banyin_trapdoor");
			remapBlock(mapping, "starlight_mangrove_pressure_plate", "banyin_pressure_plate");
			remapBlock(mapping, "starlight_mangrove_button", "banyin_button");
			remapBlock(mapping, "starlight_mangrove_fence", "banyin_fence");
			remapBlock(mapping, "starlight_mangrove_fence_gate", "banyin_fence_gate");
			remapBlock(mapping, "starlight_mangrove_slab", "banyin_slab");
			remapBlock(mapping, "starlight_mangrove_stairs", "banyin_stairs");
			remapBlock(mapping, "starlight_mangrove_roots", "banyin_roots");
			remapBlock(mapping, "muddy_starlight_mangrove_roots", "muddy_banyin_roots");
			remapBlock(mapping, "starlight_mangrove_sign", "banyin_sign");
			remapBlock(mapping, "starlight_mangrove_hanging_sign", "banyin_hanging_sign");
			remapBlock(mapping, "starlight_mangrove_starfire_bird_aviary", "banyin_starfire_bird_aviary");

			// starcore
			remapBlock(mapping, "atalphaite_block", "starcore_block");
			remapBlock(mapping, "blazing_atalphaite_block", "blazing_starcore_block");
			remapBlock(mapping, "atalphaite_light", "starcore_light");
			remapBlock(mapping, "grimstone_atalphaite_ore", "grimstone_starcore_ore");
			remapBlock(mapping, "voidstone_atalphaite_ore", "voidstone_starcore_ore");
			remapBlock(mapping, "eternal_ice_atalphaite_ore", "eternal_ice_starcore_ore");
			remapBlock(mapping, "haze_ice_atalphaite_ore", "haze_ice_starcore_ore");

			// deepsilver
			remapBlock(mapping, "swamp_silver_ore", "nightfall_mud_deepsilver_ore");
			remapBlock(mapping, "swamp_silver_block", "deepsilver_block");

			// crest pot
			remapBlock(mapping, "crest_pot", "flower_pot");
		}
	}

	private static void remapItem(MissingMappingsEvent.Mapping<Item> mapping, String oldId, String newId) {
		if (mapping.getKey().getPath().equals(oldId)) {
			Item target = ForgeRegistries.ITEMS.getValue(EternalStarlight.id(newId));
			if (target != null) mapping.remap(target);
		}
	}

	private static void remapBlock(MissingMappingsEvent.Mapping<Block> mapping, String oldId, String newId) {
		if (mapping.getKey().getPath().equals(oldId)) {
			Block target = ForgeRegistries.BLOCKS.getValue(EternalStarlight.id(newId));
			if (target != null) mapping.remap(target);
		}
	}
}
