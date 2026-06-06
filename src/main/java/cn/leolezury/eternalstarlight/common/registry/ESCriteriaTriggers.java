package cn.leolezury.eternalstarlight.common.registry;

import cn.leolezury.eternalstarlight.common.EternalStarlight;
import cn.leolezury.eternalstarlight.common.critereon.WitnessWeatherTrigger;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.advancements.critereon.PlayerTrigger;

import net.minecraft.advancements.CriteriaTriggers;

public  class ESCriteriaTriggers {

	public static final PlayerTrigger CHALLENGED_GATEKEEPER =
		register(new PlayerTrigger(EternalStarlight.id("challenged_gatekeeper")));

	public static final WitnessWeatherTrigger WITNESS_WEATHER =
		register(new WitnessWeatherTrigger());

	public static final PlayerTrigger THROW_GLEECH_EGG =
		register(new PlayerTrigger(EternalStarlight.id("throw_gleech_egg")));

	public static final PlayerTrigger DEACTIVATE_ENERGY_BLOCK =
		register(new PlayerTrigger(EternalStarlight.id("deactivate_energy_block")));

	public static final PlayerTrigger FREEZE_STARLIGHT_GOLEM =
		register(new PlayerTrigger(EternalStarlight.id("freeze_starlight_golem")));

	public static final PlayerTrigger CHAIN_TANGLED_SKULL_EXPLOSION =
		register(new PlayerTrigger(EternalStarlight.id("chain_tangled_skull_explosion")));

	public static final PlayerTrigger SATURATE_DAGGER_OF_HUNGER =
		register(new PlayerTrigger(EternalStarlight.id("saturate_dagger_of_hunger")));

	public static final PlayerTrigger IGNITE_TEAR_BOMB =
		register(new PlayerTrigger(EternalStarlight.id("ignite_tear_bomb")));

	public static final PlayerTrigger WITNESS_STRANGHOUL_HUNT =
		register(new PlayerTrigger(EternalStarlight.id("witness_stranghoul_hunt")));

	public static final PlayerTrigger HIRE_STRANGHOUL =
		register(new PlayerTrigger(EternalStarlight.id("hire_stranghoul")));

	public static final PlayerTrigger HAMMER_CRITICAL_HIT =
		register(new PlayerTrigger(EternalStarlight.id("hammer_critical_hit")));

	public static final PlayerTrigger PUT_SEEDS_INTO_STARFIRE_BIRD_NEST =
		register(new PlayerTrigger(EternalStarlight.id("put_seeds_into_starfire_bird_nest")));

	private static <T extends CriterionTrigger<?>> T register(T trigger) {
		return CriteriaTriggers.register(trigger);
	}

	public static void loadClass() {}
}