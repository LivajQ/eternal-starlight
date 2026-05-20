package cn.leolezury.eternalstarlight.common.item.misc;

import cn.leolezury.eternalstarlight.common.entity.misc.TearBombMinecart;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

public class TearBombMinecartItem extends Item {
	private static final DispenseItemBehavior DISPENSE_ITEM_BEHAVIOR = new DefaultDispenseItemBehavior() {
		private final DefaultDispenseItemBehavior fallback = new DefaultDispenseItemBehavior();

		@Override
		public ItemStack execute(BlockSource source, ItemStack stack) {
			Direction dir = source.getBlockState().getValue(DispenserBlock.FACING);
			ServerLevel level = source.getLevel();

			Vec3 center = new Vec3(
				source.getPos().getX() + 0.5,
				source.getPos().getY() + 0.5,
				source.getPos().getZ() + 0.5
			);

			double x = center.x + dir.getStepX() * 1.125F;
			double y = Math.floor(center.y) + dir.getStepY();
			double z = center.z + dir.getStepZ() * 1.125F;

			BlockPos frontPos = source.getPos().relative(dir);
			BlockState state = level.getBlockState(frontPos);

			RailShape shape = state.getBlock() instanceof BaseRailBlock
				? state.getValue(((BaseRailBlock) state.getBlock()).getShapeProperty())
				: RailShape.NORTH_SOUTH;

			double yOffset;

			if (state.is(BlockTags.RAILS)) {
				yOffset = shape.isAscending() ? 0.6 : 0.1;
			} else {
				if (!state.isAir() || !level.getBlockState(frontPos.below()).is(BlockTags.RAILS)) {
					return fallback.dispense(source, stack);
				}

				BlockState below = level.getBlockState(frontPos.below());
				RailShape belowShape = below.getBlock() instanceof BaseRailBlock
					? below.getValue(((BaseRailBlock) below.getBlock()).getShapeProperty())
					: RailShape.NORTH_SOUTH;

				if (dir != Direction.DOWN && belowShape.isAscending()) {
					yOffset = -0.4;
				} else {
					yOffset = -0.9;
				}
			}

			AbstractMinecart minecart = new TearBombMinecart(level, x, y + yOffset, z);
			level.addFreshEntity(minecart);

			stack.shrink(1);
			return stack;
		}

		@Override
		protected void playSound(BlockSource source) {
			source.getLevel().levelEvent(1000, source.getPos(), 0);
		}
	};

	public TearBombMinecartItem(Properties properties) {
		super(properties);
		DispenserBlock.registerBehavior(this, DISPENSE_ITEM_BEHAVIOR);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		Level level = context.getLevel();
		BlockPos pos = context.getClickedPos();
		BlockState state = level.getBlockState(pos);

		if (!state.is(BlockTags.RAILS)) {
			return InteractionResult.FAIL;
		}

		ItemStack stack = context.getItemInHand();

		if (level instanceof ServerLevel serverLevel) {
			RailShape shape = state.getBlock() instanceof BaseRailBlock
				? state.getValue(((BaseRailBlock) state.getBlock()).getShapeProperty())
				: RailShape.NORTH_SOUTH;

			double yOffset = shape.isAscending() ? 0.5F : 0.0F;

			TearBombMinecart minecart = new TearBombMinecart(
				serverLevel,
				pos.getX() + 0.5F,
				pos.getY() + 0.0625F + yOffset,
				pos.getZ() + 0.5F
			);

			serverLevel.addFreshEntity(minecart);
			serverLevel.gameEvent(GameEvent.ENTITY_PLACE, pos,
				GameEvent.Context.of(context.getPlayer(), serverLevel.getBlockState(pos.below())));
		}

		stack.shrink(1);
		return InteractionResult.sidedSuccess(level.isClientSide);
	}
}
