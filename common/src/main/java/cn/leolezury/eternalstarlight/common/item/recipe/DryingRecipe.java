package cn.leolezury.eternalstarlight.common.item.recipe;

import cn.leolezury.eternalstarlight.common.EternalStarlight;
import cn.leolezury.eternalstarlight.common.registry.ESRecipeSerializers;
import cn.leolezury.eternalstarlight.common.registry.ESRecipes;
import com.google.gson.JsonObject;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

public class DryingRecipe implements Recipe<Container> {
	private final Ingredient input;
	private final ItemStack output;
	private final int durationTicks;
	private final boolean fireBelow;

	public DryingRecipe(Ingredient input, ItemStack output, int durationTicks, boolean fireBelow) {
		this.input = input;
		this.output = output;
		this.durationTicks = durationTicks;
		this.fireBelow = fireBelow;
	}

	public Ingredient input() {
		return input;
	}

	public ItemStack output() {
		return output;
	}

	public int durationTicks() {
		return durationTicks;
	}

	public boolean fireBelow() {
		return fireBelow;
	}

	@Override
	public boolean matches(Container container, Level level) {
		ItemStack stack = container.getItem(0);
		return input.test(stack);
	}

	@Override
	public boolean canCraftInDimensions(int w, int h) {
		return true;
	}

	@Override
	public ItemStack assemble(Container container, RegistryAccess registryAccess) {
		return output.copy();
	}

	@Override
	public ItemStack getResultItem(RegistryAccess registryAccess) {
		return output;
	}

	@Override
	public ResourceLocation getId() {
		return ESRecipes.DRYING.getId();
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return ESRecipeSerializers.DRYING.get();
	}

	@Override
	public RecipeType<?> getType() {
		return ESRecipes.DRYING.get();
	}

	public static class Type implements RecipeType<DryingRecipe> {
		public static final ResourceLocation ID = EternalStarlight.id("drying");

		@Override
		public String toString() {
			return ID.toString();
		}
	}

	public static class Serializer implements RecipeSerializer<DryingRecipe> {

		@Override
		public DryingRecipe fromJson(ResourceLocation id, JsonObject json) {
			Ingredient input = Ingredient.fromJson(json.get("input"));
			ItemStack output = ShapedRecipe.itemStackFromJson(json.getAsJsonObject("output"));
			int duration = GsonHelper.getAsInt(json, "duration_ticks");
			boolean fireBelow = GsonHelper.getAsBoolean(json, "fire_below");
			return new DryingRecipe(input, output, duration, fireBelow);
		}

		@Override
		public DryingRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
			Ingredient input = Ingredient.fromNetwork(buf);
			ItemStack output = buf.readItem();
			int duration = buf.readInt();
			boolean fireBelow = buf.readBoolean();
			return new DryingRecipe(input, output, duration, fireBelow);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buf, DryingRecipe recipe) {
			recipe.input.toNetwork(buf);
			buf.writeItem(recipe.output);
			buf.writeInt(recipe.durationTicks);
			buf.writeBoolean(recipe.fireBelow);
		}
	}
}
