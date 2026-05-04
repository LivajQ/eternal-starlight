package cn.leolezury.eternalstarlight.common.network;

import cn.leolezury.eternalstarlight.common.EternalStarlight;
import cn.leolezury.eternalstarlight.common.util.ESMiscUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class ClientMountPacket implements ESPacket {
	private final int riderId;
	private final int vehicleId;

	public ClientMountPacket(int riderId, int vehicleId) {
		this.riderId = riderId;
		this.vehicleId = vehicleId;
	}

	public static ClientMountPacket read(FriendlyByteBuf buf) {
		return new ClientMountPacket(buf.readInt(), buf.readInt());
	}

	@Override
	public void write(FriendlyByteBuf buf) {
		buf.writeInt(riderId);
		buf.writeInt(vehicleId);
	}

	@Override
	public void handle(Player player) {
		ESMiscUtil.runWhenOnClient(() ->
			() -> EternalStarlight.getClientHelper().handleClientMount(this)
		);
	}

	@Override
	public ResourceLocation id() {
		return EternalStarlight.id("client_mount");
	}

	public int riderId() { return riderId; }
	public int vehicleId() { return vehicleId; }
}
