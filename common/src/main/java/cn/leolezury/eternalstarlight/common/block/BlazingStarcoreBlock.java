package cn.leolezury.eternalstarlight.common.block;

import cn.leolezury.eternalstarlight.common.registry.ESBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public class BlazingStarcoreBlock extends Block {

	public BlazingStarcoreBlock(Properties properties) {
		super(properties);
	}

	@Override
	public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
		BlockState state = super.getStateForPlacement(context);
		return state == null ? null : (isAffectedByFluid(state, context.getLevel(), context.getClickedPos(), FluidTags.WATER) ? ESBlocks.STARCORE_BLOCK.get().defaultBlockState() : state);
	}

	@Override
	public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
		BlockState newState = super.updateShape(state, direction, neighborState, level, pos, neighborPos);
		return isAffectedByFluid(newState, level, pos, FluidTags.WATER) ? ESBlocks.STARCORE_BLOCK.get().defaultBlockState() : newState;
	}

	private boolean isAffectedByFluid(BlockState state, LevelAccessor level, BlockPos pos, TagKey<Fluid> fluid) {
		return Arrays.stream(Direction.values()).anyMatch(direction -> level.getFluidState(pos.relative(direction)).is(fluid)) || state.getFluidState().is(fluid);
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		ItemStack stack = player.getItemInHand(hand);

		if (stack.is(ItemTags.PICKAXES)) {
			if (!level.isClientSide) {
				level.destroyBlock(pos, false);
				level.setBlockAndUpdate(pos, ESBlocks.STARCORE_LIGHT.get().defaultBlockState());
				stack.hurtAndBreak(1, player, e -> e.broadcastBreakEvent(hand));
			}
			return InteractionResult.sidedSuccess(level.isClientSide);
		}

		return InteractionResult.PASS;
	}

}
