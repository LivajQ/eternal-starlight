package cn.leolezury.eternalstarlight.common.mixin;

import net.minecraft.world.item.DiggerItem;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(DiggerItem.class)
public abstract class DiggerItemMixin {
	/*
	@WrapOperation(method = "postHurtEnemy", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;hurtAndBreak(ILnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/EquipmentSlot;)V"))
	private void hurtAndBreak(ItemStack instance, int damage, LivingEntity livingEntity, EquipmentSlot slot, Operation<Void> original) {
		if (ESAccessoryUtil.getAccessories(instance).contains(ESItems.BATTLEAXE_PENDANT.get())) {
			original.call(instance, 1, livingEntity, slot);
		} else {
			original.call(instance, damage, livingEntity, slot);
		}
	}
	 */
}
