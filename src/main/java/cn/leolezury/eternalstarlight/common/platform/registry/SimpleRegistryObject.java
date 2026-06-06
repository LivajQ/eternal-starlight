package cn.leolezury.eternalstarlight.common.platform.registry;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class SimpleRegistryObject<T, I extends T> implements RegistryObject<T, I> {
	private final ResourceLocation id;
	private final I value;
	private final ResourceKey<? extends Registry<T>> registryKey;

	public SimpleRegistryObject(ResourceLocation id, I value, ResourceKey<? extends Registry<T>> registryKey) {
		this.id = id;
		this.value = value;
		this.registryKey = registryKey;
	}

	@Override
	public I get() {
		return value;
	}

	@Override
	public ResourceLocation getId() {
		return id;
	}

	@Override
	public Holder<T> asHolder() {
		return Holder.direct(value);
	}

	@Override
	@SuppressWarnings("unchecked")
	public ResourceKey<I> getResourceKey() {
		return (ResourceKey<I>) ResourceKey.create(registryKey, id);
	}
}