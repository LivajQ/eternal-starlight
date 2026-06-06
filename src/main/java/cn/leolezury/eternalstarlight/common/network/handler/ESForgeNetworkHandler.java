package cn.leolezury.eternalstarlight.common.network.handler;

import cn.leolezury.eternalstarlight.common.EternalStarlight;
import cn.leolezury.eternalstarlight.common.network.ESPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class ESForgeNetworkHandler {
	private static final String PROTOCOL = "1";
	public static SimpleChannel CHANNEL;

	public static void register() {
		CHANNEL = NetworkRegistry.newSimpleChannel(
			new ResourceLocation(EternalStarlight.ID, "main"),
			() -> PROTOCOL,
			PROTOCOL::equals,
			PROTOCOL::equals
		);

		int id = 0;

		// Only ONE registered message: the wrapper
		CHANNEL.registerMessage(
			id++,
			ForgePayloadPacket.class,
			ForgePayloadPacket::encode,
			ForgePayloadPacket::decode,
			ForgePayloadPacket::handle
		);
	}

	public static void sendToServer(ESPacket packet) {
		CHANNEL.sendToServer(new ForgePayloadPacket(packet));
	}

	public static void sendToClient(ServerPlayer player, ESPacket packet) {
		CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new ForgePayloadPacket(packet));
	}

	public static void sendToTracking(ServerLevel level, Entity entity, ESPacket packet) {
		level.getChunkSource().broadcast(entity, CHANNEL.toVanillaPacket(new ForgePayloadPacket(packet), NetworkDirection.PLAY_TO_CLIENT));
	}
}