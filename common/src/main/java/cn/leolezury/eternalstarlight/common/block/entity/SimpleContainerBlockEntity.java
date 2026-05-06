package cn.leolezury.eternalstarlight.common.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class SimpleContainerBlockEntity extends BlockEntity implements Container {

	protected SimpleContainerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	protected abstract NonNullList<ItemStack> getItems();

	@Override
	public int getContainerSize() {
		return getItems().size();
	}

	@Override
	public boolean isEmpty() {
		for (ItemStack stack : getItems()) {
			if (!stack.isEmpty()) return false;
		}
		return true;
	}

	@Override
	public ItemStack getItem(int slot) {
		return getItems().get(slot);
	}

	@Override
	public ItemStack removeItem(int slot, int amount) {
		ItemStack result = ContainerHelper.removeItem(getItems(), slot, amount);
		if (!result.isEmpty()) setChanged();
		return result;
	}

	@Override
	public ItemStack removeItemNoUpdate(int slot) {
		return ContainerHelper.takeItem(getItems(), slot);
	}

	@Override
	public void setItem(int slot, ItemStack stack) {
		getItems().set(slot, stack);
		if (!stack.isEmpty() && stack.getCount() > getMaxStackSize()) {
			stack.setCount(getMaxStackSize());
		}
		setChanged();
	}

	@Override
	public boolean stillValid(Player player) {
		return Container.stillValidBlockEntity(this, player);
	}

	@Override
	public void clearContent() {
		getItems().clear();
	}

	@Override
	public void load(CompoundTag tag) {
		super.load(tag);
		ContainerHelper.loadAllItems(tag, getItems());
	}

	@Override
	protected void saveAdditional(CompoundTag tag) {
		super.saveAdditional(tag);
		ContainerHelper.saveAllItems(tag, getItems());
	}
}
