package cn.leolezury.eternalstarlight.common.block;

import cn.leolezury.eternalstarlight.common.registry.ESBlocks;
import cn.leolezury.eternalstarlight.common.registry.ESItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.GrowingPlantBodyBlock;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.world.level.block.state.BlockState;

public class CaveMossPlantBlock extends GrowingPlantBodyBlock {

	public CaveMossPlantBlock(Properties properties) {
		super(properties, Direction.DOWN, CaveMossBlock.SHAPE, false);
	}

	@Override
	public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
		return ESItems.CAVE_MOSS.get().getDefaultInstance();
	}

	@Override
	protected GrowingPlantHeadBlock getHeadBlock() {
		return ESBlocks.CAVE_MOSS.get();
	}
}
