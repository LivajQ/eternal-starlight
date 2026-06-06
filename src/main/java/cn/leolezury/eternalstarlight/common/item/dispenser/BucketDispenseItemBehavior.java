package cn.leolezury.eternalstarlight.common.item.dispenser;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.world.item.DispensibleContainerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;

public class BucketDispenseItemBehavior extends DefaultDispenseItemBehavior {
	private final DefaultDispenseItemBehavior fallback = new DefaultDispenseItemBehavior();

	@Override
	public ItemStack execute(BlockSource source, ItemStack stack) {
		Item item = stack.getItem();
		if (!(item instanceof DispensibleContainerItem bucket)) {
			return fallback.dispense(source, stack);
		}

		Level level = source.getLevel();
		Direction dir = source.getBlockState().getValue(DispenserBlock.FACING);
		BlockPos pos = source.getPos().relative(dir);

		if (bucket.emptyContents(null, level, pos, null)) {
			bucket.checkExtraContent(null, level, stack, pos);

			stack.shrink(1);
			return new ItemStack(Items.BUCKET);
		}

		return fallback.dispense(source, stack);
	}
}
