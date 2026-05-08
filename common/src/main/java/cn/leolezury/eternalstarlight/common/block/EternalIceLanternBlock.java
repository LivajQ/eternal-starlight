package cn.leolezury.eternalstarlight.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class EternalIceLanternBlock extends LanternBlock {
	public static final VoxelShape SHAPE = Block.box(6, 0, 6, 10, 10, 10);

	public EternalIceLanternBlock(Properties properties) {
		super(properties);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}
}
