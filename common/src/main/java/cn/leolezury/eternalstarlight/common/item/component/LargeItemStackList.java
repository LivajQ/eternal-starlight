package cn.leolezury.eternalstarlight.common.item.component;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

import java.util.AbstractList;
import java.util.List;

public class LargeItemStackList extends AbstractList<LargeItemStackList.LargeItemStack> {
	private final List<LargeItemStack> list;

	public LargeItemStackList(List<LargeItemStack> list) {
		this.list = list;
	}

	@Override
	public LargeItemStack get(int index) {
		return this.list.get(index);
	}

	@Override
	public LargeItemStack set(int index, LargeItemStack value) {
		return this.list.set(index, value);
	}

	@Override
	public void add(int index, LargeItemStack value) {
		this.list.add(index, value);
	}

	@Override
	public LargeItemStack remove(int index) {
		return this.list.remove(index);
	}

	@Override
	public int size() {
		return this.list.size();
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		} else {
			if (other instanceof LargeItemStackList stacks) {
				if (stacks.size() != size()) {
					return false;
				} else {
					for (int i = 0; i < stacks.size(); ++i) {
						if (!ItemStack.matches(stacks.get(i).getItem(), get(i).getItem()) || stacks.get(i).getCount() != get(i).getCount()) {
							return false;
						}
					}
					return true;
				}
			} else {
				return false;
			}
		}
	}

	public static class LargeItemStack {

		private final ItemStack item;
		private int count;

		public LargeItemStack(ItemStack stack) {
			this(stack.copyWithCount(1), stack.getCount());
		}

		public LargeItemStack(ItemStack item, int count) {
			this.item = item;
			this.count = count;
		}

		public ItemStack getItem() { return item; }
		public int getCount() { return count; }
		public void setCount(int count) { this.count = count; }

		public boolean isEmpty() {
			return item.isEmpty() || count == 0;
		}

		public void grow(int amount) { count += amount; }
		public void shrink(int amount) { count -= amount; }

		public ItemStack split(int amount) {
			int splitCount = Math.min(amount, count);
			ItemStack out = item.copyWithCount(splitCount);
			count -= splitCount;
			return out;
		}

		public ItemStack asItemStack() {
			return item.copyWithCount(count);
		}

		public ItemStack splitMaxStack() {
			return split(item.getMaxStackSize());
		}

		public CompoundTag toTag() {
			CompoundTag tag = new CompoundTag();
			CompoundTag itemTag = new CompoundTag();
			item.save(itemTag);
			tag.put("Item", itemTag);
			tag.putInt("Count", count);
			return tag;
		}

		public static LargeItemStack fromTag(CompoundTag tag) {
			ItemStack item = ItemStack.of(tag.getCompound("Item"));
			int count = tag.getInt("Count");
			return new LargeItemStack(item, count);
		}
	}
}
