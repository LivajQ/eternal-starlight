package cn.leolezury.eternalstarlight.common.world.gen.carver;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Aquifer;
import net.minecraft.world.level.levelgen.carver.CarvingContext;
import net.minecraft.world.level.levelgen.carver.CaveCarverConfiguration;
import net.minecraft.world.level.levelgen.carver.CaveWorldCarver;

public class ESCaveCarver extends CaveWorldCarver {
	public ESCaveCarver(Codec<CaveCarverConfiguration> codec) {
		super(codec);
	}

	@Override
	protected boolean canReplaceBlock(CaveCarverConfiguration config, BlockState state) {
		// we should not replace water
		return super.canReplaceBlock(config, state) && state.getFluidState().isEmpty();
	}

	@Override
	protected BlockState getCarveState(CarvingContext context, CaveCarverConfiguration config, BlockPos pos, Aquifer aquifer) {
		int lavaY = config.lavaLevel.resolveY(context);
		if (pos.getY() <= lavaY) return LAVA.createLegacyBlock();
		else return CAVE_AIR;
	}
}
