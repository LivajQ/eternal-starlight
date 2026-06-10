package cn.leolezury.eternalstarlight.common.world;

import cn.leolezury.eternalstarlight.common.block.ESPortalBlock;
import cn.leolezury.eternalstarlight.common.registry.ESBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.ITeleporter;

import java.util.Optional;
import java.util.function.Function;

public class ESTeleporter implements ITeleporter {
	private final BlockPos target;
	private final boolean valid;

	public ESTeleporter(Entity entity, BlockPos entrancePos, ServerLevel destLevel) {
		WorldBorder border = destLevel.getWorldBorder();
		double scale = DimensionType.getTeleportationScale(
			entity.level().dimensionType(), destLevel.dimensionType()
		);
		BlockPos scaledPos = new BlockPos(
			(int) Math.max(border.getMinX(), Math.min(border.getMaxX(), entity.getX() * scale)),
			(int) entity.getY(),
			(int) Math.max(border.getMinZ(), Math.min(border.getMaxZ(), entity.getZ() * scale))
		);
		Direction.Axis axis = entity.level().getBlockState(entrancePos)
			.getOptionalValue(ESPortalBlock.AXIS).orElse(Direction.Axis.X);

		Optional<BlockPos> result = getOrMakePortal(destLevel, scaledPos, axis);
		this.target = result.orElse(null);
		this.valid = result.isPresent();
	}

	public boolean isValid() {
		return valid;
	}

	@Override
	public PortalInfo getPortalInfo(Entity entity, ServerLevel destLevel, Function<ServerLevel, PortalInfo> defaultPortalInfo) {
		if (target == null) return null;
		return new PortalInfo(Vec3.atCenterOf(target), Vec3.ZERO, entity.getYRot(), entity.getXRot());
	}

	@Override
	public Entity placeEntity(Entity entity, ServerLevel currentWorld, ServerLevel destWorld, float yaw, Function<Boolean, Entity> repositionEntity) {
		return repositionEntity.apply(false);
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
						return Optional.of(blockPos.immutable());
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
			int worldSurface = level.getHeight(Heightmap.Types.WORLD_SURFACE, pos.getX(), pos.getZ());
			int from = Math.max(minHeight, worldSurface - 10);
			int to = Math.min(maxHeight, worldSurface + 10);
			if (from < to) {
				for (int height = from; height < to; height++) {
					pos.setY(height);
					if (border.isWithinBounds(pos) && ESPortalBlock.placePortal(level, pos, axis, 2)) {
						return Optional.of(pos.relative(Direction.UP).immutable());
					}
				}
			}
			pos.setY(blockPos.getY());
			for (int height = blockPos.getY(); height < maxHeight; height++) {
				pos.setY(height);
				if (border.isWithinBounds(pos) && ESPortalBlock.placePortal(level, pos, axis, 2)) {
					return Optional.of(pos.relative(Direction.UP).immutable());
				}
			}
			pos.setY(blockPos.getY());
			for (int height = blockPos.getY(); height > minHeight; height--) {
				pos.setY(height);
				if (border.isWithinBounds(pos) && ESPortalBlock.placePortal(level, pos, axis, 2)) {
					return Optional.of(pos.relative(Direction.UP).immutable());
				}
			}
		}
		return Optional.empty();
	}

	public static Optional<BlockPos> getOrMakePortal(ServerLevel level, BlockPos pos, Direction.Axis axis) {
		return getExistingPortal(level, pos).or(() -> makePortal(level, pos, axis));
	}

	public BlockPos getTarget() {
		return target;
	}
}