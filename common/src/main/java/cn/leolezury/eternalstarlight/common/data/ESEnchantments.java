package cn.leolezury.eternalstarlight.common.data;

import cn.leolezury.eternalstarlight.common.EternalStarlight;
import cn.leolezury.eternalstarlight.common.enchantment.*;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.enchantment.Enchantment;

public final class ESEnchantments {
	public static final ResourceKey<Enchantment> POISONING      = create("poisoning");
	public static final ResourceKey<Enchantment> FEARLESS       = create("fearless");
	public static final ResourceKey<Enchantment> SOUL_SNATCHER  = create("soul_snatcher");
	public static final ResourceKey<Enchantment> TRACING        = create("tracing");
	public static final ResourceKey<Enchantment> TEARING        = create("tearing");
	public static final ResourceKey<Enchantment> OVERHEAT       = create("overheat");
	public static final ResourceKey<Enchantment> GLACIAL_SOWING = create("glacial_sowing");
	public static final ResourceKey<Enchantment> FERTILE        = create("fertile");
	public static final ResourceKey<Enchantment> PRECISION      = create("precision");
	public static final ResourceKey<Enchantment> HOMING         = create("homing");
	public static final ResourceKey<Enchantment> GATHERING      = create("gathering");
	public static final ResourceKey<Enchantment> SWIFT_LASH     = create("swift_lash");
	public static final ResourceKey<Enchantment> ABYSSAL_TOUCH  = create("abyssal_touch");

	private ESEnchantments() {}

	public static ResourceKey<Enchantment> create(String name) {
		return ResourceKey.create(Registries.ENCHANTMENT, EternalStarlight.id(name));
	}

	public static void register() {
		Registry.register(BuiltInRegistries.ENCHANTMENT, POISONING.location(),      new PoisoningEnchantment());
		Registry.register(BuiltInRegistries.ENCHANTMENT, FEARLESS.location(),       new FearlessEnchantment());
		Registry.register(BuiltInRegistries.ENCHANTMENT, SOUL_SNATCHER.location(),  new SoulSnatcherEnchantment());
		Registry.register(BuiltInRegistries.ENCHANTMENT, TRACING.location(),        new TracingEnchantment());
		Registry.register(BuiltInRegistries.ENCHANTMENT, TEARING.location(),        new TearingEnchantment());
		Registry.register(BuiltInRegistries.ENCHANTMENT, OVERHEAT.location(),       new OverheatEnchantment());
		Registry.register(BuiltInRegistries.ENCHANTMENT, GLACIAL_SOWING.location(), new GlacialSowingEnchantment());
		Registry.register(BuiltInRegistries.ENCHANTMENT, FERTILE.location(),        new FertileEnchantment());
		Registry.register(BuiltInRegistries.ENCHANTMENT, PRECISION.location(),      new PrecisionEnchantment());
		Registry.register(BuiltInRegistries.ENCHANTMENT, HOMING.location(),         new HomingEnchantment());
		Registry.register(BuiltInRegistries.ENCHANTMENT, GATHERING.location(),      new GatheringEnchantment());
		Registry.register(BuiltInRegistries.ENCHANTMENT, SWIFT_LASH.location(),     new SwiftLashEnchantment());
		Registry.register(BuiltInRegistries.ENCHANTMENT, ABYSSAL_TOUCH.location(),  new AbyssalTouchEnchantment());
	}
}
