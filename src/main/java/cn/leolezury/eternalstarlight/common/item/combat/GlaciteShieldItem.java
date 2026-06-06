package cn.leolezury.eternalstarlight.common.item.combat;

import cn.leolezury.eternalstarlight.common.client.renderer.ESForgeItemStackRenderer;
import cn.leolezury.eternalstarlight.common.util.ESConventionalTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.function.Consumer;

public class GlaciteShieldItem extends ShieldItem {
	public GlaciteShieldItem(Properties properties) {
		super(properties);
	}

	@Override
	public boolean isValidRepairItem(ItemStack stack, ItemStack repairCandidate) {
		return repairCandidate.is(ESConventionalTags.Items.GEMS_GLACITE) || super.isValidRepairItem(stack, repairCandidate);
	}

	@Override
	public void initializeClient(Consumer<IClientItemExtensions> consumer) {
		consumer.accept(ESForgeItemStackRenderer.CLIENT_ITEM_EXTENSION);
	}
}
