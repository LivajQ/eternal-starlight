package cn.leolezury.eternalstarlight.common.item.armor;

import cn.leolezury.eternalstarlight.common.registry.ESAttributes;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class AethersentArmorItem extends ArmorItem {

	private final Multimap<Attribute, AttributeModifier> extraModifiers;

	public AethersentArmorItem(Holder<ArmorMaterial> material, Type type, Properties properties) {
		super(material.value(), type, properties);

		ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();

		String id = "armor." + type.getName() + "_meteor_counterattack";

		builder.put(
			ESAttributes.METEOR_COUNTERATTACK_CHANCE.get(),
			new AttributeModifier(id, 0.25, AttributeModifier.Operation.ADDITION)
		);

		this.extraModifiers = builder.build();
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
		Multimap<Attribute, AttributeModifier> base = super.getDefaultAttributeModifiers(slot);

		if (slot == this.type.getSlot()) {
			ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
			builder.putAll(base);
			builder.putAll(extraModifiers);
			return builder.build();
		}

		return base;
	}

	@Override
	public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
		super.inventoryTick(stack, level, entity, slot, selected);

		if (!level.isClientSide && level.getGameTime() % 20 == 0) {
			if (this.getType() == ArmorItem.Type.CHESTPLATE) {
				stack.getOrCreateTag().putInt("AccessorySlotCount", 2);
			}
		}
	}
}