package cn.leolezury.eternalstarlight.common.block.entity;

import cn.leolezury.eternalstarlight.common.EternalStarlight;
import cn.leolezury.eternalstarlight.common.registry.ESBlockEntities;
import cn.leolezury.eternalstarlight.common.registry.ESSoundEvents;
import cn.leolezury.eternalstarlight.common.util.ESMathUtil;
import cn.leolezury.eternalstarlight.common.util.Easing;
import cn.leolezury.eternalstarlight.common.util.SmoothSegmentedValue;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class StellarRackBlockEntity extends RandomizableContainerBlockEntity {

	private static final SmoothSegmentedValue RED = SmoothSegmentedValue.of(Easing.IN_OUT_EXPO, 251 / 255f, 151 / 255f, 0.5f).add(Easing.IN_OUT_EXPO, 151 / 255f, 251 / 255f, 0.5f);
	private static final SmoothSegmentedValue GREEN = SmoothSegmentedValue.of(Easing.IN_OUT_EXPO, 129 / 255f, 225 / 255f, 0.5f).add(Easing.IN_OUT_EXPO, 225 / 255f, 129 / 255f, 0.5f);
	private static final SmoothSegmentedValue BLUE = SmoothSegmentedValue.of(Easing.IN_OUT_EXPO, 2 / 255f, 250 / 255f, 0.5f).add(Easing.IN_OUT_EXPO, 250 / 255f, 2 / 255f, 0.5f);
	private static final SmoothSegmentedValue ALPHA = SmoothSegmentedValue.of(Easing.IN_OUT_EXPO, 0.9f, 1f, 0.5f).add(Easing.IN_OUT_EXPO, 1f, 0.9f, 0.5f);

	private NonNullList<ItemStack> items = NonNullList.withSize(5, ItemStack.EMPTY);

	private int tickCount = 0;
	private float oldStarRotation;
	private float starRotation;

	public StellarRackBlockEntity(BlockPos pos, BlockState state) {
		super(ESBlockEntities.STELLAR_RACK.get(), pos, state);
	}

	public int getTickCount() {
		return tickCount;
	}

	public boolean anyEmpty() {
		return items.stream().anyMatch(ItemStack::isEmpty);
	}

	public boolean placeItem(ItemStack itemStack) {
		for (int i = 0; i < getContainerSize(); i++) {
			if (getItem(i).isEmpty()) {
				setItem(i, itemStack);
				markUpdated();
				return true;
			}
		}
		return false;
	}

	private void markUpdated() {
		if (getLevel() != null) {
			this.setChanged();
			this.getLevel().sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
		}
	}

	public static void tick(Level level, BlockPos pos, BlockState state, StellarRackBlockEntity entity) {
		entity.tickCount++;
		entity.oldStarRotation = entity.starRotation;
		entity.starRotation += 10 * Mth.DEG_TO_RAD;

		if (level.isClientSide) {
			EternalStarlight.getClientHelper().spawnStellarRackParticles(pos.getCenter());

			float angle = 360f / entity.getItems().stream().filter(i -> !i.isEmpty()).count();
			float accumulatedAngle = entity.getStarRotation(0) * 0.4f * Mth.RAD_TO_DEG;

			for (ItemStack itemStack : entity.getItems()) {
				if (!itemStack.isEmpty()) {
					accumulatedAngle += angle;
					Vec3 vec3 = ESMathUtil.rotationToPosition(0.9f, 0, accumulatedAngle);
					EternalStarlight.getClientHelper().spawnStellarRackItemParticles(
						Vec3.atBottomCenterOf(entity.getBlockPos())
							.add(0, 0.9, 0)
							.add(vec3)
					);
				}
			}
		}

		if (level.getGameTime() % 600 == 0) {
			level.playSound(null, pos, ESSoundEvents.STELLAR_RACK_AMBIENT.get(), SoundSource.BLOCKS, 1f, 1f);
		}
	}

	public int getColor(float partialTicks) {
		float t = (getTickCount() + partialTicks) % 250;
		float f = t / 250f;

		float a = ALPHA.calculate(f);
		float r = RED.calculate(f);
		float g = GREEN.calculate(f);
		float b = BLUE.calculate(f);

		int ai = (int)(a * 255f);
		int ri = (int)(r * 255f);
		int gi = (int)(g * 255f);
		int bi = (int)(b * 255f);

		return FastColor.ARGB32.color(ai, ri, gi, bi);
	}

	public float getStarRotation(float partialTicks) {
		return Mth.lerp(partialTicks, oldStarRotation, starRotation);
	}

	@Override
	protected Component getDefaultName() {
		return Component.translatable("container." + EternalStarlight.ID + ".stellar_rack");
	}

	@Override
	public NonNullList<ItemStack> getItems() {
		return this.items;
	}

	@Override
	protected void setItems(NonNullList<ItemStack> items) {
		this.items = items;
	}

	@Override
	protected AbstractContainerMenu createMenu(int i, Inventory inv) {
		return null;
	}

	@Override
	public int getContainerSize() {
		return 5;
	}

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public CompoundTag getUpdateTag() {
		CompoundTag tag = new CompoundTag();
		ContainerHelper.saveAllItems(tag, this.items);
		return tag;
	}

	@Override
	public void load(CompoundTag tag) {
		super.load(tag);
		this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);

		if (!this.tryLoadLootTable(tag)) {
			ContainerHelper.loadAllItems(tag, this.items);
		}
	}

	@Override
	protected void saveAdditional(CompoundTag tag) {
		super.saveAdditional(tag);

		if (!this.trySaveLootTable(tag)) {
			ContainerHelper.saveAllItems(tag, this.items);
		}
	}
}
