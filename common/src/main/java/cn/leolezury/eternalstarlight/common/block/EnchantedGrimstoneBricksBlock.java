package cn.leolezury.eternalstarlight.common.block;

import cn.leolezury.eternalstarlight.common.data.ESCrests;
import cn.leolezury.eternalstarlight.common.entity.misc.CrestEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class EnchantedGrimstoneBricksBlock extends HorizontalDirectionalBlock {

	public EnchantedGrimstoneBricksBlock(Properties properties) {
		super(properties);
	}

	@Nullable
	public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
		return defaultBlockState().setValue(FACING, blockPlaceContext.getHorizontalDirection().getOpposite());
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		player.displayClientMessage(Component.translatable("message.eternal_starlight.enchanted_grimstone_pre"), true);
		player.displayClientMessage(Component.translatable("message.eternal_starlight.enchanted_grimstone_pre"), true);
		return InteractionResult.sidedSuccess(level.isClientSide);
	}

	@Override
	public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
		player.displayClientMessage(Component.translatable("message.eternal_starlight.enchanted_grimstone_post"), true);

		CrestEntity crest = new CrestEntity(level, pos.getX(), pos.getY(), pos.getZ(), ESCrests.BOULDERS_SHIELD);
		level.addFreshEntity(crest);

		super.playerWillDestroy(level, pos, state, player);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}
}
