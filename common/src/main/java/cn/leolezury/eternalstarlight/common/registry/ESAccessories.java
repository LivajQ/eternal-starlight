package cn.leolezury.eternalstarlight.common.registry;

import cn.leolezury.eternalstarlight.common.item.component.Accessory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public final class ESAccessories {

	private static final Map<Item, Accessory> ACCESSORIES = new HashMap<>();

	private ESAccessories() {}

	public static void register(Item item, Accessory accessory) {
		ACCESSORIES.put(item, accessory);
	}

	@Nullable
	public static Accessory get(ItemStack stack) {
		return ACCESSORIES.get(stack.getItem());
	}
}
