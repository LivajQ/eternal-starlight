package cn.leolezury.eternalstarlight.common.network;

import cn.leolezury.eternalstarlight.common.EternalStarlight;
import cn.leolezury.eternalstarlight.common.util.ESMiscUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class ClientDismountPacket implements ESPacket {
	private final int riderId;

	public ClientDismountPacket(int riderId) {
		this.riderId = riderId;
	}

	public static ClientDismountPacket read(FriendlyByteBuf buf) {
		return new ClientDismountPacket(buf.readInt());
	}

	@Override
	public void write(FriendlyByteBuf buf) {
		buf.writeInt(riderId);
	}

	@Override
	public void handle(Player player) {
		ESMiscUtil.runWhenOnClient(() ->
			() -> EternalStarlight.getClientHelper().handleClientDismount(this)
		);
	}

	@Override
	public ResourceLocation id() {
		return EternalStarlight.id("client_dismount");
	}

	public int riderId() {
		return riderId;
	}
}
