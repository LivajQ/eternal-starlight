package cn.leolezury.eternalstarlight.common.registry;

import cn.leolezury.eternalstarlight.common.util.ESConventionalTags;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Supplier;

public class ESArmorMaterials {

	public static final ArmorMaterial AETHERSENT = new ESArmorMaterial(
		35,
		new int[]{3, 6, 7, 3},
		35,
		ESSoundEvents.ARMOR_EQUIP_AETHERSENT,
		() -> Ingredient.of(ESConventionalTags.Items.INGOTS_AETHERSENT),
		0F,
		0F
	);

	public static final ArmorMaterial THERMAL_SPRINGSTONE = new ESArmorMaterial(
		15,
		new int[]{2, 5, 6, 3},
		15,
		ESSoundEvents.ARMOR_EQUIP_THERMAL_SPRINGSTONE,
		() -> Ingredient.of(ESConventionalTags.Items.INGOTS_THERMAL_SPRINGSTONE),
		0.5F,
		0F
	);

	public static final ArmorMaterial GLACITE = new ESArmorMaterial(
		9,
		new int[]{3, 5, 6, 3},
		9,
		ESSoundEvents.ARMOR_EQUIP_GLACITE,
		() -> Ingredient.of(ESConventionalTags.Items.GEMS_GLACITE),
		0.5F,
		0F
	);

	public static final ArmorMaterial STARLIT_DIAMOND = new ESArmorMaterial(
		22,
		new int[]{3, 6, 8, 3},
		22,
		ESSoundEvents.ARMOR_EQUIP_STARLIT_DIAMOND,
		() -> Ingredient.of(ESConventionalTags.Items.GEMS_STARLIT_DIAMOND),
		3F,
		0.1F
	);

	public static final ArmorMaterial DEEPSILVER = new ESArmorMaterial(
		25,
		new int[]{2, 5, 6, 2},
		25,
		ESSoundEvents.ARMOR_EQUIP_DEEPSILVER,
		() -> Ingredient.of(ESConventionalTags.Items.INGOTS_DEEPSILVER),
		0.5F,
		0.2F
	);

	public static final ArmorMaterial UNREALIUM = new ESArmorMaterial(
		25,
		new int[]{3, 6, 8, 3},
		25,
		ESSoundEvents.ARMOR_EQUIP_UNREALIUM,
		() -> Ingredient.of(ESConventionalTags.Items.INGOTS_UNREALIUM),
		0.5F,
		0F
	);

	public static final ArmorMaterial AMARAMBER = new ESArmorMaterial(
		9,
		new int[]{2, 5, 5, 2},
		9,
		ESSoundEvents.ARMOR_EQUIP_AMARAMBER,
		() -> Ingredient.of(ESConventionalTags.Items.INGOTS_AMARAMBER),
		0F,
		0F
	);

	public static final ArmorMaterial ALCHEMIST = new ESArmorMaterial(
		15,
		new int[]{2, 5, 6, 2},
		15,
		ESSoundEvents.ARMOR_EQUIP_ALCHEMIST,
		() -> Ingredient.of(ESConventionalTags.Items.GEMS_THIOQUARTZ),
		0F,
		0F
	);

	public static final ArmorMaterial AIR_SAC = new ESArmorMaterial(
		9,
		new int[]{1, 4, 4, 1},
		9,
		() -> SoundEvents.ARMOR_EQUIP_LEATHER,
		() -> Ingredient.of(ESItems.ROOKFISH_AIR_SAC.get()),
		0F,
		0F
	);

	public static void loadClass() {
	}

	public record ESArmorMaterial(
		int durabilityMultiplier,
		int[] slotProtections,
		int enchantmentValue,
		Supplier<SoundEvent> equipSoundSupplier,
		Supplier<Ingredient> repairIngredient,
		float toughness,
		float knockbackResistance
	) implements ArmorMaterial {

		private static final int[] BASE_DURABILITY = {13, 15, 16, 11};

		@Override
		public int getDurabilityForType(ArmorItem.Type type) {
			return BASE_DURABILITY[type.getSlot().getIndex()] * durabilityMultiplier;
		}

		@Override
		public int getDefenseForType(ArmorItem.Type type) {
			return slotProtections[type.getSlot().getIndex()];
		}

		@Override
		public int getEnchantmentValue() {
			return enchantmentValue;
		}

		@Override
		public SoundEvent getEquipSound() {
			return equipSoundSupplier.get();
		}

		@Override
		public Ingredient getRepairIngredient() {
			return repairIngredient.get();
		}

		@Override
		public String getName() {
			return "eternalstarlight:" + this.toString().toLowerCase();
		}

		@Override
		public float getToughness() {
			return toughness;
		}

		@Override
		public float getKnockbackResistance() {
			return knockbackResistance;
		}
	}

}
