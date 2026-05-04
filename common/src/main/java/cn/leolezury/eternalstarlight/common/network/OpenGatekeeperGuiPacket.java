package cn.leolezury.eternalstarlight.common.network;

import cn.leolezury.eternalstarlight.common.EternalStarlight;
import cn.leolezury.eternalstarlight.common.util.ESMiscUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class OpenGatekeeperGuiPacket implements ESPacket {
	private final int id;
	private final boolean challenged;

	public OpenGatekeeperGuiPacket(int id, boolean challenged) {
		this.id = id;
		this.challenged = challenged;
	}

	public static OpenGatekeeperGuiPacket read(FriendlyByteBuf buf) {
		return new OpenGatekeeperGuiPacket(
			buf.readInt(),
			buf.readBoolean()
		);
	}

	@Override
	public void write(FriendlyByteBuf buf) {
		buf.writeInt(id);
		buf.writeBoolean(challenged);
	}

	@Override
	public void handle(Player player) {
		ESMiscUtil.runWhenOnClient(() ->
			() -> EternalStarlight.getClientHelper().handleOpenGatekeeperGui(this)
		);
	}

	@Override
	public ResourceLocation id() {
		return EternalStarlight.id("open_gatekeeper_gui");
	}

	public int gatekeeperId() {
		return id;
	}

	public boolean challenged() { return challenged; }
}
