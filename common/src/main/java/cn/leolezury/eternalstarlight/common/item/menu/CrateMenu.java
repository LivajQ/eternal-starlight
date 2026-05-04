package cn.leolezury.eternalstarlight.common.item.menu;

import cn.leolezury.eternalstarlight.common.registry.ESMenuTypes;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ShulkerBoxSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class CrateMenu extends AbstractContainerMenu {
	private final Container container;

	public CrateMenu(int containerId, Inventory playerInventory) {
		this(containerId, playerInventory, new SimpleContainer(36));
	}

	public CrateMenu(int containerId, Inventory inventory, Container container) {
		super(ESMenuTypes.CRATE.get(), containerId);
		checkContainerSize(container, 36);
		this.container = container;
		container.startOpen(inventory.player);

		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 9; x++) {
				this.addSlot(new ShulkerBoxSlot(container, x + y * 9, 8 + x * 18, 18 + y * 18));
			}
		}

		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 9; x++) {
				this.addSlot(new Slot(inventory, x + y * 9 + 9, 8 + x * 18, 103 + y * 18));
			}
		}

		for (int y = 0; y < 9; y++) {
			this.addSlot(new Slot(inventory, y, 8 + y * 18, 161));
		}
	}

	public Container getContainer() {
		return container;
	}

	@Override
	public boolean stillValid(Player player) {
		return this.container.stillValid(player);
	}

	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		ItemStack itemStack = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);
		if (slot.hasItem()) {
			ItemStack slotItem = slot.getItem();
			itemStack = slotItem.copy();
			if (index < this.container.getContainerSize()) {
				if (!this.moveItemStackTo(slotItem, this.container.getContainerSize(), this.slots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.moveItemStackTo(slotItem, 0, this.container.getContainerSize(), false)) {
				return ItemStack.EMPTY;
			}

			if (slotItem.isEmpty()) {
				slot.setByPlayer(ItemStack.EMPTY);
			} else {
				slot.setChanged();
			}
		}

		return itemStack;
	}

	@Override
	public void removed(Player player) {
		super.removed(player);
		this.container.stopOpen(player);
	}
}
