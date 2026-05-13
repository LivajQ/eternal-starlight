package cn.leolezury.eternalstarlight.common.item.armor;

import cn.leolezury.eternalstarlight.common.EternalStarlight;
import cn.leolezury.eternalstarlight.common.registry.ESAttributes;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class UnrealiumArmorItem extends ArmorItem {

	private final Multimap<Attribute, AttributeModifier> extraModifiers;

	public UnrealiumArmorItem(Holder<ArmorMaterial> materialHolder, Type type, Properties properties) {
		super(materialHolder.value(), type, properties);

		ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();

		String id = "armor." + type.getName() + "_unrealium";

		builder.put(
			Attributes.MOVEMENT_SPEED,
			new AttributeModifier(id + "_ms_add", 0.05, AttributeModifier.Operation.ADDITION)
		);

		builder.put(
			Attributes.MOVEMENT_SPEED,
			new AttributeModifier(id + "_ms_mult", 0.10, AttributeModifier.Operation.MULTIPLY_TOTAL)
		);

		builder.put(
			Attributes.KNOCKBACK_RESISTANCE,
			new AttributeModifier(id + "_kb", -0.05, AttributeModifier.Operation.MULTIPLY_TOTAL)
		);

		builder.put(
			ESAttributes.ENEMY_FOLLOW_RANGE_MULTIPLIER.get(),
			new AttributeModifier(id + "_follow", -0.15, AttributeModifier.Operation.ADDITION)
		);

		if (type == Type.HELMET) {
			builder.put(
				ESAttributes.FOG_VISION.get(),
				new AttributeModifier(id + "_fog", 50, AttributeModifier.Operation.ADDITION)
			);
		}

		/* these don't exist

		if (type == Type.CHESTPLATE) {
			builder.put(
				Attributes.EXPLOSION_KNOCKBACK_RESISTANCE,
				new AttributeModifier(id + "_expl_kb", 1, AttributeModifier.Operation.ADDITION)
			);
		}

		if (type == Type.LEGGINGS) {
			builder.put(
				Attributes.GRAVITY,
				new AttributeModifier(id + "_gravity", -0.02, AttributeModifier.Operation.ADDITION)
			);
		}

		if (type == Type.BOOTS) {
			builder.put(
				Attributes.MOVEMENT_EFFICIENCY,
				new AttributeModifier(id + "_eff", 1, AttributeModifier.Operation.ADDITION)
			);
			builder.put(
				Attributes.STEP_HEIGHT,
				new AttributeModifier(id + "_step", 0.5, AttributeModifier.Operation.ADDITION)
			);
		}

		 */

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
				stack.getOrCreateTag().putInt("AccessorySlotCount", 4);
			}
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {

		tooltip.add(CommonComponents.EMPTY);
		tooltip.add(Component.translatable("item.modifiers.armor").withStyle(ChatFormatting.GRAY));

		Type t = getType();

		if (t == Type.HELMET) {
			tooltip.add(Component.literal(" ")
				.append(Component.translatable("tooltip." + EternalStarlight.ID + ".unrealium_helmet"))
				.withStyle(ChatFormatting.DARK_PURPLE));
			tooltip.add(Component.literal(" ")
				.append(Component.translatable("tooltip." + EternalStarlight.ID + ".unrealium_helmet.mute"))
				.withStyle(ChatFormatting.DARK_PURPLE));
		}

		else if (t == Type.CHESTPLATE) {
			tooltip.add(Component.literal(" ")
				.append(Component.translatable("tooltip." + EternalStarlight.ID + ".unrealium_chestplate"))
				.withStyle(ChatFormatting.DARK_PURPLE));
			tooltip.add(Component.literal(" ")
				.append(Component.translatable("tooltip." + EternalStarlight.ID + ".unrealium_chestplate.mute"))
				.withStyle(ChatFormatting.DARK_PURPLE));
		}

		else if (t == Type.LEGGINGS) {
			tooltip.add(Component.literal(" ")
				.append(Component.translatable("tooltip." + EternalStarlight.ID + ".unrealium_leggings.mute"))
				.withStyle(ChatFormatting.DARK_PURPLE));
		}

		else if (t == Type.BOOTS) {
			tooltip.add(Component.literal(" ")
				.append(Component.translatable("tooltip." + EternalStarlight.ID + ".unrealium_boots.mute"))
				.withStyle(ChatFormatting.DARK_PURPLE));
		}

		super.appendHoverText(stack, level, tooltip, flag);
	}
}
