package cn.leolezury.eternalstarlight.common.mixin;

import cn.leolezury.eternalstarlight.common.registry.ESBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CropBlock.class)
public abstract class CropBlockMixin {

	@Inject(method = "mayPlaceOn", at = @At("HEAD"), cancellable = true)
	private void mayPlaceOn(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CallbackInfoReturnable<Boolean> cir) {
		if (blockState.is(ESBlocks.NIGHTFALL_FARMLAND.get())) {
			cir.setReturnValue(true);
		}
	}

	@Inject(method = "randomTick", at = @At("HEAD"), cancellable = true)
	private void hasSufficientLight(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource, CallbackInfo ci) {
		if (serverLevel.getBlockState(blockPos).is(ESBlocks.PUNGENCY_FRUIT_VINES.get())) {
			CropBlock crop = (CropBlock)(Object)this;

			int i = crop.getAge(blockState);
			if (i < crop.getMaxAge()) {
				float f = CropBlock.getGrowthSpeed(crop, serverLevel, blockPos);
				if (randomSource.nextInt((int)(25.0F / f) + 1) == 0) {
					serverLevel.setBlock(blockPos, crop.getStateForAge(i + 1), 2);
				}
			}
			ci.cancel();
		}
	}
}
