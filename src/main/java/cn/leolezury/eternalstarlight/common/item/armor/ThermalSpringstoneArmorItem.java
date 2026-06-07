package cn.leolezury.eternalstarlight.common.item.armor;

import cn.leolezury.eternalstarlight.common.EternalStarlight;
import cn.leolezury.eternalstarlight.common.client.model.armor.ThermalSpringStoneArmorModel;
import cn.leolezury.eternalstarlight.common.registry.ESAttributes;
import cn.leolezury.eternalstarlight.common.registry.ESItems;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

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
	public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
		return getTexture(slot).toString();
	}

	@Override
	public void initializeClient(Consumer<IClientItemExtensions> consumer) {
		consumer.accept(new IClientItemExtensions() {
			private ThermalSpringStoneArmorModel<LivingEntity> innerModel;
			private ThermalSpringStoneArmorModel<LivingEntity> outerModel;

			@Override
			public @NotNull HumanoidModel<?> getHumanoidArmorModel(
				LivingEntity livingEntity,
				ItemStack stack,
				EquipmentSlot slot,
				HumanoidModel<?> original
			) {
				if (innerModel == null || outerModel == null) {
					var models = Minecraft.getInstance().getEntityModels();
					innerModel = new ThermalSpringStoneArmorModel<>(models.bakeLayer(ThermalSpringStoneArmorModel.INNER_LOCATION));
					outerModel = new ThermalSpringStoneArmorModel<>(models.bakeLayer(ThermalSpringStoneArmorModel.OUTER_LOCATION));
				}

				if (stack.is(ESItems.THERMAL_SPRINGSTONE_LEGGINGS.get())) {
					return innerModel;
				}

				if (stack.is(ESItems.THERMAL_SPRINGSTONE_HELMET.get())
					|| stack.is(ESItems.THERMAL_SPRINGSTONE_CHESTPLATE.get())
					|| stack.is(ESItems.THERMAL_SPRINGSTONE_BOOTS.get())) {
					return outerModel;
				}

				return original;
			}
		});
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
