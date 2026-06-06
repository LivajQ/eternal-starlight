package cn.leolezury.eternalstarlight.common.mixin.client;

import net.minecraft.client.renderer.blockentity.StructureBlockRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

// copied from Huge Structure Blocks mod since, for reasons only known to god, that mod causes mixin errors in completely unrelated classes for Secrets of the Void
@Mixin(
	value = {StructureBlockRenderer.class},
	priority = 999
)
public class StructureBlockRendererMixin {
	@ModifyConstant(
		method = {"getViewDistance"},
		constant = {@Constant(
			intValue = 96
		)},
		require = 0
	)
	public int getRenderDistance(int value) {
		return 256;
	}
}
