package cn.leolezury.eternalstarlight.common.critereon;

import cn.leolezury.eternalstarlight.common.data.ESDimensions;
import cn.leolezury.eternalstarlight.common.handler.ESCommonHandler;
import cn.leolezury.eternalstarlight.common.registry.ESWeathers;
import cn.leolezury.eternalstarlight.common.weather.AbstractWeather;
import cn.leolezury.eternalstarlight.common.weather.WeatherInstance;
import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;

import java.util.Optional;

public class WitnessWeatherTrigger extends SimpleCriterionTrigger<WitnessWeatherTrigger.TriggerInstance> {

	private static final ResourceLocation ID = new ResourceLocation("eternalstarlight", "witness_weather");

	@Override
	public ResourceLocation getId() {
		return ID;
	}

	public void trigger(ServerPlayer player) {
		this.trigger(player, inst -> inst.matches(player.serverLevel()));
	}

	@Override
	protected TriggerInstance createInstance(JsonObject json, ContextAwarePredicate player, DeserializationContext ctx) {
		String weatherId = GsonHelper.getAsString(json, "weather");
		ResourceLocation weatherKey = new ResourceLocation(weatherId);

		return new TriggerInstance(player, weatherKey);
	}

	public static class TriggerInstance extends AbstractCriterionTriggerInstance {

		private final ResourceLocation weatherKey;

		public TriggerInstance(ContextAwarePredicate player, ResourceLocation weatherKey) {
			super(WitnessWeatherTrigger.ID, player);
			this.weatherKey = weatherKey;
		}

		public boolean matches(ServerLevel level) {
			Optional<WeatherInstance> active = ESCommonHandler.getActiveWeather();

			if (active.isEmpty()) return false;

			Registry<AbstractWeather> registry =
				level.registryAccess().registryOrThrow(ESWeathers.REGISTRY_KEY);

			Holder<AbstractWeather> expected =
				registry.getHolder(ResourceKey.create(ESWeathers.REGISTRY_KEY, weatherKey))
					.orElse(null);

			return expected != null
				&& level.dimension().location().equals(ESDimensions.STARLIGHT_KEY.location())
				&& expected.isBound()
				&& active.get().getWeather() == expected.value();
		}

		@Override
		public JsonObject serializeToJson(SerializationContext ctx) {
			JsonObject json = super.serializeToJson(ctx);
			json.addProperty("weather", weatherKey.toString());
			return json;
		}
	}
}
