package cn.leolezury.eternalstarlight.common.network;

import cn.leolezury.eternalstarlight.common.EternalStarlight;
import cn.leolezury.eternalstarlight.common.handler.ESCommonHandler;
import cn.leolezury.eternalstarlight.common.util.ESMiscUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class SimpleActionPacket implements ESPacket {
	private final String id;

	public static final String C2S_SWING_ATTACK = "swing_attack";
	public static final String C2S_SWITCH_CREST = "switch_crest";

	public static final String S2C_CLEAR_WEATHER = "clear_weather";

	public SimpleActionPacket(String id) {
		this.id = id;
	}

	public static SimpleActionPacket read(FriendlyByteBuf buf) {
		return new SimpleActionPacket(buf.readUtf());
	}

	@Override
	public void write(FriendlyByteBuf buf) {
		buf.writeUtf(id);
	}

	@Override
	public void handle(Player player) {
		// S2C
		ESMiscUtil.runWhenOnClient(() ->
			() -> EternalStarlight.getClientHelper().handleServerToClientSimpleAction(this)
		);

		// C2S
		if (player instanceof ServerPlayer serverPlayer) {
			ESCommonHandler.onClientToServerSimpleAction(serverPlayer, id);
		}
	}

	@Override
	public ResourceLocation id() {
		return EternalStarlight.id("simple_action");
	}

	public String actionId() {
		return id;
	}
}
