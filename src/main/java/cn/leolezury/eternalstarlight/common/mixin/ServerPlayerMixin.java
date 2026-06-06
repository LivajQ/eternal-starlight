package cn.leolezury.eternalstarlight.common.mixin;

import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin {

	/*
	@WrapOperation(
		method = "respawn",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/server/level/ServerPlayer;changeDimension(Lnet/minecraft/server/level/ServerLevel;)Lnet/minecraft/server/level/ServerPlayer;"
		)
	)
	private ServerPlayer modifyRespawnDimension(ServerPlayer player, ServerLevel target, Operation<ServerPlayer> original) {
		if (ESConfig.INSTANCE.respawnInEternalStarlight) {
			ServerLevel starlight = player.server.getLevel(ESDimensions.STARLIGHT_KEY);
			return original.call(player, starlight);
		}

		return original.call(player, target);
	}

	 */
}

