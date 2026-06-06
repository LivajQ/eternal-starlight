package cn.leolezury.eternalstarlight.common.mixin;

import net.minecraft.world.entity.projectile.AbstractArrow;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(AbstractArrow.class)
public abstract class AbstractArrowMixin {

	/* TODO arrows have no weapon metadata
	@Shadow
	@Nullable
	private ItemStack firedFromWeapon;

	@Inject(method = "shotFromCrossbow", at = @At("RETURN"), cancellable = true)
	public void shotFromCrossbow(CallbackInfoReturnable<Boolean> cir) {
		if (this.firedFromWeapon != null && (BuiltInRegistries.ITEM.getKey(firedFromWeapon.getItem()).getNamespace().equals(EternalStarlight.ID) && firedFromWeapon.getItem() instanceof CrossbowItem)) {
			cir.setReturnValue(true);
		}
	}

	@Inject(method = "getWaterInertia", at = @At("RETURN"), cancellable = true)
	public void getWaterInertia(CallbackInfoReturnable<Float> cir) {
		if (this.firedFromWeapon != null && this.firedFromWeapon.is(ESItems.WILTED_CROSSBOW.get())) {
			cir.setReturnValue(0.99f);
		}
	}

	@WrapOperation(method = "tryPickup", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;add(Lnet/minecraft/world/item/ItemStack;)Z"))
	public boolean addToInventory(Inventory instance, ItemStack itemStack, Operation<Boolean> original) {
		if (itemStack.is(ItemTags.ARROWS)) {
			boolean arrowSuccess = GalacticQuiverItem.addArrowToInventory(instance, itemStack);
			return arrowSuccess || original.call(instance, itemStack);
		}
		return original.call(instance, itemStack);
	}

	@WrapOperation(method = "doKnockback", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/Vec3;scale(D)Lnet/minecraft/world/phys/Vec3;"))
	public Vec3 scaleKnockback(Vec3 instance, double scale, Operation<Vec3> original) {
		if (this.firedFromWeapon != null && this.firedFromWeapon.is(ESItems.UNREALIUM_CROSSBOW.get())) {
			return original.call(instance, scale * 0.5);
		}
		return original.call(instance, scale);
	}
	 */
}
