package cn.leolezury.eternalstarlight.common.network;

import cn.leolezury.eternalstarlight.common.EternalStarlight;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class TriggerEntityEventPacket implements ESPacket {
	private final int id;
	private final byte event;

	public TriggerEntityEventPacket(int id, byte event) {
		this.id = id;
		this.event = event;
	}

	public static TriggerEntityEventPacket read(FriendlyByteBuf buf) {
		return new TriggerEntityEventPacket(buf.readInt(), buf.readByte());
	}

	@Override
	public void write(FriendlyByteBuf buf) {
		buf.writeInt(id);
		buf.writeByte(event);
	}

	@Override
	public void handle(Player player) {
		Entity entity = player.level().getEntity(id);
		if (entity != null) {
			player.level().broadcastEntityEvent(entity, event);
		}
	}

	@Override
	public ResourceLocation id() {
		return EternalStarlight.id("trigger_entity_event");
	}

	public int entityId() { return id; }
	public byte eventId() { return event; }
}
