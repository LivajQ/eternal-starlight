package cn.leolezury.eternalstarlight.common.block;

import cn.leolezury.eternalstarlight.common.registry.ESItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

public class CaveMossVeinBlock extends SimpleMultifaceBlock {
	public CaveMossVeinBlock(Properties properties) {
		super(properties);
	}

	@Override
	public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
		return ESItems.CAVE_MOSS.get().getDefaultInstance();
	}
}
