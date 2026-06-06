package cn.leolezury.eternalstarlight.common.block;

import cn.leolezury.eternalstarlight.common.block.entity.ESBrushableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BrushableBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class ESBrushableBlock extends BrushableBlock {

	public ESBrushableBlock(Block turnsInto, BlockBehaviour.Properties properties, SoundEvent brushSound, SoundEvent brushCompletedSound) {
		super(turnsInto, properties, brushSound, brushCompletedSound);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new ESBrushableBlockEntity(blockPos, blockState);
	}
}
