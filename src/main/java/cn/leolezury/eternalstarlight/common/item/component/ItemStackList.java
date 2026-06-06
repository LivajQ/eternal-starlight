package cn.leolezury.eternalstarlight.common.item.component;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.AbstractList;
import java.util.List;

public class ItemStackList extends AbstractList<ItemStack> {
	private final List<ItemStack> list;

	public ItemStackList(List<ItemStack> list) {
		this.list = list;
	}

	@Override
	public ItemStack get(int index) {
		return this.list.get(index);
	}

	@Override
	public ItemStack set(int index, ItemStack value) {
		return this.list.set(index, value);
	}

	@Override
	public void add(int index, ItemStack value) {
		this.list.add(index, value);
	}

	@Override
	public ItemStack remove(int index) {
		return this.list.remove(index);
	}

	@Override
	public int size() {
		return this.list.size();
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) return true;
		if (!(other instanceof ItemStackList stacks)) return false;
		return stacksEqual(this.list, stacks.list);
	}

	@Override
	public int hashCode() {
		return hashStacks(this.list);
	}

	private static boolean stacksEqual(List<ItemStack> a, List<ItemStack> b) {
		if (a.size() != b.size()) return false;

		for (int i = 0; i < a.size(); i++) {
			if (!ItemStack.isSameItemSameTags(a.get(i), b.get(i))) {
				return false;
			}
		}

		return true;
	}

	private static int hashStacks(List<ItemStack> list) {
		int hash = 1;

		for (ItemStack stack : list) {
			int stackHash = 0;

			if (!stack.isEmpty()) {
				stackHash = Item.getId(stack.getItem());
				if (stack.hasTag()) {
					stackHash = 31 * stackHash + stack.getTag().hashCode();
				}
				stackHash = 31 * stackHash + stack.getCount();
			}

			hash = 31 * hash + stackHash;
		}

		return hash;
	}
}
