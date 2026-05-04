package cn.leolezury.eternalstarlight.common.network;

import cn.leolezury.eternalstarlight.common.EternalStarlight;
import cn.leolezury.eternalstarlight.common.client.weather.ClientWeatherState;
import cn.leolezury.eternalstarlight.common.registry.ESWeathers;
import cn.leolezury.eternalstarlight.common.util.ESMiscUtil;
import cn.leolezury.eternalstarlight.common.weather.AbstractWeather;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class UpdateWeatherPacket implements ESPacket {

	private final AbstractWeather weather;

	public UpdateWeatherPacket(AbstractWeather weather) {
		this.weather = weather;
	}

	public static UpdateWeatherPacket read(FriendlyByteBuf buf) {
		AbstractWeather weather = ESWeathers.WEATHERS.registry().byId(buf.readVarInt());
		return new UpdateWeatherPacket(weather);
	}

	@Override
	public void write(FriendlyByteBuf buf) {
		buf.writeVarInt(ESWeathers.WEATHERS.registry().getId(weather));
	}

	@Override
	public void handle(Player player) {
		ESMiscUtil.runWhenOnClient(() ->
			() -> ClientWeatherState.weather = weather
		);
	}

	@Override
	public ResourceLocation id() {
		return EternalStarlight.id("update_weather");
	}

	public AbstractWeather weather() {
		return weather;
	}
}
