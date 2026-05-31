package cn.leolezury.eternalstarlight.common.item.recipe;

import cn.leolezury.eternalstarlight.common.registry.ESRecipeSerializers;
import cn.leolezury.eternalstarlight.common.spell.ManaType;
import cn.leolezury.eternalstarlight.common.util.ESTags;
import com.google.gson.JsonObject;
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
import java.util.List;
import java.util.function.Consumer;

public class ManaCrystalRecipe extends CustomRecipe {
	private final ManaType manaType;
	private final Item manaCrystal;

	public ManaCrystalRecipe(ResourceLocation id, CraftingBookCategory category, ManaType type, Item item) {
		super(id, category);
		this.manaType = type;
		this.manaCrystal = item;
	}

	@Override
	public boolean matches(CraftingContainer container, Level level) {
		int day = (int)(level.getDayTime() / 24000L);

		if (container.getWidth() == 3 && container.getHeight() == 3) {
			boolean checkDay = day % 6 == List.of(ManaType.values()).indexOf(manaType) - 1;

			boolean checkEmpty =
				container.getItem(0).isEmpty() &&
					container.getItem(2).isEmpty() &&
					container.getItem(6).isEmpty() &&
					container.getItem(8).isEmpty();

			boolean checkIngredients =
				container.getItem(1).is(ESTags.Items.MANA_CRYSTAL_INGREDIENTS) &&
					container.getItem(3).is(ESTags.Items.MANA_CRYSTAL_INGREDIENTS) &&
					container.getItem(4).is(ESTags.Items.MANA_CRYSTAL_INGREDIENTS) &&
					container.getItem(5).is(ESTags.Items.MANA_CRYSTAL_INGREDIENTS) &&
					container.getItem(7).is(ESTags.Items.MANA_CRYSTAL_INGREDIENTS);

			return checkDay && checkEmpty && checkIngredients;
		}

		return false;
	}

	@Override
	public ItemStack assemble(CraftingContainer container, RegistryAccess registryAccess) {
		return manaCrystal.getDefaultInstance();
	}

	@Override
	public boolean canCraftInDimensions(int w, int h) {
		return w == 3 && h == 3;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return ESRecipeSerializers.MANA_CRYSTAL.get();
	}

	public static class Serializer implements RecipeSerializer<ManaCrystalRecipe> {

		@Override
		public ManaCrystalRecipe fromJson(ResourceLocation id, JsonObject json) {
			CraftingBookCategory category =
				CraftingBookCategory.CODEC.byName(GsonHelper.getAsString(json, "category"), CraftingBookCategory.MISC);

			ManaType type = ManaType.valueOf(GsonHelper.getAsString(json, "mana_type").toUpperCase());

			Item crystal = BuiltInRegistries.ITEM.get(new ResourceLocation(GsonHelper.getAsString(json, "crystal")));

			return new ManaCrystalRecipe(id, category, type, crystal);
		}

		@Override
		public ManaCrystalRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
			CraftingBookCategory category = buf.readEnum(CraftingBookCategory.class);
			ManaType type = buf.readEnum(ManaType.class);
			Item output = buf.readById(BuiltInRegistries.ITEM);
			return new ManaCrystalRecipe(id, category, type, output);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buf, ManaCrystalRecipe recipe) {
			buf.writeEnum(recipe.category());
			buf.writeEnum(recipe.manaType);
			buf.writeId(BuiltInRegistries.ITEM, recipe.manaCrystal);
		}
	}

	public void save(Consumer<FinishedRecipe> consumer, ResourceLocation id) {
		consumer.accept(new FinishedRecipe() {
			@Override
			public void serializeRecipeData(JsonObject json) {
				json.addProperty("category", category().getSerializedName());
				json.addProperty("mana_type", manaType.name().toLowerCase());
				json.addProperty("crystal", BuiltInRegistries.ITEM.getKey(manaCrystal).toString());
			}

			@Override
			public RecipeSerializer<?> getType() {
				return ESRecipeSerializers.MANA_CRYSTAL.get();
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