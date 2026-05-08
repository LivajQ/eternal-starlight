package cn.leolezury.eternalstarlight.common.block;

import cn.leolezury.eternalstarlight.common.block.entity.LootChestBlockEntity;
import cn.leolezury.eternalstarlight.common.registry.ESBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
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
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class LootChestBlock extends BaseEntityBlock {
	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

	private static final VoxelShape SHAPE = Shapes.or(
		Block.box(1, 1, 1, 15, 9, 15),
		Block.box(0.75, 7.75, 0.75, 15.25, 15.25, 15.25),
		Block.box(13, 0, 0, 16, 10, 3),
		Block.box(0, 0, 0, 3, 10, 3),
		Block.box(0, 0, 13, 3, 10, 16),
		Block.box(13, 0, 13, 16, 10, 16)
	);
	private static final VoxelShape EJECTING_SHAPE = Shapes.or(
		Block.box(1, 1, 1, 15, 9, 15),
		Block.box(13, 0, 0, 16, 10, 3),
		Block.box(0, 0, 0, 3, 10, 3),
		Block.box(0, 0, 13, 3, 10, 16),
		Block.box(13, 0, 13, 16, 10, 16)
	);

	public LootChestBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH));
	}

	@Override
	@Nullable
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {

		if (level.getBlockEntity(pos) instanceof LootChestBlockEntity blockEntity
			&& player instanceof ServerPlayer serverPlayer
			&& blockEntity.getRewardTargets().contains(serverPlayer.getUUID())
			&& blockEntity.isFree()) {

			blockEntity.rewardPlayer(serverPlayer, pos, this, player.isCrouching());
			level.playSound(null,
				pos.getX() + 0.5,
				pos.getY(),
				pos.getZ() + 0.5,
				SoundEvents.CHEST_OPEN,
				SoundSource.BLOCKS,
				1.0F,
				1.0F);

			return InteractionResult.CONSUME;
		}

		if (level.isClientSide) {
			return InteractionResult.SUCCESS;
		}

		return super.use(state, level, pos, player, hand, hit);
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.ENTITYBLOCK_ANIMATED;
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
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		if (level.getBlockEntity(pos) instanceof LootChestBlockEntity blockEntity && blockEntity.isEjecting()) {
			return EJECTING_SHAPE;
		}
		return SHAPE;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

	@Override
	public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new LootChestBlockEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return createTickerHelper(type, ESBlockEntities.LOOT_CHEST.get(), LootChestBlockEntity::tick);
	}
}
