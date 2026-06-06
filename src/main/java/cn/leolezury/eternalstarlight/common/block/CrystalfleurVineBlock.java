package cn.leolezury.eternalstarlight.common.block;

import cn.leolezury.eternalstarlight.common.registry.ESBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;

public class CrystalfleurVineBlock extends SimpleMultifaceBlock {

	public CrystalfleurVineBlock(Properties properties) {
		super(properties);
	}

	@Override
	public void performBonemeal(ServerLevel level, RandomSource randomSource, BlockPos pos, BlockState state) {
		super.performBonemeal(level, randomSource, pos, state);
		if (randomSource.nextInt(8) == 0) {
			for (Direction direction : Direction.values()) {
				BlockPos relativePos = pos.relative(direction);
				BlockState relativeState = level.getBlockState(relativePos);
				if (relativeState.is(ESBlocks.RED_STARLIGHT_CRYSTAL_CLUSTER.get()) && randomSource.nextBoolean()) {
					level.setBlockAndUpdate(relativePos, ESBlocks.BLOOMING_RED_STARLIGHT_CRYSTAL_CLUSTER.get().withPropertiesOf(relativeState));
				} else if (relativeState.is(ESBlocks.BLUE_STARLIGHT_CRYSTAL_CLUSTER.get()) && randomSource.nextBoolean()) {
					level.setBlockAndUpdate(relativePos, ESBlocks.BLOOMING_BLUE_STARLIGHT_CRYSTAL_CLUSTER.get().withPropertiesOf(relativeState));
				} else if (relativeState.isAir()) {
					BlockState flowerState = randomSource.nextBoolean() ? ESBlocks.RED_CRYSTALFLEUR.get().defaultBlockState() : ESBlocks.BLUE_CRYSTALFLEUR.get().defaultBlockState();
					if (flowerState.canSurvive(level, relativePos)) {
						level.setBlockAndUpdate(relativePos, flowerState);
					}
				}
			}
		}
	}
}
