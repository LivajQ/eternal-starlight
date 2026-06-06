package cn.leolezury.eternalstarlight.common.block.entity;

import cn.leolezury.eternalstarlight.common.block.DryingRackBlock;
import cn.leolezury.eternalstarlight.common.item.recipe.DryingRecipe;
import cn.leolezury.eternalstarlight.common.registry.ESBlockEntities;
import cn.leolezury.eternalstarlight.common.registry.ESRecipes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class DryingRackBlockEntity extends SimpleContainerBlockEntity {
	private static final String TAG_DRYING_TICKS = "drying_ticks";

	protected DryingRackBlockEntity(BlockEntityType<?> entityType, BlockPos pos, BlockState state) {
		super(entityType, pos, state);
	}

	public DryingRackBlockEntity(BlockPos blockPos, BlockState blockState) {
		this(ESBlockEntities.DRYING_RACK.get(), blockPos, blockState);
	}

	private final RecipeManager.CachedCheck<Container, DryingRecipe> quickCheck = RecipeManager.createCheck(ESRecipes.DRYING.get());

	private boolean lastLit;
	private int dryingTicks = 0;
	private NonNullList<ItemStack> items = NonNullList.withSize(1, ItemStack.EMPTY);

	public ItemStack getItem() {
		return items.get(0);
	}

	public void setItem(ItemStack item) {
		setItem(0, item);
		this.dryingTicks = 0;
	}

	public boolean canBeDried(ItemStack stack, boolean fireBelow) {
		if (level == null) {
			return false;
		}
		List<DryingRecipe> list = level.getRecipeManager().getAllRecipesFor(ESRecipes.DRYING.get())
			.stream()
			.toList();
		for (DryingRecipe recipe : list) {
			if (fireBelow == recipe.fireBelow() && recipe.input().test(stack)) {
				return true;
			}
		}

		return false;
	}

	public static void tick(Level level, BlockPos pos, BlockState state, DryingRackBlockEntity entity) {
		if (!level.isClientSide) {
			if (!entity.getItem().isEmpty()) {
				boolean lit = state.getValue(DryingRackBlock.LIT);
				if (entity.lastLit != lit) {
					entity.dryingTicks = 0;
					entity.lastLit = lit;
				}
				SimpleContainer container = new SimpleContainer(entity.items.get(0));
				Optional<DryingRecipe> optionalRecipe = entity.quickCheck.getRecipeFor(container, level);
				if (optionalRecipe.isPresent()) {
					DryingRecipe recipe = optionalRecipe.get();

					entity.dryingTicks++;
					if (entity.dryingTicks > recipe.durationTicks()) {
						entity.dryingTicks = 0;
						entity.setItem(recipe.output().copy());
					}
				} else {
					entity.dryingTicks = 0;
				}
			} else {
				entity.dryingTicks = 0;
			}
		}
	}

	@Nullable
	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public CompoundTag getUpdateTag() {
		return this.saveWithFullMetadata();
	}

	@Override
	public void setChanged() {
		super.setChanged();
		if (getLevel() != null) {
			this.getLevel().sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
		}
	}

	@Override
	public void load(CompoundTag tag) {
		super.load(tag);

		this.dryingTicks = tag.getInt(TAG_DRYING_TICKS);
		this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
		ContainerHelper.loadAllItems(tag, this.items);
	}

	@Override
	protected void saveAdditional(CompoundTag tag) {
		super.saveAdditional(tag);

		tag.putInt(TAG_DRYING_TICKS, this.dryingTicks);
		ContainerHelper.saveAllItems(tag, this.items);
	}

	@Override
	protected NonNullList<ItemStack> getItems() {
		return items;
	}

	@Override
	public int getContainerSize() {
		return 1;
	}

	@Override
	public int getMaxStackSize() {
		return 1;
	}

	@Override
	public boolean canPlaceItem(int index, ItemStack stack) {
		BlockState state = getBlockState();
		return canBeDried(stack, state.hasProperty(DryingRackBlock.LIT) && state.getValue(DryingRackBlock.LIT)) && this.getItem(index).isEmpty() && stack.getCount() <= this.getMaxStackSize();
	}
}
