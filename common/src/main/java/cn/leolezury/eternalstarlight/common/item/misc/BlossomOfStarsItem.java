package cn.leolezury.eternalstarlight.common.item.misc;

import cn.leolezury.eternalstarlight.common.EternalStarlight;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class BlossomOfStarsItem extends Item {
	public BlossomOfStarsItem(Properties properties) {
		super(properties);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
		tooltip.add(
			Component.translatable("tooltip." + EternalStarlight.ID + ".blossom_of_stars")
				.withStyle(style -> style.withColor(0x5187c4))
				.withStyle(ChatFormatting.ITALIC)
		);

		super.appendHoverText(stack, level, tooltip, flag);
	}

}
