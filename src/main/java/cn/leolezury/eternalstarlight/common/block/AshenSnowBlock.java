package cn.leolezury.eternalstarlight.common.block;

import cn.leolezury.eternalstarlight.common.registry.ESParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class AshenSnowBlock extends LayeredBlock {
	public AshenSnowBlock(Properties properties) {
		super(properties);
	}

	@Override
	public void stepOn(Level level, BlockPos blockPos, BlockState blockState, Entity entity) {
		if (level.isClientSide && entity.getDeltaMovement().length() > 0.001f) {
			for (int i = 0; i < 4; i++) {
				float scale = Mth.randomBetween(level.getRandom(), 1.75f, 2.25f);
				level.addParticle(ESParticles.ASHEN_SNOW.get(), entity.getRandomX(0.75), entity.getY(), entity.getRandomZ(0.75), entity.getDeltaMovement().x() / scale, Mth.randomBetween(level.getRandom(), 0.15f, 0.25f), entity.getDeltaMovement().z() / scale);
			}
		}
	}
}
