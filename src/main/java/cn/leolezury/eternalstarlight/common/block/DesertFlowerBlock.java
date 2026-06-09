package cn.leolezury.eternalstarlight.common.block;

import cn.leolezury.eternalstarlight.common.registry.ESBlocks;
import cn.leolezury.eternalstarlight.common.util.ESTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.state.BlockState;

public class DesertFlowerBlock extends FlowerBlock {

	public DesertFlowerBlock(Holder<MobEffect> holder, int duration, Properties properties) {
		// TODO holder null at reg time
		 super(holder::value, duration, properties);
		//super(MobEffects.WEAKNESS, duration, properties);
	}

	@Override
	protected boolean mayPlaceOn(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
		return super.mayPlaceOn(blockState, blockGetter, blockPos) || blockState.is(BlockTags.SAND) || blockState.is(ESTags.Blocks.BASE_STONE_STARLIGHT) || blockState.is(ESBlocks.RED_STARLIGHT_CRYSTAL_BLOCK.get()) || blockState.is(ESBlocks.BLUE_STARLIGHT_CRYSTAL_BLOCK.get());
	}
}
