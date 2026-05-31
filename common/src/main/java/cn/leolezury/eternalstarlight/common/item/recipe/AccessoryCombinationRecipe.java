package cn.leolezury.eternalstarlight.common.item.recipe;

import cn.leolezury.eternalstarlight.common.item.component.Accessory;
import cn.leolezury.eternalstarlight.common.registry.ESAccessories;
import cn.leolezury.eternalstarlight.common.registry.ESRecipeSerializers;
import cn.leolezury.eternalstarlight.common.util.ESAccessoryUtil;
import com.google.gson.JsonObject;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class AccessoryCombinationRecipe extends CustomRecipe {

	public AccessoryCombinationRecipe(ResourceLocation id, CraftingBookCategory category) {
		super(id, category);
	}

	@Override
	public boolean matches(CraftingContainer container, Level level) {

		long nonEmpty = 0;
		for (int i = 0; i < container.getContainerSize(); i++) {
			if (!container.getItem(i).isEmpty()) nonEmpty++;
		}
		if (nonEmpty > 2) return false;

		List<ItemStack> accessoryStacks = new ArrayList<>();
		for (int i = 0; i < container.getContainerSize(); i++) {
			ItemStack stack = container.getItem(i);
			if (ESAccessories.get(stack) != null) {
				accessoryStacks.add(stack);
			}
		}
		if (accessoryStacks.size() != 1) return false;

		ItemStack accessoryStack = accessoryStacks.get(0);
		Accessory accessory = ESAccessories.get(accessoryStack);
		if (accessory == null) return false;

		List<ItemStack> equipmentStacks = new ArrayList<>();
		for (int i = 0; i < container.getContainerSize(); i++) {
			ItemStack stack = container.getItem(i);
			if (stack.isEmpty()) continue;

			if (!stack.is(accessory.combinationTarget())) continue;

			List<ItemStack> existing = ESAccessoryUtil.getAccessoryStacks(stack);
			if (existing.size() >= ESAccessoryUtil.getAccessorySlotCount(stack)) continue;

			if (existing.stream().anyMatch(s -> s.is(accessoryStack.getItem()))) continue;

			equipmentStacks.add(stack);
		}

		return equipmentStacks.size() == 1;
	}

	@Override
	public ItemStack assemble(CraftingContainer container, RegistryAccess registryAccess) {

		List<ItemStack> accessoryStacks = new ArrayList<>();
		for (int i = 0; i < container.getContainerSize(); i++) {
			ItemStack stack = container.getItem(i);
			if (ESAccessories.get(stack) != null) {
				accessoryStacks.add(stack);
			}
		}
		if (accessoryStacks.size() != 1) return ItemStack.EMPTY;

		ItemStack accessoryStack = accessoryStacks.get(0);
		Accessory accessory = ESAccessories.get(accessoryStack);
		if (accessory == null) return ItemStack.EMPTY;

		List<ItemStack> equipmentStacks = new ArrayList<>();
		for (int i = 0; i < container.getContainerSize(); i++) {
			ItemStack stack = container.getItem(i);
			if (stack.isEmpty()) continue;

			if (!stack.is(accessory.combinationTarget())) continue;

			List<ItemStack> existing = ESAccessoryUtil.getAccessoryStacks(stack);
			if (existing.size() >= ESAccessoryUtil.getAccessorySlotCount(stack)) continue;

			if (existing.stream().anyMatch(s -> s.is(accessoryStack.getItem()))) continue;

			equipmentStacks.add(stack);
		}

		if (equipmentStacks.size() != 1) return ItemStack.EMPTY;

		ItemStack result = equipmentStacks.get(0).copy();
		ESAccessoryUtil.applyAccessory(result, accessoryStack);
		return result;
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return true;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return ESRecipeSerializers.ACCESSORY_COMBINATION.get();
	}

	public void save(Consumer<FinishedRecipe> consumer, ResourceLocation id) {
		consumer.accept(new FinishedRecipe() {
			@Override
			public void serializeRecipeData(JsonObject json) {
				json.addProperty("category", category().getSerializedName());
			}

			@Override
			public RecipeSerializer<?> getType() {
				return ESRecipeSerializers.ACCESSORY_COMBINATION.get();
			}

			@Override
			public ResourceLocation getId() {
				return id;
			}

			@Nullable
			@Override
			public JsonObject serializeAdvancement() {
				return null;
			}

			@Nullable
			@Override
			public ResourceLocation getAdvancementId() {
				return null;
			}
		});
	}
}
