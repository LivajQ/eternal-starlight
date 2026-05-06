package cn.leolezury.eternalstarlight.common.item.recipe;

import cn.leolezury.eternalstarlight.common.EternalStarlight;
import cn.leolezury.eternalstarlight.common.registry.ESItems;
import cn.leolezury.eternalstarlight.common.registry.ESRecipeSerializers;
import cn.leolezury.eternalstarlight.common.registry.ESRecipes;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

public record AlloyRecipe(NonNullList<Result> results, NonNullList<Ingredient> ingredients, int burnTime) implements Recipe<Container> {

	@Override
	public RecipeSerializer<?> getSerializer() {
		return ESRecipeSerializers.ALLOY.get();
	}

	@Override
	public RecipeType<?> getType() {
		return ESRecipes.ALLOY.get();
	}

	public static class Type implements RecipeType<AlloyRecipe> {
		public static final ResourceLocation ID = EternalStarlight.id("alloy");

		@Override
		public String toString() {
			return ID.toString();
		}
	}

	@Override
	public ItemStack getToastSymbol() {
		return ESItems.ALLOY_FURNACE.get().getDefaultInstance();
	}

	@Override
	public ResourceLocation getId() {
		return ESRecipes.ALLOY.getId();
	}

	@Override
	public ItemStack getResultItem(RegistryAccess registryAccess) {
		return results.getFirst().item();
	}

	@Override
	public NonNullList<Ingredient> getIngredients() {
		return this.ingredients;
	}

	@Override
	public boolean matches(Container container, Level level) {
		StackedContents contents = new StackedContents();
		int count = 0;

		for (int i = 0; i < container.getContainerSize(); ++i) {
			ItemStack stack = container.getItem(i);
			if (!stack.isEmpty()) {
				++count;
				contents.accountStack(stack, 1);
			}
		}

		return count == this.ingredients.size() && contents.canCraft(this, null);
	}

	@Override
	public ItemStack assemble(Container container, RegistryAccess access) {
		return this.results.getFirst().item().copy();
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return width * height >= this.ingredients.size();
	}

	@Override
	public boolean isSpecial() {
		return true;
	}

	public record Result(ItemStack item, IntProvider amount) {

		public ItemStack getResultItem(RandomSource random) {
			return item().copyWithCount(amount().sample(random));
		}

		public ItemStack getMaxResultItem() {
			return item().copyWithCount(amount().getMaxValue());
		}
	}

	public static class Serializer implements RecipeSerializer<AlloyRecipe> {

		@Override
		public AlloyRecipe fromJson(ResourceLocation id, JsonObject json) {
			JsonArray resultsArray = GsonHelper.getAsJsonArray(json, "results");
			NonNullList<Result> results = NonNullList.create();
			for (JsonElement elem : resultsArray) {
				JsonObject obj = elem.getAsJsonObject();
				ItemStack stack = net.minecraft.world.item.crafting.ShapedRecipe.itemStackFromJson(
					GsonHelper.getAsJsonObject(obj, "item")
				);
				int min = GsonHelper.getAsInt(obj, "amount_min", 1);
				int max = GsonHelper.getAsInt(obj, "amount_max", min);
				results.add(new Result(stack, min == max ? ConstantInt.of(min) : net.minecraft.util.valueproviders.UniformInt.of(min, max)));
			}

			JsonArray ingredientsArray = GsonHelper.getAsJsonArray(json, "ingredients");
			NonNullList<Ingredient> ingredients = NonNullList.create();
			for (JsonElement elem : ingredientsArray) {
				ingredients.add(Ingredient.fromJson(elem));
			}

			int burnTime = GsonHelper.getAsInt(json, "burn_time");

			return new AlloyRecipe(results, ingredients, burnTime);
		}

		@Override
		public AlloyRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
			int resultSize = buf.readVarInt();
			NonNullList<Result> results = NonNullList.withSize(resultSize, new Result(ItemStack.EMPTY, ConstantInt.of(1)));
			for (int i = 0; i < resultSize; i++) {
				ItemStack item = buf.readItem();
				int min = buf.readVarInt();
				int max = buf.readVarInt();
				IntProvider amount = (min == max) ? ConstantInt.of(min) : net.minecraft.util.valueproviders.UniformInt.of(min, max);
				results.set(i, new Result(item, amount));
			}

			int ingredientSize = buf.readVarInt();
			NonNullList<Ingredient> ingredients = NonNullList.withSize(ingredientSize, Ingredient.EMPTY);
			for (int i = 0; i < ingredientSize; i++) {
				ingredients.set(i, Ingredient.fromNetwork(buf));
			}

			int burnTime = buf.readInt();
			return new AlloyRecipe(results, ingredients, burnTime);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buf, AlloyRecipe recipe) {
			buf.writeVarInt(recipe.results.size());
			for (Result result : recipe.results) {
				buf.writeItem(result.item());
				int min = result.amount().getMinValue();
				int max = result.amount().getMaxValue();
				buf.writeVarInt(min);
				buf.writeVarInt(max);
			}

			buf.writeVarInt(recipe.ingredients.size());
			for (Ingredient ingredient : recipe.ingredients) {
				ingredient.toNetwork(buf);
			}

			buf.writeInt(recipe.burnTime);
		}
	}
}
