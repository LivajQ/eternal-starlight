package cn.leolezury.eternalstarlight.common.world.saved;

import cn.leolezury.eternalstarlight.common.EternalStarlight;
import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.*;

public class BookProgressions extends SavedData {
	private static final String TAG_PROGRESSIONS = "progressions";

	private final Map<UUID, Set<ResourceLocation>> progressions = new HashMap<>();

	public Map<UUID, Set<ResourceLocation>> getProgressions() {
		return progressions;
	}

	public static BookProgressions get(ServerLevel level) {
		return level.getDataStorage().computeIfAbsent(
			tag -> load(level, tag),
			BookProgressions::new,
			"book_progressions"
		);
	}

	public static BookProgressions load(ServerLevel serverLevel, CompoundTag tag) {
		BookProgressions progression = new BookProgressions();
		if (tag.contains(TAG_PROGRESSIONS)) {
			ProgressionInstance.LIST_CODEC.parse(NbtOps.INSTANCE, tag.get(TAG_PROGRESSIONS))
				.resultOrPartial(s -> EternalStarlight.LOGGER.warn("Failed to parse Book Progressions: {}", s))
				.ifPresent(list -> list.forEach(instance ->
					progression.getProgressions().put(instance.id(), Sets.newHashSet(instance.unlocked()))
				));
		}
		return progression;
	}

	@Override
	public CompoundTag save(CompoundTag tag) {
		List<ProgressionInstance> list = progressions.entrySet().stream()
			.map(e -> new ProgressionInstance(e.getKey(), List.copyOf(e.getValue())))
			.toList();

		tag.put(
			TAG_PROGRESSIONS,
			ProgressionInstance.LIST_CODEC
				.encodeStart(NbtOps.INSTANCE, list)
				.getOrThrow(false, msg -> {
					throw new IllegalStateException("Failed to encode book progressions: " + msg);
				})
		);
		return tag;
	}

	private record ProgressionInstance(UUID id, List<ResourceLocation> unlocked) {
		public static final Codec<ProgressionInstance> CODEC =
			RecordCodecBuilder.create(instance -> instance.group(
				UUIDUtil.CODEC.fieldOf("id").forGetter(ProgressionInstance::id),
				ResourceLocation.CODEC.listOf().fieldOf("unlocked").forGetter(ProgressionInstance::unlocked)
			).apply(instance, ProgressionInstance::new));

		public static final Codec<List<ProgressionInstance>> LIST_CODEC = CODEC.listOf();
	}
}
