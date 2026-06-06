package cn.leolezury.eternalstarlight.common.data;

import cn.leolezury.eternalstarlight.common.EternalStarlight;
import cn.leolezury.eternalstarlight.common.enchantment.*;

import cn.leolezury.eternalstarlight.common.platform.registry.RegistrationProvider;
import cn.leolezury.eternalstarlight.common.platform.registry.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

public final class ESEnchantments {
	public static final RegistrationProvider<Enchantment> ENCHANTMENTS = RegistrationProvider.get(Registries.ENCHANTMENT, EternalStarlight.ID);

	public static final RegistryObject<Enchantment, Enchantment> POISONING = ENCHANTMENTS.register("poisoning", PoisoningEnchantment::new);
	public static final RegistryObject<Enchantment, Enchantment> FEARLESS = ENCHANTMENTS.register("fearless", FearlessEnchantment::new);
	public static final RegistryObject<Enchantment, Enchantment> SOUL_SNATCHER = ENCHANTMENTS.register("soul_snatcher", SoulSnatcherEnchantment::new);
	public static final RegistryObject<Enchantment, Enchantment> TRACING = ENCHANTMENTS.register("tracing", TracingEnchantment::new);
	public static final RegistryObject<Enchantment, Enchantment> TEARING = ENCHANTMENTS.register("tearing", TearingEnchantment::new);
	public static final RegistryObject<Enchantment, Enchantment> OVERHEAT = ENCHANTMENTS.register("overheat", OverheatEnchantment::new);
	public static final RegistryObject<Enchantment, Enchantment> GLACIAL_SOWING = ENCHANTMENTS.register("glacial_sowing", GlacialSowingEnchantment::new);
	public static final RegistryObject<Enchantment, Enchantment> FERTILE = ENCHANTMENTS.register("fertile", FertileEnchantment::new);
	public static final RegistryObject<Enchantment, Enchantment> PRECISION = ENCHANTMENTS.register("precision", PrecisionEnchantment::new);
	public static final RegistryObject<Enchantment, Enchantment> HOMING = ENCHANTMENTS.register("homing", HomingEnchantment::new);
	public static final RegistryObject<Enchantment, Enchantment> GATHERING = ENCHANTMENTS.register("gathering", GatheringEnchantment::new);
	public static final RegistryObject<Enchantment, Enchantment> SWIFT_LASH = ENCHANTMENTS.register("swift_lash", SwiftLashEnchantment::new);
	public static final RegistryObject<Enchantment, Enchantment> ABYSSAL_TOUCH = ENCHANTMENTS.register("abyssal_touch", AbyssalTouchEnchantment::new);

	public static void loadClass() {}

	public static float modifyBoomerangCritChance(ItemStack tool, float base) {
		float result = base;
		result += PrecisionEnchantment.getCritChanceBonus(tool);
		return result;
	}

	public static float modifyBoomerangHomingStrength(ItemStack tool, float base) {
		float result = base;
		result += HomingEnchantment.getHomingBonus(tool);
		return result;
	}

	public static float modifyBoomerangPickupRadius(ItemStack tool, float base) {
		float result = base;
		result += GatheringEnchantment.getPickupRadiusBonus(tool);
		return result;
	}

}
