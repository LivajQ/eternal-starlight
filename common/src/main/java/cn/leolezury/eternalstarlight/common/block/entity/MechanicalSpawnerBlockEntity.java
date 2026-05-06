package cn.leolezury.eternalstarlight.common.block.entity;

import cn.leolezury.eternalstarlight.common.registry.ESBlockEntities;
import cn.leolezury.eternalstarlight.common.registry.ESBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SpawnData;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class MechanicalSpawnerBlockEntity extends BlockEntity {

	private final MechanicalSpawner spawner = new MechanicalSpawner() {
		@Override
		public void broadcastEvent(Level level, BlockPos pos, int id) {
			level.blockEvent(pos, ESBlocks.MECHANICAL_SPAWNER.get(), id, 0);
		}

		@Override
		public void setNextSpawnData(@Nullable Level level, BlockPos pos, SpawnData data) {
			super.setNextSpawnData(level, pos, data);
			if (level != null) {
				BlockState state = level.getBlockState(pos);
				level.sendBlockUpdated(pos, state, state, 4);
			}
		}
	};

	public AnimationState idleAnimationState = new AnimationState();
	public int clientTickCount = 0;

	public MechanicalSpawnerBlockEntity(BlockPos pos, BlockState state) {
		super(ESBlockEntities.MECHANICAL_SPAWNER.get(), pos, state);
	}

	@Override
	public void load(CompoundTag tag) {
		super.load(tag);
		this.spawner.load(this.level, this.worldPosition, tag);
	}

	@Override
	protected void saveAdditional(CompoundTag tag) {
		super.saveAdditional(tag);
		this.spawner.save(tag);
	}

	public static void clientTick(Level level, BlockPos pos, BlockState state, MechanicalSpawnerBlockEntity be) {
		be.spawner.clientTick(level, pos);
		be.clientTickCount++;
		be.idleAnimationState.startIfStopped(be.clientTickCount);
	}

	public static void serverTick(Level level, BlockPos pos, BlockState state, MechanicalSpawnerBlockEntity be) {
		if (level instanceof ServerLevel serverLevel) {
			be.spawner.serverTick(serverLevel, pos);
		}
	}

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public CompoundTag getUpdateTag() {
		CompoundTag tag = new CompoundTag();
		this.saveAdditional(tag);
		tag.remove(MechanicalSpawner.TAG_SPAWN_POTENTIALS);
		return tag;
	}

	@Override
	public boolean triggerEvent(int id, int type) {
		if (this.level == null) return false;
		return this.spawner.onEventTriggered(this.level, getBlockState(), id)
			|| super.triggerEvent(id, type);
	}

	@Override
	public boolean onlyOpCanSetNbt() {
		return true;
	}

	/*
	@Override
	public void setEntityId(EntityType<?> type, RandomSource random) {
		this.spawner.setEntityId(type, this.level, random, this.worldPosition);
		this.setChanged();
	}
	 */

	public MechanicalSpawner getSpawner() {
		return this.spawner;
	}
}
