package cn.leolezury.eternalstarlight.common.mixin;

import cn.leolezury.eternalstarlight.common.item.component.LargeItemStackList;
import cn.leolezury.eternalstarlight.common.item.misc.GalacticQuiverItem;
import cn.leolezury.eternalstarlight.common.registry.ESItems;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Predicate;

@Mixin(ProjectileWeaponItem.class)
public abstract class ProjectileWeaponItemMixin {
	@Inject(method = "getHeldProjectile", at = @At("RETURN"), cancellable = true)
	private static void getHeldProjectile(LivingEntity livingEntity, Predicate<ItemStack> predicate, CallbackInfoReturnable<ItemStack> cir) {
		if (!cir.getReturnValue().isEmpty() || !(livingEntity instanceof Player player)) return;

		Inventory inv = player.getInventory();

		for (int i = 0; i < inv.getContainerSize(); i++) {
			ItemStack quiver = inv.getItem(i);

			if (quiver.is(ESItems.GALACTIC_QUIVER.get())) {

				LargeItemStackList arrows = GalacticQuiverItem.getArrows(quiver);

				for (LargeItemStackList.LargeItemStack arrow : arrows) {

					if (predicate.test(arrow.getItem())) {
						ItemStack ammo = arrow.getItem().copy();
						ammo.getOrCreateTag().putBoolean("FromQuiver", true);

						cir.setReturnValue(ammo);
						return;
					}
				}
			}
		}
	}

	/* TODO combine with the fix for AbstractArrowMixin
	@Inject(method = "useAmmo", at = @At("RETURN"))
	private static void useAmmo(ItemStack weapon, ItemStack ammo, LivingEntity shooter, boolean intangible, CallbackInfoReturnable<ItemStack> cir, @Local(ordinal = 0) int ammoUse) {
		int use = ammoUse;
		ItemStack result = cir.getReturnValue();

		if (!(shooter instanceof Player player)) return;
		if (use <= 0) return;

		if (!result.hasTag() || !result.getTag().getBoolean("FromQuiver")) return;

		Inventory inv = player.getInventory();

		for (int i = 0; i < inv.getContainerSize(); i++) {
			ItemStack quiver = inv.getItem(i);

			if (quiver.is(ESItems.GALACTIC_QUIVER.get())) {
				LargeItemStackList list = GalacticQuiverItem.getArrows(quiver);
				List<LargeItemStackList.LargeItemStack> arrows = new ArrayList<>(list);

				for (LargeItemStackList.LargeItemStack arrowStack : arrows) {
					ItemStack compare = arrowStack.getItem().copy();
					compare.getOrCreateTag().putBoolean("FromQuiver", true);

					if (ItemStack.isSameItemSameTags(compare, result)) {

						int shrink = Math.min(use, arrowStack.getCount());
						arrowStack.shrink(shrink);
						use -= shrink;

						if (use <= 0) break;
					}
				}

				arrows.removeIf(LargeItemStackList.LargeItemStack::isEmpty);
				GalacticQuiverItem.setArrows(quiver, new LargeItemStackList(Collections.unmodifiableList(arrows)));

				if (player instanceof ServerPlayer sp) {
					sp.connection.send(new ClientboundContainerSetSlotPacket(
						ClientboundContainerSetSlotPacket.PLAYER_INVENTORY, 0, i, quiver
					));
				}
			}
		}
	}
	 */
}
