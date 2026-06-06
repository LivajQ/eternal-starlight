package cn.leolezury.eternalstarlight.common.mixin;

import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

// to avoid "assigning weaker modifier" spam on vanilla classes that AT caused
@Mixin(AbstractArrow.class)
public interface AbstractArrowAccessor {
	@Invoker("getPickupItem")
	ItemStack invokeGetPickupItem();
}