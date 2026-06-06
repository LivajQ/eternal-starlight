package cn.leolezury.eternalstarlight.common.network;

import cn.leolezury.eternalstarlight.common.EternalStarlight;
import cn.leolezury.eternalstarlight.common.entity.living.boss.gatekeeper.TheGatekeeper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class CloseGatekeeperGuiPacket implements ESPacket {
	private final int id;
	private final int operation;

	public CloseGatekeeperGuiPacket(int id, int operation) {
		this.id = id;
		this.operation = operation;
	}

	public static CloseGatekeeperGuiPacket read(FriendlyByteBuf buf) {
		return new CloseGatekeeperGuiPacket(buf.readInt(), buf.readInt());
	}

	@Override
	public void write(FriendlyByteBuf buf) {
		buf.writeInt(id);
		buf.writeInt(operation);
	}

	@Override
	public void handle(Player player) {
		if (player instanceof ServerPlayer serverPlayer) {
			Entity entity = serverPlayer.serverLevel().getEntity(id);
			if (entity instanceof TheGatekeeper gatekeeper) {
				gatekeeper.handleDialogueClose(operation);
			}
		}
	}

	@Override
	public ResourceLocation id() {
		return EternalStarlight.id("close_gatekeeper_gui");
	}

	public int gatekeeperId() { return id; }
	public int operation() { return operation; }
}
