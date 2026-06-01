package cn.leolezury.eternalstarlight.common.mixin;

import cn.leolezury.eternalstarlight.common.data.ESDimensions;
import cn.leolezury.eternalstarlight.common.util.ESWeatherUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin {

	@Inject(
		method = "tickChunk",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/level/block/Block;handlePrecipitation(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/biome/Biome$Precipitation;)V",
			shift = At.Shift.AFTER
		)
	)
	private void afterPrecipitationTick(LevelChunk chunk, int randomTickSpeed, CallbackInfo ci) {
		ServerLevel level = (ServerLevel)(Object)this;

		if (level.dimension() == ESDimensions.STARLIGHT_KEY) {
			ESWeatherUtil.getOrCreateWeathers(level)
				.getActiveWeather()
				.ifPresent(instance -> {
					BlockPos pos = chunk.getPos().getMiddleBlockPosition(level.getMinBuildHeight());
					instance.getWeather().tickBlock(level, instance.ticksSinceStarted, pos);
				});
		}
	}
}
