package cn.leolezury.eternalstarlight.common.item.armor;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraftforge.common.ForgeMod;

public class AirSacArmorItem extends ArmorItem {

	private final Multimap<Attribute, AttributeModifier> extraModifiers;

	public AirSacArmorItem(ArmorMaterial material, Type type, Properties properties) {
		super(material, type, properties);

		ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();

		String id = "armor." + type.getName() + "_water_movement";

		builder.put(
			ForgeMod.SWIM_SPEED.get(),
			new AttributeModifier(id, 0.9, AttributeModifier.Operation.ADDITION)
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
}
