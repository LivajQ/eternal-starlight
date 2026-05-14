package cn.leolezury.eternalstarlight.common.item.armor;

import cn.leolezury.eternalstarlight.common.EternalStarlight;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class GlaciteArmorItem extends ArmorItem {

	public GlaciteArmorItem(ArmorMaterial material, Type type, Properties properties) {
		super(material, type, properties);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
		tooltip.add(CommonComponents.EMPTY);
		tooltip.add(Component.translatable("item.modifiers.armor").withStyle(ChatFormatting.GRAY));
		tooltip.add(Component.literal(" ")
			.append(Component.translatable("tooltip." + EternalStarlight.ID + ".glacite_armor"))
			.withStyle(ChatFormatting.AQUA));

		super.appendHoverText(stack, level, tooltip, flag);
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
