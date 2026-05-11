package cn.leolezury.eternalstarlight.common.weather;

import cn.leolezury.eternalstarlight.common.registry.ESWeathers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Weathers extends SavedData {
	private static final String TAG_WEATHERS = "weathers";

	private final ServerLevel serverLevel;
	private final List<WeatherInstance> weathers = new ArrayList<>();

	public Weathers(ServerLevel serverLevel) {
		this.serverLevel = serverLevel;

		ESWeathers.WEATHERS.registry().forEach(weather -> {
			WeatherInstance instance = new WeatherInstance(serverLevel, weather);
			weathers.add(instance);
		});
	}

	public List<WeatherInstance> getWeathers() {
		return weathers;
	}

	public Optional<WeatherInstance> getActiveWeather() {
		for (WeatherInstance instance : weathers) {
			if (instance.active) {
				return Optional.of(instance);
			}
		}
		return Optional.empty();
	}

	public void setActiveWeather(AbstractWeather weather, int duration) {
		for (WeatherInstance instance : weathers) {
			if (!instance.active && instance.getWeather() == weather) {
				instance.start();
				instance.currentDuration = duration;
			}
			if (instance.active && instance.getWeather() != weather) {
				instance.stop();
			}
		}
		setDirty();
	}

	public void clearAllWeathers(int duration) {
		for (WeatherInstance instance : weathers) {
			instance.stop();
			instance.ticksUntilNext = duration;
		}
		setDirty();
	}

	public static Weathers get(ServerLevel level) {
		return level.getDataStorage().computeIfAbsent(
			tag -> load(level, tag),
			() -> new Weathers(level),
			"weathers"
		);
	}

	public void tick() {
		List<WeatherInstance> canStart = new ArrayList<>();
		boolean hasActive = false;

		for (WeatherInstance instance : weathers) {
			if (instance.tick()) {
				canStart.add(instance);
			}
			hasActive |= instance.active;
		}

		if (!hasActive && !canStart.isEmpty()) {
			canStart.get(serverLevel.getRandom().nextInt(canStart.size())).start();
		}

		setDirty();
	}

	public static Weathers load(ServerLevel serverLevel, CompoundTag tag) {
		Weathers data = new Weathers(serverLevel);

		if (tag.contains(TAG_WEATHERS, CompoundTag.TAG_COMPOUND)) {
			CompoundTag weathersTag = tag.getCompound(TAG_WEATHERS);

			data.weathers.clear();

			ESWeathers.WEATHERS.registry().forEach(weather -> {
				WeatherInstance instance = new WeatherInstance(serverLevel, weather);
				String id = Objects.requireNonNull(ESWeathers.WEATHERS.registry().getKey(weather)).toString();

				if (weathersTag.contains(id, CompoundTag.TAG_COMPOUND)) {
					instance.load(weathersTag.getCompound(id));
				}

				data.weathers.add(instance);
			});
		}

		return data;
	}

	@Override
	public CompoundTag save(CompoundTag tag) {
		CompoundTag weathersTag = new CompoundTag();

		for (WeatherInstance instance : weathers) {
			String id = Objects.requireNonNull(ESWeathers.WEATHERS.registry().getKey(instance.getWeather())).toString();
			CompoundTag weatherTag = new CompoundTag();
			instance.save(weatherTag);
			weathersTag.put(id, weatherTag);
		}

		tag.put(TAG_WEATHERS, weathersTag);
		return tag;
	}
}
