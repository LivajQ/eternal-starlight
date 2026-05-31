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
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public record GeyserSmokingRecipe(Item input, int inputCount, ItemStack output) implements Recipe<Container> {

	@Override
	public boolean matches(Container container, Level level) {
		return container.getItem(0).is(input) && container.getItem(0).getCount() >= inputCount;
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
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
		return ESRecipes.GEYSER_SMOKING.getId();
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return ESRecipeSerializers.GEYSER_SMOKING.get();
	}

	@Override
	public RecipeType<?> getType() {
		return ESRecipes.GEYSER_SMOKING.get();
	}

	public static class Type implements RecipeType<GeyserSmokingRecipe> {
		public static final ResourceLocation ID = EternalStarlight.id("geyser_smoking");

		@Override
		public String toString() {
			return ID.toString();
		}
	}

	public static class Serializer implements RecipeSerializer<GeyserSmokingRecipe> {

		@Override
		public GeyserSmokingRecipe fromJson(ResourceLocation id, JsonObject json) {
			Item input = BuiltInRegistries.ITEM.get(new ResourceLocation(json.get("input").getAsString()));
			int inputCount = json.get("input_count").getAsInt();
			ItemStack output = ShapedRecipe.itemStackFromJson(json.getAsJsonObject("output"));
			return new GeyserSmokingRecipe(input, inputCount, output);
		}

		@Override
		public GeyserSmokingRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
			Item input = buf.readById(BuiltInRegistries.ITEM);
			int inputCount = buf.readInt();
			ItemStack output = buf.readItem();
			return new GeyserSmokingRecipe(input, inputCount, output);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buf, GeyserSmokingRecipe recipe) {
			buf.writeId(BuiltInRegistries.ITEM, recipe.input());
			buf.writeInt(recipe.inputCount());
			buf.writeItem(recipe.output());
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
				json.addProperty("input", BuiltInRegistries.ITEM.getKey(input).toString());
				json.addProperty("input_count", inputCount);
				json.add("output", serializeItemStack(output));
			}

			@Override
			public RecipeSerializer<?> getType() {
				return ESRecipeSerializers.GEYSER_SMOKING.get();
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
