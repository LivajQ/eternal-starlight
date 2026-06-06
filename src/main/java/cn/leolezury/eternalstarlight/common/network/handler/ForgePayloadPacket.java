package cn.leolezury.eternalstarlight.common.network.handler;

import cn.leolezury.eternalstarlight.common.network.ESPacket;
import cn.leolezury.eternalstarlight.common.network.ESPackets;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ForgePayloadPacket {
	private final ESPacket payload;

	public ForgePayloadPacket(ESPacket payload) {
		this.payload = payload;
	}

	public static void encode(ForgePayloadPacket msg, FriendlyByteBuf buf) {
		buf.writeResourceLocation(msg.payload.id());
		msg.payload.write(buf);
	}

	public static ForgePayloadPacket decode(FriendlyByteBuf buf) {
		ResourceLocation id = buf.readResourceLocation();
		ESPacket packet = ESPackets.create(id, buf);
		return new ForgePayloadPacket(packet);
	}

	public static void handle(ForgePayloadPacket msg, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			Player player = ctx.get().getSender();
			msg.payload.handle(player);
		});
		ctx.get().setPacketHandled(true);
	}
}
