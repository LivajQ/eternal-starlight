package cn.leolezury.eternalstarlight.common.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.StructureBlockEntity;
import net.minecraft.world.level.block.state.properties.StructureMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

// copied from Huge Structure Blocks mod since, for reasons only known to god, that mod causes mixin errors in completely unrelated classes for Secrets of the Void
@Mixin(
	value = {StructureBlockEntity.class},
	priority = 999
)
public abstract class StructureBlockEntityMixin {

	@ModifyConstant(
		method = {"load"},
		constant = {@Constant(
			intValue = 48
		)},
		require = 0
	)
	public int readNbtUpper(int value) {
		return 512;
	}

	@ModifyConstant(
		method = {"load"},
		constant = {@Constant(
			intValue = -48
		)},
		require = 0
	)
	public int readNbtLower(int value) {
		return -512;
	}

	@Overwrite
	private Stream<BlockPos> getRelatedCorners(BlockPos min, BlockPos max) {
		StructureBlockEntity blockEntity = (StructureBlockEntity)(Object)this;
		Level level = blockEntity.getLevel();
		if (level == null) {
			return Stream.empty();
		} else {
			BlockPos middle = blockEntity.getBlockPos();
			List<BlockPos> blocks = new ArrayList(2);
			int maxSearch = this.detectSize(-1) + 1;
			BlockPos.findClosestMatch(middle, maxSearch, Math.min(maxSearch, level.getHeight() + 1), (pos) -> {
				BlockEntity patt1919$temp = level.getBlockEntity(pos);
				if (patt1919$temp instanceof StructureBlockEntity block) {
					if (block.getMode() == StructureMode.CORNER && Objects.equals(blockEntity.getStructureName(), block.getStructureName())) {
						blocks.add(block.getBlockPos());
					}
				}

				return blocks.size() == 2;
			});
			return blocks.stream();
		}
	}

	@ModifyConstant(
		method = {"detectSize"},
		constant = {@Constant(
			intValue = 80
		)},
		require = 0
	)
	public int detectSize(int value) {
		return 512;
	}
}
