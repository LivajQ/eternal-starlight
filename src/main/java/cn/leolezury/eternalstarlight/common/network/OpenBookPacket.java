package cn.leolezury.eternalstarlight.common.network;

import cn.leolezury.eternalstarlight.common.EternalStarlight;
import cn.leolezury.eternalstarlight.common.util.ESMiscUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.HashSet;
import java.util.Set;

public class OpenBookPacket implements ESPacket {
	private final ResourceLocation bookId;
	private final Set<ResourceLocation> unlocked;

	public OpenBookPacket(ResourceLocation bookId, Set<ResourceLocation> unlocked) {
		this.bookId = bookId;
		this.unlocked = unlocked;
	}

	public static OpenBookPacket read(FriendlyByteBuf buf) {
		ResourceLocation bookId = new ResourceLocation(buf.readUtf());

		int size = buf.readInt();
		Set<ResourceLocation> unlocked = new HashSet<>();

		for (int i = 0; i < size; i++) {
			unlocked.add(new ResourceLocation(buf.readUtf()));
		}

		return new OpenBookPacket(bookId, unlocked);
	}

	@Override
	public void write(FriendlyByteBuf buf) {
		buf.writeUtf(bookId.toString());
		buf.writeInt(unlocked.size());
		for (ResourceLocation rl : unlocked) {
			buf.writeUtf(rl.toString());
		}
	}

	@Override
	public void handle(Player player) {
		ESMiscUtil.runWhenOnClient(() ->
			() -> EternalStarlight.getClientHelper().handleOpenBook(this)
		);
	}

	@Override
	public ResourceLocation id() {
		return EternalStarlight.id("open_book");
	}

	public ResourceLocation bookId() { return bookId; }
	public Set<ResourceLocation> unlocked() { return unlocked; }
}
