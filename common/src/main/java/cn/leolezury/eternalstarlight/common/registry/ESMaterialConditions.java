package cn.leolezury.eternalstarlight.common.registry;

import cn.leolezury.eternalstarlight.common.EternalStarlight;
import cn.leolezury.eternalstarlight.common.platform.registry.RegistrationProvider;
import cn.leolezury.eternalstarlight.common.platform.registry.RegistryObject;
import cn.leolezury.eternalstarlight.common.world.gen.surface.OnSurfaceCondition;
import com.mojang.serialization.Codec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.SurfaceRules;

public class ESMaterialConditions {
	public static final RegistrationProvider<Codec<? extends SurfaceRules.ConditionSource>> MATERIAL_CONDITIONS =
		RegistrationProvider.get(Registries.MATERIAL_CONDITION, EternalStarlight.ID);

	public static final RegistryObject<Codec<? extends SurfaceRules.ConditionSource>, Codec<OnSurfaceCondition>> ON_SURFACE =
		MATERIAL_CONDITIONS.register("on_surface", OnSurfaceCondition.CODEC::codec);

	public static void loadClass() {}
}
