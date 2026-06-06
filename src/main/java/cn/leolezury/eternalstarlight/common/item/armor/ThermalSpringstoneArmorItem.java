package cn.leolezury.eternalstarlight.common.item.armor;

import cn.leolezury.eternalstarlight.common.EternalStarlight;
import cn.leolezury.eternalstarlight.common.registry.ESAttributes;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class ThermalSpringstoneArmorItem extends ArmorItem {

	private final Multimap<Attribute, AttributeModifier> extraModifiers;

	public ThermalSpringstoneArmorItem(ArmorMaterial material, Type type, Properties properties) {
		super(material, type, properties);

		ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();

		String id = "armor." + type.getName() + "_fire_resistance";

		builder.put(
			ESAttributes.FIRE_RESISTANCE.get(),
			new AttributeModifier(id, 0.24, AttributeModifier.Operation.ADDITION)
		);

		this.extraModifiers = builder.build();
	}

	public static ResourceLocation getTexture(EquipmentSlot slot) {
		return EternalStarlight.id(
			"textures/armor/thermal_springstone_layer_" +
				((slot == EquipmentSlot.LEGS) ? "2.png" : "1.png")
		);
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
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
		list.add(CommonComponents.EMPTY);
		list.add(Component.translatable("item.modifiers.armor").withStyle(ChatFormatting.GRAY));
		list.add(
			Component.literal(" ")
				.append(Component.translatable("tooltip." + EternalStarlight.ID + ".thermal_springstone_armor"))
				.withStyle(ChatFormatting.GOLD)
		);

		super.appendHoverText(stack, level, list, flag);
	}

}
