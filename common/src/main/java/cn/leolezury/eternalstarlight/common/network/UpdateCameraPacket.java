package cn.leolezury.eternalstarlight.common.network;

import cn.leolezury.eternalstarlight.common.EternalStarlight;
import cn.leolezury.eternalstarlight.common.util.ESMiscUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class UpdateCameraPacket implements ESPacket {

	private final int cameraId;

	public UpdateCameraPacket(int cameraId) {
		this.cameraId = cameraId;
	}

	public static UpdateCameraPacket read(FriendlyByteBuf buf) {
		return new UpdateCameraPacket(buf.readInt());
	}

	@Override
	public void write(FriendlyByteBuf buf) {
		buf.writeInt(cameraId);
	}

	@Override
	public void handle(Player player) {
		ESMiscUtil.runWhenOnClient(() ->
			() -> EternalStarlight.getClientHelper().handleUpdateCamera(this)
		);
	}

	@Override
	public ResourceLocation id() {
		return EternalStarlight.id("update_camera");
	}

	public int cameraId() {
		return cameraId;
	}
}
