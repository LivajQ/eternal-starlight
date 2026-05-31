package cn.leolezury.eternalstarlight.common.item.recipe;

import cn.leolezury.eternalstarlight.common.registry.ESRecipeSerializers;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class ToolModificationRecipe extends CustomRecipe {
	private final Item tool;
	private final Item input;
	private final ItemStack output;

	public ToolModificationRecipe(ResourceLocation id, CraftingBookCategory category, Item tool, Item input, ItemStack output) {
		super(id, category);
		this.tool = tool;
		this.input = input;
		this.output = output;
	}

	@Override
	public boolean matches(CraftingContainer container, Level level) {
		long toolCount = 0;
		long inputCount = 0;

		for (int i = 0; i < container.getContainerSize(); i++) {
			ItemStack stack = container.getItem(i);

			if (stack.isEmpty()) continue;

			if (stack.is(tool)) toolCount++;
			else if (stack.is(input)) inputCount++;
			else return false;
		}

		return toolCount == 1 && inputCount == 1;
	}

	@Override
	public ItemStack assemble(CraftingContainer container, RegistryAccess access) {
		return output.copy();
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(CraftingContainer container) {
		NonNullList<ItemStack> items = NonNullList.withSize(container.getContainerSize(), ItemStack.EMPTY);

		for (int i = 0; i < container.getContainerSize(); i++) {
			ItemStack stack = container.getItem(i);

			if (stack.is(tool)) {
				ItemStack remaining = stack.copy();
				if (remaining.getDamageValue() + 1 < remaining.getMaxDamage()) {
					remaining.setDamageValue(remaining.getDamageValue() + 1);
					items.set(i, remaining);
				}
			}
		}

		return items;
	}

	@Override
	public boolean canCraftInDimensions(int w, int h) {
		return true;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return ESRecipeSerializers.TOOL_MODIFICATION.get();
	}

	public static class Serializer implements RecipeSerializer<ToolModificationRecipe> {

		@Override
		public ToolModificationRecipe fromJson(ResourceLocation id, JsonObject json) {
			CraftingBookCategory category = CraftingBookCategory.CODEC.byName(
				GsonHelper.getAsString(json, "category", "misc")
			);

			Item tool = BuiltInRegistries.ITEM.get(new ResourceLocation(GsonHelper.getAsString(json, "tool")));
			Item input = BuiltInRegistries.ITEM.get(new ResourceLocation(GsonHelper.getAsString(json, "input")));
			ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "output"));

			return new ToolModificationRecipe(id, category, tool, input, output);
		}

		@Override
		public ToolModificationRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
			CraftingBookCategory category = buf.readEnum(CraftingBookCategory.class);
			Item tool = buf.readById(BuiltInRegistries.ITEM);
			Item input = buf.readById(BuiltInRegistries.ITEM);
			ItemStack output = buf.readItem();

			return new ToolModificationRecipe(id, category, tool, input, output);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buf, ToolModificationRecipe recipe) {
			buf.writeEnum(recipe.category());
			buf.writeId(BuiltInRegistries.ITEM, recipe.tool);
			buf.writeId(BuiltInRegistries.ITEM, recipe.input);
			buf.writeItem(recipe.output);
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
				json.addProperty("category", category().getSerializedName());
				json.addProperty("tool", BuiltInRegistries.ITEM.getKey(tool).toString());
				json.addProperty("input", BuiltInRegistries.ITEM.getKey(input).toString());
				json.add("output", serializeItemStack(output));
			}

			@Override
			public RecipeSerializer<?> getType() {
				return ESRecipeSerializers.TOOL_MODIFICATION.get();
			}

			@Override
			public ResourceLocation getId() { return id; }

			@Nullable
			@Override
			public JsonObject serializeAdvancement() { return null; }

			@Nullable @Override
			public ResourceLocation getAdvancementId() { return null; }
		});
	}
}
