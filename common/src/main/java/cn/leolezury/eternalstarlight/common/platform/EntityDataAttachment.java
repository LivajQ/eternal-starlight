package cn.leolezury.eternalstarlight.common.platform;

import cn.leolezury.eternalstarlight.common.network.SyncAttachmentsPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public interface EntityDataAttachment<T> {
	ResourceLocation id();

	boolean hasData(Entity entity);
	T getData(Entity entity);
	Optional<T> getExistingData(Entity entity);

	@Nullable T setDataUnsynced(Entity entity, T data);
	@Nullable T removeDataUnsynced(Entity entity);

	default @Nullable T setData(Entity entity, T data) {
		T previousValue = setDataUnsynced(entity, data);
		if (entity.level() instanceof ServerLevel serverLevel && shouldSync(previousValue, data)) {
			ESPlatform.INSTANCE.sendToTrackingClients(
				serverLevel, entity,
				new SyncAttachmentsPacket(entity.getId(), this, data)
			);
		}
		return previousValue;
	}

	default @Nullable T removeData(Entity entity) {
		T previousValue = removeDataUnsynced(entity);
		if (entity.level() instanceof ServerLevel serverLevel && shouldSync(previousValue, null)) {
			ESPlatform.INSTANCE.sendToTrackingClients(
				serverLevel, entity,
				new SyncAttachmentsPacket(entity.getId(), this, null)
			);
		}
		return previousValue;
	}

	void writeToNetwork(FriendlyByteBuf buf, @Nullable T value);
	@Nullable T readFromNetwork(FriendlyByteBuf buf);

	boolean shouldSync(@Nullable T v1, @Nullable T v2);
}
