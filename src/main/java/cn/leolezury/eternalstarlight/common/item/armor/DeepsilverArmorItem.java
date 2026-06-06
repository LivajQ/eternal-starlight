package cn.leolezury.eternalstarlight.common.item.armor;

import cn.leolezury.eternalstarlight.common.EternalStarlight;
import cn.leolezury.eternalstarlight.common.item.interfaces.TickableArmor;
import cn.leolezury.eternalstarlight.common.util.ESTags;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class DeepsilverArmorItem extends ArmorItem implements TickableArmor {

	private final Multimap<Attribute, AttributeModifier> extraModifiers;

	public DeepsilverArmorItem(ArmorMaterial material, Type type, Item.Properties properties) {
		super(material, type, properties);

		ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();

		String id = "armor." + type.getName() + "_deepsilver";

		builder.put(
			Attributes.ATTACK_SPEED,
			new AttributeModifier(id + "_as", 0.05, AttributeModifier.Operation.ADDITION)
		);

		builder.put(
			Attributes.MOVEMENT_SPEED,
			new AttributeModifier(id + "_ms", 0.01, AttributeModifier.Operation.ADDITION)
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
	public void tick(Level level, LivingEntity livingEntity, ItemStack armor) {
		boolean fullSet = true;

		for (ItemStack stack : livingEntity.getArmorSlots()) {
			if (!(stack.getItem() instanceof DeepsilverArmorItem)) {
				fullSet = false;
				break;
			}
		}

		if (fullSet) {
			level.registryAccess()
				.registryOrThrow(Registries.MOB_EFFECT)
				.getTagOrEmpty(ESTags.MobEffects.DEEPSILVER_ARMOR_CAN_REMOVE)
				.forEach(effect -> {
					if (livingEntity.hasEffect(effect.value())) {
						livingEntity.removeEffect(effect.value());
					}
				});
		}
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

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
		tooltip.add(CommonComponents.EMPTY);
		tooltip.add(Component.translatable("tooltip." + EternalStarlight.ID + ".full_set").withStyle(ChatFormatting.GRAY));
		tooltip.add(Component.literal(" ")
			.append(Component.translatable("tooltip." + EternalStarlight.ID + ".deepsilver_armor"))
			.withStyle(ChatFormatting.YELLOW));

		super.appendHoverText(stack, level, tooltip, flag);
	}
}
