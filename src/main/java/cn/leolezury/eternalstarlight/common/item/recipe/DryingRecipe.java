package cn.leolezury.eternalstarlight.common.item.recipe;

import cn.leolezury.eternalstarlight.common.EternalStarlight;
import cn.leolezury.eternalstarlight.common.registry.ESRecipeSerializers;
import cn.leolezury.eternalstarlight.common.registry.ESRecipes;
import com.google.gson.JsonObject;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class DryingRecipe implements Recipe<Container> {
	private final Ingredient input;
	private final ItemStack output;
	private final int durationTicks;
	private final boolean fireBelow;
	private final ResourceLocation id;

	public DryingRecipe(ResourceLocation id, Ingredient input, ItemStack output, int durationTicks, boolean fireBelow) {
		this.input = input;
		this.output = output;
		this.durationTicks = durationTicks;
		this.fireBelow = fireBelow;
		this.id = id;
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
		return id;
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
			return new DryingRecipe(id, input, output, duration, fireBelow);
		}

		@Override
		public DryingRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
			Ingredient input = Ingredient.fromNetwork(buf);
			ItemStack output = buf.readItem();
			int duration = buf.readInt();
			boolean fireBelow = buf.readBoolean();
			return new DryingRecipe(id, input, output, duration, fireBelow);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buf, DryingRecipe recipe) {
			recipe.input.toNetwork(buf);
			buf.writeItem(recipe.output);
			buf.writeInt(recipe.durationTicks);
			buf.writeBoolean(recipe.fireBelow);
		}
	}

	private static JsonObject serializeItemStack(ItemStack stack) {
		JsonObject json = new JsonObject();
		json.addProperty("item", BuiltInRegistries.ITEM.getKey(stack.getItem()).toString());
		if (stack.getCount() > 1) json.addProperty("count", stack.getCount());
		return json;
	}

	public void save(Consumer<FinishedRecipe> consumer, ResourceLocation id) {
		consumer.accept(new FinishedRecipe() {
			@Override
			public void serializeRecipeData(JsonObject json) {
				json.add("input", input.toJson());
				json.add("output", serializeItemStack(output));
				json.addProperty("duration_ticks", durationTicks);
				json.addProperty("fire_below", fireBelow);
			}

			@Override
			public RecipeSerializer<?> getType() {
				return ESRecipeSerializers.DRYING.get();
			}

			@Override
			public ResourceLocation getId() { return id; }

			@Nullable @Override
			public JsonObject serializeAdvancement() { return null; }

			@Nullable
			@Override
			public ResourceLocation getAdvancementId() { return null; }
		});
	}
}
