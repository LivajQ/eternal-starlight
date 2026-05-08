package cn.leolezury.eternalstarlight.common.block;

import cn.leolezury.eternalstarlight.common.registry.ESBlocks;
import cn.leolezury.eternalstarlight.common.registry.ESItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.GrowingPlantBodyBlock;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;

public class BerriesVinesPlantBlock extends GrowingPlantBodyBlock implements BerriesVines {

	public BerriesVinesPlantBlock(BlockBehaviour.Properties properties) {
		super(properties, Direction.DOWN, SHAPE, false);
		this.registerDefaultState(this.stateDefinition.any().setValue(BERRIES, false));
	}

	@Override
	protected GrowingPlantHeadBlock getHeadBlock() {
		return ESBlocks.BERRIES_VINES.get();
	}

	@Override
	protected BlockState updateHeadAfterConvertedFromBody(BlockState state, BlockState blockState) {
		return blockState.setValue(BERRIES, state.getValue(BERRIES));
	}

	@Override
	public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
		return new ItemStack(ESItems.LUNAR_BERRIES.get());
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		return BerriesVines.use(state, level, pos);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(BERRIES);
	}

	@Override
	public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state, boolean isClientSide) {
		return !state.getValue(BERRIES);
	}

	@Override
	public boolean isBonemealSuccess(Level level, RandomSource randomSource, BlockPos pos, BlockState state) {
		return true;
	}

	@Override
	public void performBonemeal(ServerLevel serverLevel, RandomSource randomSource, BlockPos blockPos, BlockState blockState) {
		serverLevel.setBlock(blockPos, blockState.setValue(BERRIES, true), 2);
	}
}
