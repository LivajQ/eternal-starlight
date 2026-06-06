package cn.leolezury.eternalstarlight.common.world;

import cn.leolezury.eternalstarlight.common.block.ESPortalBlock;
import cn.leolezury.eternalstarlight.common.registry.ESBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.Optional;

public class ESTeleporter {
	protected static Optional<BlockPos> getOrMakePortal(ServerLevel level, Entity entity, BlockPos entrancePos, BlockPos pos) {
		Optional<BlockPos> existingPortal = getExistingPortal(level, pos);
		if (existingPortal.isPresent()) {
			return existingPortal;
		} else {
			Direction.Axis portalAxis = entity.level().getBlockState(entrancePos).getOptionalValue(ESPortalBlock.AXIS).orElse(Direction.Axis.X);
			return makePortal(level, pos, portalAxis);
		}
	}

	public static Optional<BlockPos> getExistingPortal(ServerLevel level, BlockPos pos) {
		int maxHeight = level.getMaxBuildHeight();
		int minHeight = level.getMinBuildHeight();
		WorldBorder border = level.getWorldBorder();
		BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos();
		for (int x = pos.getX() - 16; x <= pos.getX() + 16; x++) {
			for (int z = pos.getZ() - 16; z <= pos.getZ() + 16; z++) {
				for (int y = minHeight; y <= maxHeight; y++) {
					blockPos.set(x, y, z);
					BlockState state = level.getBlockState(blockPos);
					if (border.isWithinBounds(blockPos) && state.is(ESBlocks.STARLIGHT_PORTAL.get()) && state.getValue(ESPortalBlock.CENTER)) {
						return Optional.of(blockPos);
					}
				}
			}
		}
		return Optional.empty();
	}

	public static Optional<BlockPos> makePortal(ServerLevel level, BlockPos blockPos, Direction.Axis axis) {
		int maxHeight = level.getMaxBuildHeight();
		int minHeight = level.getMinBuildHeight();
		WorldBorder border = level.getWorldBorder();
		for (BlockPos.MutableBlockPos pos : BlockPos.spiralAround(blockPos, 32, Direction.EAST, Direction.SOUTH)) {
			// search near world surface
			int worldSurface = level.getHeight(Heightmap.Types.WORLD_SURFACE, pos.getX(), pos.getZ());
			int from = Math.max(minHeight, worldSurface - 10);
			int to = Math.min(maxHeight, worldSurface + 10);
			if (from < to) {
				for (int height = from; height < to; height++) {
					pos.setY(height);
					if (border.isWithinBounds(pos) && ESPortalBlock.placePortal(level, pos, axis, 2)) {
						return Optional.of(pos.relative(Direction.UP));
					}
				}
			}
			// search upwards
			pos.setY(blockPos.getY());
			for (int height = blockPos.getY(); height < maxHeight; height++) {
				pos.setY(height);
				if (border.isWithinBounds(pos) && ESPortalBlock.placePortal(level, pos, axis, 2)) {
					return Optional.of(pos.relative(Direction.UP));
				}
			}
			// search downwards
			pos.setY(blockPos.getY());
			for (int height = blockPos.getY(); height > minHeight; height--) {
				pos.setY(height);
				if (border.isWithinBounds(pos) && ESPortalBlock.placePortal(level, pos, axis, 2)) {
					return Optional.of(pos.relative(Direction.UP));
				}
			}
		}
		return Optional.empty();
	}
}
