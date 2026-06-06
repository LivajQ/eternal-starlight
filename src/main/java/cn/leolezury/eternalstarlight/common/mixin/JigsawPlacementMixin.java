package cn.leolezury.eternalstarlight.common.mixin;

import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

// copied from Huge Structure Blocks mod since, for reasons only known to god, that mod causes mixin errors in completely unrelated classes for Secrets of the Void
@Mixin(
	value = {JigsawPlacement.class},
	priority = 999
)
public class JigsawPlacementMixin {
	@ModifyConstant(
		method = {"generateJigsaw"},
		constant = {@Constant(
			intValue = 128
		)},
		require = 0
	)
	private static int changeMaxGenDistance(int value) {
		return 512;
	}
}
