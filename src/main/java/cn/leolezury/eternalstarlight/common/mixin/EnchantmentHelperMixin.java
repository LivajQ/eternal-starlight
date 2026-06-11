package cn.leolezury.eternalstarlight.common.mixin;

import cn.leolezury.eternalstarlight.common.item.combat.ScytheItem;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {
	@Inject(method = "getSweepingDamageRatio", at = @At("RETURN"), cancellable = true)
	private static void addScytheSweep(LivingEntity entity, CallbackInfoReturnable<Float> cir) {
		ItemStack mainhand = entity.getItemInHand(InteractionHand.MAIN_HAND);
		if (mainhand.getItem() instanceof ScytheItem scythe) {
			float existing = cir.getReturnValue();
			float combined = existing + scythe.getSweep();
			cir.setReturnValue(combined);
		}
	}
}