package cn.leolezury.eternalstarlight.common.util;

import cn.leolezury.eternalstarlight.common.item.component.Accessory;
import cn.leolezury.eternalstarlight.common.registry.ESAccessories;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

public class ESAccessoryUtil {

	private static final String TAG_ACCESSORIES = "Accessories";
	private static final String TAG_ACCESSORY_SLOT_COUNT = "AccessorySlotCount";

	public static Set<Item> getActiveAccessoriesOnArmors(LivingEntity entity) {
		return getActiveAccessories(entity, Set.of(
			EquipmentSlot.HEAD,
			EquipmentSlot.CHEST,
			EquipmentSlot.LEGS,
			EquipmentSlot.FEET
		));
	}

	public static Set<Item> getActiveAccessories(LivingEntity entity, Set<EquipmentSlot> slots) {
		Set<Item> result = new HashSet<>();
		for (EquipmentSlot slot : slots) {
			result.addAll(getAccessories(entity.getItemBySlot(slot)));
		}
		return result;
	}

	public static Set<Item> getAccessories(ItemStack stack) {
		List<ItemStack> list = getAccessoryStacks(stack);
		return list.stream().map(ItemStack::getItem).collect(Collectors.toSet());
	}

	public static int getAccessorySlotCount(ItemStack stack) {
		CompoundTag tag = stack.getTag();
		if (tag != null && tag.contains(TAG_ACCESSORY_SLOT_COUNT, Tag.TAG_INT)) {
			return tag.getInt(TAG_ACCESSORY_SLOT_COUNT);
		}
		return 1;
	}

	public static void applyAccessory(ItemStack equipmentStack, ItemStack accessoryStack) {
		if (accessoryStack.isEmpty()) return;

		List<ItemStack> accessories = new ArrayList<>(getAccessoryStacks(equipmentStack));
		accessories.add(accessoryStack.copyWithCount(1));
		setAccessoryStacks(equipmentStack, Collections.unmodifiableList(accessories));
	}

	public static void removeAccessory(ItemStack equipmentStack, ItemStack accessoryStack) {
		List<ItemStack> accessories = new ArrayList<>(getAccessoryStacks(equipmentStack));
		accessories.removeIf(stack -> stack.is(accessoryStack.getItem()));
		setAccessoryStacks(equipmentStack, Collections.unmodifiableList(accessories));
	}

	public static boolean overrideEquipmentOnAccessory(ItemStack stack, Slot slot, ClickAction action, Player player) {
		if (stack.getCount() != 1 || action != ClickAction.SECONDARY) return false;

		List<ItemStack> accessories = getAccessoryStacks(stack);
		ItemStack slotItem = slot.getItem();
		Accessory accessory = ESAccessories.get(slotItem);

		if (accessories.isEmpty() && accessory == null) return false;

		if (slotItem.isEmpty()) {
			if (!accessories.isEmpty()) {
				ItemStack removed = accessories.get(accessories.size() - 1);
				removeAccessory(stack, removed);
				ItemStack remain = slot.safeInsert(removed.copy());
				applyAccessory(stack, remain);
			}
		} else if (accessory != null
			&& stack.is(accessory.combinationTarget())
			&& getAccessorySlotCount(stack) > accessories.size()
			&& accessories.stream().noneMatch(s -> s.is(slotItem.getItem()))) {

			ItemStack taken = slot.safeTake(slotItem.getCount(), 1, player);
			applyAccessory(stack, taken);
		}

		return true;
	}

	public static boolean overrideAccessoryOnEquipment(ItemStack stack, ItemStack other, Slot slot, ClickAction action, Player player, SlotAccess access) {
		if (stack.getCount() != 1) return false;

		Accessory accessory = ESAccessories.get(other);
		if (!other.isEmpty() && accessory == null) return false;

		if (action == ClickAction.SECONDARY && slot.allowModification(player)) {
			List<ItemStack> accessories = getAccessoryStacks(stack);

			if (other.isEmpty()) {
				if (!accessories.isEmpty()) {
					ItemStack removed = accessories.get(accessories.size() - 1);
					removeAccessory(stack, removed);
					access.set(removed.copy());
				}
			} else if (accessory != null
				&& stack.is(accessory.combinationTarget())
				&& getAccessorySlotCount(stack) > accessories.size()
				&& accessories.stream().noneMatch(s -> s.is(other.getItem()))) {

				applyAccessory(stack, other);
				other.shrink(1);
			}
			return true;
		} else {
			return false;
		}
	}

	public static List<ItemStack> getAccessoryStacks(ItemStack equipmentStack) {
		CompoundTag tag = equipmentStack.getTag();
		if (tag == null || !tag.contains(TAG_ACCESSORIES, Tag.TAG_LIST)) {
			return new ArrayList<>();
		}

		ListTag list = tag.getList(TAG_ACCESSORIES, Tag.TAG_COMPOUND);
		List<ItemStack> result = new ArrayList<>();

		for (int i = 0; i < list.size(); i++) {
			result.add(ItemStack.of(list.getCompound(i)));
		}

		return result;
	}

	public static void setAccessoryStacks(ItemStack equipmentStack, List<ItemStack> stacks) {
		CompoundTag tag = equipmentStack.getOrCreateTag();
		ListTag list = new ListTag();
		for (ItemStack s : stacks) {
			list.add(s.save(new CompoundTag()));
		}
		tag.put(TAG_ACCESSORIES, list);
	}
}
