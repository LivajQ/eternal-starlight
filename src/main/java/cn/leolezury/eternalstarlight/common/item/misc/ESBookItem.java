package cn.leolezury.eternalstarlight.common.item.misc;

import cn.leolezury.eternalstarlight.common.EternalStarlight;
import cn.leolezury.eternalstarlight.common.item.component.GuideBook;
import cn.leolezury.eternalstarlight.common.network.OpenBookPacket;
import cn.leolezury.eternalstarlight.common.platform.ESPlatform;
import cn.leolezury.eternalstarlight.common.util.ESBookUtil;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.HashSet;
import java.util.Set;

public class ESBookItem extends Item {
	public ESBookItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);

		if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
			GuideBook book = GuideBook.getGuideBook(stack);
			if (book != null) {
				ESPlatform.INSTANCE.sendToClient(
					serverPlayer,
					new OpenBookPacket(
						book.id(),
						new HashSet<>(ESBookUtil.getUnlockedParts(serverPlayer))
					)
				);
			}
		}
		return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
	}

	@Override
	public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
		if (level.getGameTime() % 20 == 0 && !level.isClientSide && GuideBook.getGuideBook(stack) == null) {
			GuideBook.setGuideBook(
				stack,
				new GuideBook(
					EternalStarlight.id("main"),
					new HashSet<>(Set.of(EternalStarlight.ID))
				)
			);
		}
	}
}
