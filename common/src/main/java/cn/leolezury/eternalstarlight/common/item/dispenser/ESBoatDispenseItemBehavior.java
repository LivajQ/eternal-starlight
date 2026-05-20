package cn.leolezury.eternalstarlight.common.item.dispenser;

import cn.leolezury.eternalstarlight.common.entity.misc.ESBoat;
import cn.leolezury.eternalstarlight.common.entity.misc.ESChestBoat;
import cn.leolezury.eternalstarlight.common.platform.ESPlatform;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.phys.Vec3;

public class ESBoatDispenseItemBehavior extends DefaultDispenseItemBehavior {
	private final DefaultDispenseItemBehavior fallback = new DefaultDispenseItemBehavior();
	private final ESBoat.Type type;
	private final boolean isChestBoat;

	public ESBoatDispenseItemBehavior(ESBoat.Type type) {
		this(type, false);
	}

	public ESBoatDispenseItemBehavior(ESBoat.Type type, boolean isChestBoat) {
		this.type = type;
		this.isChestBoat = isChestBoat;
	}

	@Override
	public ItemStack execute(BlockSource source, ItemStack stack) {
		Direction dir = source.getBlockState().getValue(DispenserBlock.FACING);
		ServerLevel level = source.getLevel();

		Vec3 center = new Vec3(
			source.getPos().getX() + 0.5,
			source.getPos().getY() + 0.5,
			source.getPos().getZ() + 0.5
		);

		double offset = 0.5625 + EntityType.BOAT.getWidth() / 2.0;
		double x = center.x + dir.getStepX() * offset;
		double y = center.y + dir.getStepY() * 1.125F;
		double z = center.z + dir.getStepZ() * offset;

		BlockPos frontPos = source.getPos().relative(dir);

		ESBoat boat = this.isChestBoat
			? new ESChestBoat(level, x, y, z)
			: new ESBoat(level, x, y, z);

		EntityType.<ESBoat>createDefaultStackConfig(level, stack, null).accept(boat);
		boat.setStarlightBoatType(this.type);
		boat.setYRot(dir.toYRot());

		double yOffset;

		if (ESPlatform.INSTANCE.canBoatInFluid(boat, level.getFluidState(frontPos))) {
			yOffset = 1.0;
		} else {
			if (!level.getBlockState(frontPos).isAir()
				|| !ESPlatform.INSTANCE.canBoatInFluid(boat, level.getFluidState(frontPos.below()))) {
				return fallback.dispense(source, stack);
			}
			yOffset = 0.0;
		}

		boat.setPos(x, y + yOffset, z);
		level.addFreshEntity(boat);

		stack.shrink(1);
		return stack;
	}

	@Override
	protected void playSound(BlockSource source) {
		source.getLevel().levelEvent(1000, source.getPos(), 0);
	}
}
