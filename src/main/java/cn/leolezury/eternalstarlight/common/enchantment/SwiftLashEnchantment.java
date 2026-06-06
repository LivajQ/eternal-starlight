package cn.leolezury.eternalstarlight.common.enchantment;

import cn.leolezury.eternalstarlight.common.util.ESTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

import java.util.UUID;

public class SwiftLashEnchantment extends Enchantment {

	private static final UUID SWIFT_LASH_UUID = UUID.fromString("d3f3a1b2-9c4e-4c7f-8b1a-123456789abc");

	public SwiftLashEnchantment() {
		super(Rarity.COMMON, EnchantmentCategory.WEAPON, new EquipmentSlot[]{ EquipmentSlot.MAINHAND });
	}

	@Override
	public int getMinCost(int level) {
		return 1 + (level - 1) * 11;
	}

	@Override
	public int getMaxCost(int level) {
		return 21 + (level - 1) * 11;
	}

	@Override
	public int getMaxLevel() {
		return 5;
	}

	@Override
	public boolean canEnchant(ItemStack stack) {
		return stack.is(ESTags.Items.WHIP_ENCHANTABLE)
			|| super.canEnchant(stack);
	}

	//TODO this will likely have to be added through ItemStack
	/*
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack, int level) {
		if (slot != EquipmentSlot.MAINHAND) {
			return ImmutableMultimap.of();
		}

		float amount = 0.2F * level;

		return ImmutableMultimap.of(
			Attributes.ATTACK_SPEED,
			new AttributeModifier(
				SWIFT_LASH_UUID,
				"Swift Lash attack speed",
				amount,
				AttributeModifier.Operation.ADDITION
			)
		);
	}
	 */
}
