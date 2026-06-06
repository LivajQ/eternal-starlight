package cn.leolezury.eternalstarlight.common.item.misc;

import cn.leolezury.eternalstarlight.common.item.component.LargeItemStackList;
import cn.leolezury.eternalstarlight.common.item.tooltip.GalacticQuiverTooltipComponent;
import cn.leolezury.eternalstarlight.common.registry.ESItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class GalacticQuiverItem extends Item {

	private static final int MAX_ARROWS = 512;
	private static final String TAG_ARROWS = "Arrows";

	public GalacticQuiverItem(Properties properties) {
		super(properties);
	}

	public static LargeItemStackList getArrows(ItemStack stack) {
		CompoundTag tag = stack.getOrCreateTag();
		ListTag list = tag.getList(TAG_ARROWS, Tag.TAG_COMPOUND);

		List<LargeItemStackList.LargeItemStack> result = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			CompoundTag entry = list.getCompound(i);
			result.add(LargeItemStackList.LargeItemStack.fromTag(entry));
		}

		return new LargeItemStackList(result);
	}

	public static void setArrows(ItemStack stack, LargeItemStackList arrows) {
		ListTag list = new ListTag();
		for (LargeItemStackList.LargeItemStack arrow : arrows) {
			if (!arrow.isEmpty()) {
				list.add(arrow.toTag());
			}
		}
		stack.getOrCreateTag().put(TAG_ARROWS, list);
	}

	public static boolean hasArrows(ItemStack itemStack) {
		return getArrows(itemStack).stream().anyMatch(a -> !a.isEmpty());
	}

	// returns whether the entire stack is consumed
	public static boolean addArrowToInventory(Inventory inventory, ItemStack stack) {
		if (!stack.is(ItemTags.ARROWS) || stack.isEmpty()) return false;

		for (int i = 0; i < inventory.getContainerSize(); i++) {
			ItemStack invItem = inventory.getItem(i);

			if (invItem.is(ESItems.GALACTIC_QUIVER.get())) {

				LargeItemStackList list = getArrows(invItem);
				List<LargeItemStackList.LargeItemStack> arrows = new ArrayList<>(list);

				int total = arrows.stream().mapToInt(LargeItemStackList.LargeItemStack::getCount).sum();

				if (total < MAX_ARROWS) {
					ItemStack taken = stack.split(MAX_ARROWS - total);

					boolean merged = false;
					for (LargeItemStackList.LargeItemStack arrow : arrows) {
						if (ItemStack.isSameItemSameTags(arrow.getItem(), taken)) {
							arrow.setCount(arrow.getCount() + taken.getCount());
							merged = true;
							break;
						}
					}

					if (!merged) {
						arrows.add(new LargeItemStackList.LargeItemStack(taken));
					}
				}

				arrows.removeIf(LargeItemStackList.LargeItemStack::isEmpty);
				setArrows(invItem, new LargeItemStackList(Collections.unmodifiableList(arrows)));

				if (inventory.player instanceof ServerPlayer sp) {
					sp.connection.send(new ClientboundContainerSetSlotPacket(
						ClientboundContainerSetSlotPacket.PLAYER_INVENTORY, 0, i, invItem
					));
				}
			}

			if (stack.isEmpty()) break;
		}

		return stack.isEmpty();
	}

	@Override
	public boolean overrideStackedOnOther(ItemStack stack, Slot slot, ClickAction action, Player player) {
		if (stack.getCount() != 1 || action != ClickAction.SECONDARY) return false;

		List<LargeItemStackList.LargeItemStack> arrows = new ArrayList<>(getArrows(stack));
		ItemStack slotItem = slot.getItem();

		if (slotItem.isEmpty()) {
			if (!arrows.isEmpty()) {
				LargeItemStackList.LargeItemStack removed = arrows.get(arrows.size() - 1);
				if (removed != null) {
					ItemStack remain = slot.safeInsert(removed.splitMaxStack());
					removed.grow(remain.getCount());
				}
			}
		} else if (slotItem.is(ItemTags.ARROWS)) {

			int total = arrows.stream().mapToInt(LargeItemStackList.LargeItemStack::getCount).sum();

			if (total < MAX_ARROWS) {
				ItemStack taken = slot.safeTake(slotItem.getCount(), MAX_ARROWS - total, player);

				boolean merged = false;
				for (LargeItemStackList.LargeItemStack arrow : arrows) {
					if (ItemStack.isSameItemSameTags(arrow.getItem(), taken)) {
						arrow.setCount(arrow.getCount() + taken.getCount());
						merged = true;
						break;
					}
				}

				if (!merged) {
					arrows.add(new LargeItemStackList.LargeItemStack(taken));
				}
			}
		}

		arrows.removeIf(LargeItemStackList.LargeItemStack::isEmpty);
		setArrows(stack, new LargeItemStackList(Collections.unmodifiableList(arrows)));
		return true;
	}

	@Override
	public boolean overrideOtherStackedOnMe(ItemStack stack, ItemStack other, Slot slot, ClickAction action, Player player, SlotAccess access) {
		if (stack.getCount() != 1) return false;
		if (!other.isEmpty() && !other.is(ItemTags.ARROWS)) return false;

		if (action == ClickAction.SECONDARY && slot.allowModification(player)) {

			List<LargeItemStackList.LargeItemStack> arrows = new ArrayList<>(getArrows(stack));

			if (other.isEmpty()) {
				if (!arrows.isEmpty()) {
					LargeItemStackList.LargeItemStack removed = arrows.get(arrows.size() - 1);
					if (removed != null) {
						access.set(removed.splitMaxStack());
					}
				}
			} else if (other.is(ItemTags.ARROWS)) {

				int total = arrows.stream().mapToInt(LargeItemStackList.LargeItemStack::getCount).sum();

				if (total < MAX_ARROWS) {
					int capacity = MAX_ARROWS - total;

					boolean merged = false;
					for (LargeItemStackList.LargeItemStack arrow : arrows) {
						if (ItemStack.isSameItemSameTags(arrow.getItem(), other)) {
							int add = Math.min(other.getCount(), capacity);
							arrow.setCount(arrow.getCount() + add);
							other.shrink(add);
							merged = true;
							break;
						}
					}

					if (!merged) {
						arrows.add(new LargeItemStackList.LargeItemStack(other.split(capacity)));
					}
				}
			}

			arrows.removeIf(LargeItemStackList.LargeItemStack::isEmpty);
			setArrows(stack, new LargeItemStackList(Collections.unmodifiableList(arrows)));
			return true;
		}

		return false;
	}

	@Override
	public void onDestroyed(ItemEntity itemEntity) {
		ItemStack stack = itemEntity.getItem();
		LargeItemStackList arrows = getArrows(stack);

		if (!arrows.isEmpty()) {
			setArrows(stack, new LargeItemStackList(List.of()));

			for (LargeItemStackList.LargeItemStack arrow : arrows) {
				while (!arrow.isEmpty()) {
					ItemStack split = arrow.splitMaxStack();
					itemEntity.level().addFreshEntity(
						new ItemEntity(itemEntity.level(), itemEntity.getX(), itemEntity.getY(), itemEntity.getZ(), split)
					);
				}
			}
		}
	}

	@Override
	public boolean isBarVisible(ItemStack stack) {
		return !getArrows(stack).isEmpty();
	}

	@Override
	public int getBarWidth(ItemStack stack) {
		int total = getArrows(stack).stream().mapToInt(LargeItemStackList.LargeItemStack::getCount).sum();
		return Mth.clamp(Math.round(((float) total / MAX_ARROWS) * 13), 0, 13);
	}

	@Override
	public int getBarColor(ItemStack stack) {
		return 0x905ea8;
	}

	@Override
	public @NotNull Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
		LargeItemStackList arrows = getArrows(stack);
		return arrows.isEmpty() ? Optional.empty() : Optional.of(new GalacticQuiverTooltipComponent(arrows));
	}
}
