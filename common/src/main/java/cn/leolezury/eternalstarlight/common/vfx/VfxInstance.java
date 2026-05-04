package cn.leolezury.eternalstarlight.common.vfx;

import cn.leolezury.eternalstarlight.common.network.VfxPacket;
import cn.leolezury.eternalstarlight.common.platform.ESPlatform;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;

import java.util.Optional;

public record VfxInstance(Optional<SyncedVfxType> type, CompoundTag data) {

	public VfxInstance(SyncedVfxType type, CompoundTag data) {
		this(Optional.of(type), data);
	}

	public void write(FriendlyByteBuf buf) {
		buf.writeBoolean(type.isPresent());

		if (type.isPresent()) {
			SyncedVfxType t = type.get();
			int id = VfxRegistry.getId(t);
			buf.writeVarInt(id);
		}

		buf.writeNbt(data);
	}

	public static VfxInstance read(FriendlyByteBuf buf) {
		Optional<SyncedVfxType> type;

		boolean hasType = buf.readBoolean();
		if (hasType) {
			int id = buf.readVarInt();
			SyncedVfxType t = VfxRegistry.byId(id);
			type = Optional.ofNullable(t);
		} else {
			type = Optional.empty();
		}

		CompoundTag data = buf.readNbt();
		return new VfxInstance(type, data);
	}

	public void send(ServerLevel level) {
		ESPlatform.INSTANCE.sendToAllClients(level, new VfxPacket(this));
	}
}
