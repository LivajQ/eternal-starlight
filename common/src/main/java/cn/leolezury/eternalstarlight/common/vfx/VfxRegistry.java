package cn.leolezury.eternalstarlight.common.vfx;

import cn.leolezury.eternalstarlight.common.EternalStarlight;
import net.minecraft.resources.ResourceLocation;

import java.util.*;

public class VfxRegistry {

	private static final Map<ResourceLocation, SyncedVfxType> EFFECTS = new HashMap<>();
	private static final List<SyncedVfxType> EFFECT_LIST = new ArrayList<>();
	private static final Map<SyncedVfxType, Integer> EFFECT_TO_ID = new HashMap<>();

	public static final SyncedVfxType SCREEN_SHAKE =
		register("screen_shake", new ScreenShakeVfx());

	public static final SyncedVfxType MANA_CRYSTAL_PARTICLE =
		register("mana_crystal_particle", new ManaCrystalParticleVfx());

	private static SyncedVfxType register(String location, SyncedVfxType type) {
		return register(EternalStarlight.id(location), type);
	}

	public static SyncedVfxType register(ResourceLocation location, SyncedVfxType type) {
		EFFECTS.put(location, type);

		int id = EFFECT_LIST.size();
		EFFECT_LIST.add(type);
		EFFECT_TO_ID.put(type, id);

		return type;
	}

	public static Optional<SyncedVfxType> get(ResourceLocation location) {
		return Optional.ofNullable(EFFECTS.get(location));
	}

	public static ResourceLocation getKey(SyncedVfxType type) {
		for (Map.Entry<ResourceLocation, SyncedVfxType> entry : EFFECTS.entrySet()) {
			if (entry.getValue() == type) {
				return entry.getKey();
			}
		}
		return new ResourceLocation("minecraft", "unregistered");
	}

	public static int getId(SyncedVfxType type) {
		return EFFECT_TO_ID.getOrDefault(type, -1);
	}

	public static SyncedVfxType byId(int id) {
		if (id < 0 || id >= EFFECT_LIST.size()) return null;
		return EFFECT_LIST.get(id);
	}
}
