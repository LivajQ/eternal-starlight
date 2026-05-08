package cn.leolezury.eternalstarlight.common.block;

import cn.leolezury.eternalstarlight.common.block.entity.StellarRackBlockEntity;
import cn.leolezury.eternalstarlight.common.registry.ESBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class StellarRackBlock extends BaseEntityBlock {
	protected static final VoxelShape SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 12.0, 16.0);

	public StellarRackBlock(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {

		ItemStack stack = player.getItemInHand(hand);

		if (level.getBlockEntity(pos) instanceof StellarRackBlockEntity entity) {

			if (entity.anyEmpty() && !stack.isEmpty()) {

				if (!level.isClientSide && entity.placeItem(stack.copyWithCount(1))) {
					stack.shrink(1);
					return InteractionResult.sidedSuccess(level.isClientSide);
				}

				return InteractionResult.CONSUME;
			}
		}

		return InteractionResult.PASS;
	}

	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {

		if (!state.is(newState.getBlock())) {
			BlockEntity be = level.getBlockEntity(pos);

			if (be instanceof Container container) {
				Containers.dropContents(level, pos, container);
				level.updateNeighbourForOutputSignal(pos, this);
			}
		}

		super.onRemove(state, level, pos, newState, isMoving);
	}

	@Override
	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	@Override
	public int getAnalogOutputSignal(BlockState blockState, Level level, BlockPos pos) {
		return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(level.getBlockEntity(pos));
	}

	@Override
	public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
		return SHAPE;
	}

	@Override
	public RenderShape getRenderShape(BlockState blockState) {
		return RenderShape.MODEL;
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new StellarRackBlockEntity(blockPos, blockState);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return createTickerHelper(type, ESBlockEntities.STELLAR_RACK.get(), StellarRackBlockEntity::tick);
	}
}
