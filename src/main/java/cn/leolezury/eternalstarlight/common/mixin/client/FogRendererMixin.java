package cn.leolezury.eternalstarlight.common.mixin.client;

import cn.leolezury.eternalstarlight.common.client.handler.ESClientHandler;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FogRenderer.class)
public abstract class FogRendererMixin {
	@WrapOperation(method = "setupColor", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getWaterVision()F"))
	private static float setupColor(LocalPlayer instance, Operation<Float> original) {

		float partialTicks = Minecraft.getInstance().getFrameTime();

		float modifier = Mth.lerp(
			partialTicks,
			ESClientHandler.oldAbyssalFogModifier,
			ESClientHandler.abyssalFogModifier
		);

		return Mth.lerp(modifier, 0f, original.call(instance));
	}

}
