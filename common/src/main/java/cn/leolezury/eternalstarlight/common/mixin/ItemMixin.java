package cn.leolezury.eternalstarlight.common.mixin;

import cn.leolezury.eternalstarlight.common.registry.ESFoods;
import cn.leolezury.eternalstarlight.common.registry.ESItems;
import cn.leolezury.eternalstarlight.common.util.ESAccessoryUtil;
import cn.leolezury.eternalstarlight.common.util.ESTags;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Item.class)
public abstract class ItemMixin {

	@WrapOperation(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;canEat(Z)Z"))
	private boolean wrapCanEat(Player player, boolean original, Operation<Boolean> op) {
		if (ESAccessoryUtil.getActiveAccessoriesOnArmors(player).contains(ESItems.FUNGUS_AMULET.get()) && ((Item)(Object)this).builtInRegistryHolder().is(ESTags.Items.CONSUMABLE_WHEN_WEARING_FUNGUS_AMULET)) {
			return true;
		}
		return op.call(player, original);
	}

	@WrapOperation(method = {"use", "finishUsingItem"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getFoodProperties(Lnet/minecraft/world/entity/LivingEntity;)Lnet/minecraft/world/food/FoodProperties;"))
	private FoodProperties wrapFoodProperties(ItemStack stack, LivingEntity entity, Operation<FoodProperties> original) {
		FoodProperties food = original.call(stack, entity);

		if (food == null
			&& ESAccessoryUtil.getActiveAccessoriesOnArmors(entity).contains(ESItems.FUNGUS_AMULET.get())
			&& stack.is(ESTags.Items.CONSUMABLE_WHEN_WEARING_FUNGUS_AMULET)) {

			return ESFoods.FUNGUS.get();
		}

		return food;
	}
}
