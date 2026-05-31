package cn.leolezury.eternalstarlight.forge.datagen.provider.loot;

import cn.leolezury.eternalstarlight.common.data.ESEnchantments;
import cn.leolezury.eternalstarlight.common.data.ESLootTables;
import cn.leolezury.eternalstarlight.common.registry.ESItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.minecraft.world.level.storage.loot.functions.EnchantRandomlyFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SetPotionFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.Objects;
import java.util.function.BiConsumer;

public class ESChestLootSubProvider implements LootTableSubProvider {

	@Override
	public void generate(BiConsumer<ResourceLocation, LootTable.Builder> consumer) {

		consumer.accept(ESLootTables.CHEST_MUSIC_DISCS,
			LootTable.lootTable()
				.withPool(LootPool.lootPool()
					.add(LootItem.lootTableItem(ESItems.MUSIC_DISC_WHISPER_OF_THE_STARS.get()))
					.add(LootItem.lootTableItem(ESItems.MUSIC_DISC_DUSK_O_EREYESTERDAY.get()))
					.add(LootItem.lootTableItem(ESItems.MUSIC_DISC_TRANQUILITY.get()))
					.add(LootItem.lootTableItem(ESItems.MUSIC_DISC_NEST.get()))
					.add(LootItem.lootTableItem(ESItems.MUSIC_DISC_POSTERITY.get()))
					.add(LootItem.lootTableItem(ESItems.MUSIC_DISC_THE_THORNY_REIGN.get()))
					.add(LootItem.lootTableItem(ESItems.MUSIC_DISC_PROFUNDITY.get()))
					.add(LootItem.lootTableItem(ESItems.MUSIC_DISC_WAILING_WELL.get()))
					.add(LootItem.lootTableItem(ESItems.MUSIC_DISC_STARS_SHINING_UPON_THE_SEA.get()))
					.add(LootItem.lootTableItem(ESItems.MUSIC_DISC_TRANQUILITY_II.get()))
					.add(LootItem.lootTableItem(ESItems.MUSIC_DISC_ATLANTIS.get()))));

		consumer.accept(ESLootTables.CHEST_ACCESSORIES,
			LootTable.lootTable()
				.withPool(LootPool.lootPool()
					.add(LootItem.lootTableItem(ESItems.WARHAMMER_PENDANT.get()))
					.add(LootItem.lootTableItem(ESItems.FUNGUS_AMULET.get()))));

		consumer.accept(ESLootTables.CHEST_GOLEM_FORGE,
			LootTable.lootTable()
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(5))
					.add(LootItem.lootTableItem(Items.STONE).apply(SetItemCountFunction.setCount(UniformGenerator.between(5, 9))).setWeight(25))
					.add(LootItem.lootTableItem(Items.COAL).apply(SetItemCountFunction.setCount(UniformGenerator.between(5, 9))).setWeight(50))
					.add(LootItem.lootTableItem(Items.CHARCOAL).apply(SetItemCountFunction.setCount(UniformGenerator.between(5, 9))).setWeight(50))
					.add(LootItem.lootTableItem(Items.REDSTONE).apply(SetItemCountFunction.setCount(UniformGenerator.between(4, 8))).setWeight(40))
					.add(LootItem.lootTableItem(Items.GUNPOWDER).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))).setWeight(20))
					.add(LootItem.lootTableItem(ESItems.GLACITE_ARROW.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(15, 30))).setWeight(50))
					.add(LootItem.lootTableItem(ESItems.SALTPETER_POWDER.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(10, 15))).setWeight(25))
					.add(LootItem.lootTableItem(ESItems.DEEPSILVER_INGOT.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 5))).setWeight(25)))
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(3))
					.add(LootItem.lootTableItem(Items.LAVA_BUCKET).setWeight(50))
					.add(LootItem.lootTableItem(Items.WATER_BUCKET).setWeight(50))
					.add(LootItem.lootTableItem(Items.BOOK).apply(new EnchantRandomlyFunction.Builder()
						.withEnchantment(Objects.requireNonNull(BuiltInRegistries.ENCHANTMENT.get(ESEnchantments.FEARLESS.location())))
						.withEnchantment(Objects.requireNonNull(BuiltInRegistries.ENCHANTMENT.get(ESEnchantments.PRECISION.location())))
						.withEnchantment(Objects.requireNonNull(BuiltInRegistries.ENCHANTMENT.get(ESEnchantments.HOMING.location())))
						.withEnchantment(Objects.requireNonNull(BuiltInRegistries.ENCHANTMENT.get(ESEnchantments.GATHERING.location())))).setWeight(25))
					.add(LootItem.lootTableItem(Items.NETHERRACK).setWeight(10))
					.add(LootItem.lootTableItem(ESItems.COOKED_RATLIN_MEAT.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(5, 15))).setWeight(25))
					.add(LootItem.lootTableItem(ESItems.COOKED_AURORA_DEER_STEAK.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(5, 15))).setWeight(12))
					.add(LootItem.lootTableItem(ESItems.LUNAR_BERRIES.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 8))).setWeight(50))
					.add(LootItem.lootTableItem(ESItems.FROZEN_TUBE.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 8))).setWeight(30))
					.add(LootItem.lootTableItem(ESItems.AMARAMBER_INGOT.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(5, 10))).setWeight(30))
					.add(LootItem.lootTableItem(ESItems.DEEPSILVER_NUGGET.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(10, 15))).setWeight(45))
					.add(LootItem.lootTableItem(ESItems.GLACITE_SHARD.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 5))).setWeight(20)))
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(3))
					.add(LootItem.lootTableItem(Items.BOOK).apply(new EnchantRandomlyFunction.Builder()
						.withEnchantment(Objects.requireNonNull(BuiltInRegistries.ENCHANTMENT.get(ESEnchantments.FEARLESS.location())))
						.withEnchantment(Objects.requireNonNull(BuiltInRegistries.ENCHANTMENT.get(ESEnchantments.PRECISION.location())))
						.withEnchantment(Objects.requireNonNull(BuiltInRegistries.ENCHANTMENT.get(ESEnchantments.HOMING.location())))
						.withEnchantment(Objects.requireNonNull(BuiltInRegistries.ENCHANTMENT.get(ESEnchantments.GATHERING.location())))).setWeight(75))
					.add(LootItem.lootTableItem(Items.GOLDEN_CARROT).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 5))).setWeight(25))
					.add(LootItem.lootTableItem(Items.GOLDEN_APPLE).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 5))).setWeight(10))
					.add(LootItem.lootTableItem(Items.ENCHANTED_GOLDEN_APPLE).setWeight(2))
					.add(LootItem.lootTableItem(Items.POTION).apply(SetPotionFunction.setPotion(Potions.STRONG_HEALING)).setWeight(15))
					.add(LootItem.lootTableItem(Items.POTION).apply(SetPotionFunction.setPotion(Potions.LONG_NIGHT_VISION)).setWeight(15))
					.add(LootItem.lootTableItem(ESItems.OXIDIZED_GOLEM_STEEL_INGOT.get()).setWeight(5))
					.add(LootItem.lootTableItem(ESItems.STARFIRE_UPGRADE_SMITHING_TEMPLATE.get()).setWeight(8))
					.add(LootItem.lootTableItem(ESItems.FLOWGLAZE_UPGRADE_SMITHING_TEMPLATE.get()).setWeight(8))
					.add(LootTableReference.lootTableReference(ESLootTables.CHEST_MUSIC_DISCS).setWeight(8))
					.add(LootTableReference.lootTableReference(ESLootTables.CHEST_ACCESSORIES).setWeight(10))));

		consumer.accept(ESLootTables.CHEST_CURSED_GARDEN,
			LootTable.lootTable()
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(4))
					.add(LootItem.lootTableItem(Items.MILK_BUCKET).setWeight(10))
					.add(LootItem.lootTableItem(ESItems.AMARAMBER_ARROW.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(15, 30))).setWeight(40))
					.add(LootItem.lootTableItem(ESItems.GLOWING_LUNAR_GRASS.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))).setWeight(25))
					.add(LootItem.lootTableItem(ESItems.GLOWING_PARASOL_GRASS.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))).setWeight(25))
					.add(LootItem.lootTableItem(ESItems.GLOWING_CRESCENT_GRASS.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))).setWeight(25)))
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(3))
					.add(LootItem.lootTableItem(Items.BOOK).apply(new EnchantRandomlyFunction.Builder()
						.withEnchantment(Objects.requireNonNull(BuiltInRegistries.ENCHANTMENT.get(ESEnchantments.FEARLESS.location())))
						.withEnchantment(Objects.requireNonNull(BuiltInRegistries.ENCHANTMENT.get(ESEnchantments.POISONING.location())))
						.withEnchantment(Objects.requireNonNull(BuiltInRegistries.ENCHANTMENT.get(ESEnchantments.SOUL_SNATCHER.location())))
						.withEnchantment(Objects.requireNonNull(BuiltInRegistries.ENCHANTMENT.get(ESEnchantments.TRACING.location())))).setWeight(40))
					.add(LootItem.lootTableItem(ESItems.LUNAR_BERRIES.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 8))).setWeight(60))
					.add(LootItem.lootTableItem(ESItems.COOKED_RATLIN_MEAT.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(5, 15))).setWeight(35))
					.add(LootItem.lootTableItem(ESItems.COOKED_AURORA_DEER_STEAK.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(5, 15))).setWeight(15))
					.add(LootItem.lootTableItem(ESItems.SALTPETER_POWDER.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(5, 15))).setWeight(20))
					.add(LootItem.lootTableItem(ESItems.SALTPETER_MATCHBOX.get()).setWeight(10)))
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(3))
					.add(LootItem.lootTableItem(Items.GOLDEN_APPLE).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 7))).setWeight(15))
					.add(LootItem.lootTableItem(Items.ENCHANTED_GOLDEN_APPLE).setWeight(2))
					.add(LootItem.lootTableItem(Items.POTION).apply(SetPotionFunction.setPotion(Potions.STRONG_HEALING)).setWeight(25))
					.add(LootItem.lootTableItem(ESItems.AMARAMBER_INGOT.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 8))).setWeight(30))
					.add(LootItem.lootTableItem(ESItems.DEEPSILVER_NUGGET.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(5, 20))).setWeight(45))
					.add(LootItem.lootTableItem(ESItems.THERMAL_SPRINGSTONE_INGOT.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 5))).setWeight(25))
					.add(LootItem.lootTableItem(ESItems.STARFIRE_UPGRADE_SMITHING_TEMPLATE.get()).setWeight(10))
					.add(LootItem.lootTableItem(ESItems.FLOWGLAZE_UPGRADE_SMITHING_TEMPLATE.get()).setWeight(10))
					.add(LootTableReference.lootTableReference(ESLootTables.CHEST_MUSIC_DISCS).setWeight(8))
					.add(LootTableReference.lootTableReference(ESLootTables.CHEST_ACCESSORIES).setWeight(10))));

		consumer.accept(ESLootTables.CHEST_DUSK_LOCKBOX,
			LootTable.lootTable()
				.withPool(LootPool.lootPool()
					.add(LootItem.lootTableItem(ESItems.GRAVITY_PICKAXE.get()))));
	}
}
