package cn.leolezury.eternalstarlight.common.block;

import cn.leolezury.eternalstarlight.common.block.entity.AbstractDuskLightBlockEntity;
import cn.leolezury.eternalstarlight.common.block.entity.DuskLightBlockEntity;
import cn.leolezury.eternalstarlight.common.registry.ESBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class DuskLightBlock extends BaseEntityBlock {
	private static final List<Direction> FACING_ORDER = Arrays.stream(Direction.values()).toList();
	public static final DirectionProperty FACING = BlockStateProperties.FACING;

	public DuskLightBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(getStateDefinition().any().setValue(FACING, Direction.UP));
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (level.getBlockEntity(pos) instanceof DuskLightBlockEntity entity && entity.isLit()) {
			if (!level.isClientSide) {
				Direction facing = state.getValue(FACING);

				for (int i = 1; i < FACING_ORDER.size(); i++) {
					Direction dir = FACING_ORDER.get((FACING_ORDER.indexOf(facing) + i) % FACING_ORDER.size());
					BlockPos relativePos = pos.relative(dir);
					BlockState relativeState = level.getBlockState(relativePos);

					if (relativeState.getCollisionShape(level, relativePos).isEmpty()
						|| AbstractDuskLightBlockEntity.canPassThrough(relativeState)
						|| AbstractDuskLightBlockEntity.canDestroy(relativeState)) {

						level.setBlockAndUpdate(pos, state.setValue(FACING, dir));
						break;
					}
				}
			}

			return InteractionResult.sidedSuccess(level.isClientSide);
		}

		return InteractionResult.PASS;
	}

	@Override
	@Nullable
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState().setValue(FACING, context.getNearestLookingDirection().getOpposite());
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rotation) {
		return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
	}

	@Override
	public BlockState mirror(BlockState state, Mirror mirror) {
		return state.rotate(mirror.getRotation(state.getValue(FACING)));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

	@Override
	public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new DuskLightBlockEntity(blockPos, blockState);
	}

	@Override
	public RenderShape getRenderShape(BlockState blockState) {
		return RenderShape.MODEL;
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
		return createTickerHelper(blockEntityType, ESBlockEntities.DUSK_LIGHT.get(), AbstractDuskLightBlockEntity::tick);
	}
}
