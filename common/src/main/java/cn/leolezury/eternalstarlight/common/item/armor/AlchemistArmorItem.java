package cn.leolezury.eternalstarlight.common.item.armor;

import cn.leolezury.eternalstarlight.common.EternalStarlight;
import cn.leolezury.eternalstarlight.common.registry.ESAttributes;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
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

public class AlchemistArmorItem extends ArmorItem {

	public static final ResourceLocation TEXTURE = EternalStarlight.id("textures/armor/alchemist.png");

	private final Multimap<Attribute, AttributeModifier> extraModifiers;

	public AlchemistArmorItem(Holder<ArmorMaterial> holder, Type type, Properties properties) {
		super(holder.value(), type, properties);

		ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();

		String id = "armor." + type.getName() + "_alchemist";

		builder.put(
			ESAttributes.THROWN_POTION_DISTANCE.get(),
			new AttributeModifier(id + "_throw", 0.25, AttributeModifier.Operation.ADDITION)
		);

		builder.put(
			ESAttributes.ETHER_RESISTANCE.get(),
			new AttributeModifier(id + "_ether", 0.3, AttributeModifier.Operation.ADDITION)
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
				stack.getOrCreateTag().putInt("AccessorySlotCount", 3);
			}
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
		tooltip.add(CommonComponents.EMPTY);
		tooltip.add(Component.translatable("item.modifiers.armor").withStyle(ChatFormatting.GRAY));

		if (getType() == Type.HELMET) {
			tooltip.add(Component.literal(" ")
				.append(Component.translatable("tooltip." + EternalStarlight.ID + ".alchemist_mask"))
				.withStyle(ChatFormatting.GREEN));
		}

		if (getType() == Type.CHESTPLATE) {
			tooltip.add(Component.literal(" ")
				.append(Component.translatable("tooltip." + EternalStarlight.ID + ".alchemist_robe"))
				.withStyle(ChatFormatting.GREEN));
		}

		super.appendHoverText(stack, level, tooltip, flag);
	}
}
