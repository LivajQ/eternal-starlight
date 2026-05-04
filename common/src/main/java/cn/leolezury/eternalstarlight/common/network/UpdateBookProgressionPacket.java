package cn.leolezury.eternalstarlight.common.network;

import cn.leolezury.eternalstarlight.common.EternalStarlight;
import cn.leolezury.eternalstarlight.common.util.ESBookUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.HashSet;
import java.util.Set;

public class UpdateBookProgressionPacket implements ESPacket {

	private final Set<ResourceLocation> ids;

	public UpdateBookProgressionPacket(Set<ResourceLocation> ids) {
		this.ids = ids;
	}

	public static UpdateBookProgressionPacket read(FriendlyByteBuf buf) {
		int size = buf.readVarInt();
		Set<ResourceLocation> ids = new HashSet<>(size);
		for (int i = 0; i < size; i++) {
			ids.add(new ResourceLocation(buf.readUtf()));
		}
		return new UpdateBookProgressionPacket(ids);
	}

	@Override
	public void write(FriendlyByteBuf buf) {
		buf.writeVarInt(ids.size());
		for (ResourceLocation rl : ids) {
			buf.writeUtf(rl.toString());
		}
	}

	@Override
	public void handle(Player player) {
		if (player instanceof ServerPlayer serverPlayer) {
			ESBookUtil.unlock(serverPlayer, ids.toArray(new ResourceLocation[0]));
		}
	}

	@Override
	public ResourceLocation id() {
		return EternalStarlight.id("update_book_progression");
	}

	public Set<ResourceLocation> ids() {
		return ids;
	}
}
