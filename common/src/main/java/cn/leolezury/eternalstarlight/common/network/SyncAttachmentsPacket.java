package cn.leolezury.eternalstarlight.common.network;

import cn.leolezury.eternalstarlight.common.platform.EntityDataAttachment;
import cn.leolezury.eternalstarlight.common.registry.ESDataAttachments;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class SyncAttachmentsPacket {
	private final int entityId;
	private final ResourceLocation attachmentId;
	private final byte[] data;

	public SyncAttachmentsPacket(int entityId, EntityDataAttachment<?> attachment, @Nullable Object value) {
		this.entityId = entityId;
		this.attachmentId = attachment.id();

		FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
		buf.writeBoolean(value != null);
		if (value != null) {
			@SuppressWarnings("unchecked")
			EntityDataAttachment<Object> att = (EntityDataAttachment<Object>) attachment;
			att.writeToNetwork(buf, value);
		}
		this.data = buf.array();
	}

	public SyncAttachmentsPacket(int entityId, ResourceLocation attachmentId, byte[] data) {
		this.entityId = entityId;
		this.attachmentId = attachmentId;
		this.data = data;
	}

	public static SyncAttachmentsPacket read(FriendlyByteBuf buf) {
		int entityId = buf.readInt();
		ResourceLocation attachmentId = buf.readResourceLocation();
		int len = buf.readVarInt();
		byte[] data = buf.readByteArray(len);
		return new SyncAttachmentsPacket(entityId, attachmentId, data);
	}

	public void write(FriendlyByteBuf buf) {
		buf.writeInt(entityId);
		buf.writeResourceLocation(attachmentId);
		buf.writeVarInt(data.length);
		buf.writeByteArray(data);
	}

	public void handle(Player player) {
		Level level = player.level();
		Entity entity = level.getEntity(entityId);
		if (entity == null) return;

		EntityDataAttachment<?> raw = ESDataAttachments.byId(attachmentId);
		if (raw == null) return;

		@SuppressWarnings("unchecked")
		EntityDataAttachment<Object> attachment = (EntityDataAttachment<Object>) raw;

		FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.wrappedBuffer(data));
		boolean hasValue = buf.readBoolean();
		Object value = hasValue ? attachment.readFromNetwork(buf) : null;

		if (value == null) {
			attachment.removeData(entity);
		} else {
			attachment.setData(entity, value);
		}
	}
}
