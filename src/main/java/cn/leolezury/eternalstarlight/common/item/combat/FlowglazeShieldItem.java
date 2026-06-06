package cn.leolezury.eternalstarlight.common.item.combat;

import cn.leolezury.eternalstarlight.common.client.renderer.ESForgeItemStackRenderer;
import cn.leolezury.eternalstarlight.common.registry.ESItems;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.function.Consumer;

public class FlowglazeShieldItem extends ShieldItem {
	public FlowglazeShieldItem(Properties properties) {
		super(properties);
	}

	@Override
	public boolean isValidRepairItem(ItemStack stack, ItemStack repairCandidate) {
		return repairCandidate.is(ESItems.FLOWGLAZE.get()) || super.isValidRepairItem(stack, repairCandidate);
	}

	@Override
	public void initializeClient(Consumer<IClientItemExtensions> consumer) {
		consumer.accept(ESForgeItemStackRenderer.CLIENT_ITEM_EXTENSION);
	}
}
