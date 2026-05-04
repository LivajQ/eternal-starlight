package cn.leolezury.eternalstarlight.common.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public interface ESPacket {
	void write(FriendlyByteBuf buf);
	void handle(Player player);
	ResourceLocation id();
}

