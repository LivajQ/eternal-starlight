package cn.leolezury.eternalstarlight.common.network;

import cn.leolezury.eternalstarlight.common.EternalStarlight;
import cn.leolezury.eternalstarlight.common.util.ESMiscUtil;
import cn.leolezury.eternalstarlight.common.vfx.VfxInstance;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class VfxPacket implements ESPacket {

	private final VfxInstance instance;

	public VfxPacket(VfxInstance instance) {
		this.instance = instance;
	}

	public static VfxPacket read(FriendlyByteBuf buf) {
		return new VfxPacket(VfxInstance.read(buf));
	}

	@Override
	public void write(FriendlyByteBuf buf) {
		instance.write(buf);
	}

	@Override
	public void handle(Player player) {
		ESMiscUtil.runWhenOnClient(() -> () -> {
			CompoundTag data = instance.data();
			instance.type().ifPresent(t -> t.spawnOnClient(data));
		});
	}

	@Override
	public ResourceLocation id() {
		return EternalStarlight.id("vfx");
	}

	public VfxInstance instance() {
		return instance;
	}
}
