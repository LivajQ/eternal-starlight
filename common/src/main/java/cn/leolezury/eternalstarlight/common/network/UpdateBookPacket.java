package cn.leolezury.eternalstarlight.common.network;

import cn.leolezury.eternalstarlight.common.EternalStarlight;
import cn.leolezury.eternalstarlight.common.util.ESMiscUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.HashSet;
import java.util.Set;

public class UpdateBookPacket implements ESPacket {

	private final Set<ResourceLocation> oldUnlocked;
	private final Set<ResourceLocation> unlocked;

	public UpdateBookPacket(Set<ResourceLocation> oldUnlocked, Set<ResourceLocation> unlocked) {
		this.oldUnlocked = oldUnlocked;
		this.unlocked = unlocked;
	}

	public static UpdateBookPacket read(FriendlyByteBuf buf) {
		int oldSize = buf.readVarInt();
		Set<ResourceLocation> oldUnlocked = new HashSet<>(oldSize);
		for (int i = 0; i < oldSize; i++) {
			oldUnlocked.add(new ResourceLocation(buf.readUtf()));
		}

		int size = buf.readVarInt();
		Set<ResourceLocation> unlocked = new HashSet<>(size);
		for (int i = 0; i < size; i++) {
			unlocked.add(new ResourceLocation(buf.readUtf()));
		}

		return new UpdateBookPacket(oldUnlocked, unlocked);
	}

	@Override
	public void write(FriendlyByteBuf buf) {
		buf.writeVarInt(oldUnlocked.size());
		for (ResourceLocation rl : oldUnlocked) {
			buf.writeUtf(rl.toString());
		}

		buf.writeVarInt(unlocked.size());
		for (ResourceLocation rl : unlocked) {
			buf.writeUtf(rl.toString());
		}
	}

	@Override
	public void handle(Player player) {
		ESMiscUtil.runWhenOnClient(() ->
			() -> EternalStarlight.getClientHelper().handleUpdateBook(this)
		);
	}

	@Override
	public ResourceLocation id() {
		return EternalStarlight.id("update_book");
	}

	public Set<ResourceLocation> oldUnlocked() { return oldUnlocked; }
	public Set<ResourceLocation> unlocked() { return unlocked; }
}
