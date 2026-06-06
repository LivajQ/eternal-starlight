package cn.leolezury.eternalstarlight.common.item.misc;

import cn.leolezury.eternalstarlight.common.entity.projectile.AetherstrikeRocketEntity;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

// Copied from vanilla FireworkRocketItem
public class AetherstrikeRocketItem extends Item {
	public AetherstrikeRocketItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		Level level = context.getLevel();
		if (!level.isClientSide) {
			ItemStack stack = context.getItemInHand();
			Vec3 pos = context.getClickLocation();
			Direction dir = context.getClickedFace();

			AetherstrikeRocketEntity rocket = new AetherstrikeRocketEntity(
				level,
				context.getPlayer(),
				pos.x + dir.getStepX() * 0.15,
				pos.y + dir.getStepY() * 0.15,
				pos.z + dir.getStepZ() * 0.15,
				stack
			);

			level.addFreshEntity(rocket);
			stack.shrink(1);
		}

		return InteractionResult.sidedSuccess(level.isClientSide);
	}
}

