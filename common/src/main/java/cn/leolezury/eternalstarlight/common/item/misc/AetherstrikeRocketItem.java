package cn.leolezury.eternalstarlight.common.item.misc;

import cn.leolezury.eternalstarlight.common.entity.projectile.AetherstrikeRocketEntity;
import cn.leolezury.eternalstarlight.common.registry.ESEntities;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

// Copied from vanilla FireworkRocketItem
public class AetherstrikeRocketItem extends Item implements ProjectileItem {
	public AetherstrikeRocketItem(Item.Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		Level level = context.getLevel();
		if (!level.isClientSide) {
			ItemStack itemStack = context.getItemInHand();
			Vec3 vec3 = context.getClickLocation();
			Direction direction = context.getClickedFace();
			AetherstrikeRocketEntity rocket = new AetherstrikeRocketEntity(level, context.getPlayer(), vec3.x + (double) direction.getStepX() * 0.15, vec3.y + (double) direction.getStepY() * 0.15, vec3.z + (double) direction.getStepZ() * 0.15, itemStack);
			level.addFreshEntity(rocket);
			itemStack.shrink(1);
		}

		return InteractionResult.sidedSuccess(level.isClientSide);
	}

	@Override
	public Projectile asProjectile(Level level, Position position, ItemStack itemStack, Direction direction) {
		return new AetherstrikeRocketEntity(level, itemStack.copyWithCount(1), position.x(), position.y(), position.z(), true);
	}

	@Override
	public ProjectileItem.DispenseConfig createDispenseConfig() {
		return DispenseConfig.builder().positionFunction(AetherstrikeRocketItem::getEntityPokingOutOfBlockPos).uncertainty(1.0F).power(0.5F).overrideDispenseEvent(1004).build();
	}

	private static Vec3 getEntityPokingOutOfBlockPos(BlockSource blockSource, Direction direction) {
		return blockSource.center().add(direction.getStepX() * (0.5 - ESEntities.AETHERSTRIKE_ROCKET.get().getWidth() / 2.0), direction.getStepY() * (0.5 - ESEntities.AETHERSTRIKE_ROCKET.get().getHeight() / 2.0) - ESEntities.AETHERSTRIKE_ROCKET.get().getHeight() / 2.0, direction.getStepZ() * (0.5 - ESEntities.AETHERSTRIKE_ROCKET.get().getWidth() / 2.0));
	}
}
