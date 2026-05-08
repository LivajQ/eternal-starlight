package cn.leolezury.eternalstarlight.common.block;

import cn.leolezury.eternalstarlight.common.block.entity.DuskLockboxBlockEntity;
import cn.leolezury.eternalstarlight.common.registry.ESBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class DuskLockboxBlock extends BaseEntityBlock {

	public DuskLockboxBlock(Properties properties) {
		super(properties);
	}

	@Override
	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	@Override
	public int getAnalogOutputSignal(BlockState blockState, Level level, BlockPos pos) {
		return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(level.getBlockEntity(pos));
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new DuskLockboxBlockEntity(pos, state);
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

	@Override
	public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
		ItemStack stack = super.getCloneItemStack(level, pos, state);

		level.getBlockEntity(pos, ESBlockEntities.DUSK_LOCKBOX.get())
			.ifPresent(be -> be.saveToItem(stack));

		return stack;
	}
}
