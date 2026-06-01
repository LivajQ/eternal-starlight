package cn.leolezury.eternalstarlight.forge.platform.registry;

import cn.leolezury.eternalstarlight.common.platform.registry.RegistryObject;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class LazyRegistryObject<T, I extends T> implements RegistryObject<T, I>
{
	private final ResourceLocation id;
	private final net.minecraftforge.registries.RegistryObject<I> forgeObject;
	private final ResourceKey<? extends Registry<T>> registryKey;

	public LazyRegistryObject(ResourceLocation id, net.minecraftforge.registries.RegistryObject<I> forgeObject, ResourceKey<? extends Registry<T>> registryKey) {
		this.id = id;
		this.forgeObject = forgeObject;
		this.registryKey = registryKey;
	}

	@Override
	public I get() {
		return forgeObject.get();
	}

	@Override
	public ResourceLocation getId() {
		return id;
	}

	@Override
	public Holder<T> asHolder() {
		return (Holder<T>) forgeObject.getHolder().orElseThrow();
	}

	@Override
	@SuppressWarnings("unchecked")
	public ResourceKey<I> getResourceKey() {
		return (ResourceKey<I>) ResourceKey.create(registryKey, id);
	}
}