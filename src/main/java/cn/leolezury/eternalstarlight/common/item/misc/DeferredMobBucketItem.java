package cn.leolezury.eternalstarlight.common.item.misc;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.MobBucketItem;
import net.minecraft.world.level.material.Fluid;

import java.util.function.Supplier;

public class DeferredMobBucketItem extends MobBucketItem {
	private final Supplier<? extends EntityType<? extends Mob>> typeSupplier;

	public DeferredMobBucketItem(Supplier<? extends EntityType<? extends Mob>> type, Fluid fluid, SoundEvent sound, Properties props) {
		super(null, fluid, sound, props);
		this.typeSupplier = type;
	}

	@Override
	protected EntityType<?> getFishType() {
		return typeSupplier.get();
	}
}
