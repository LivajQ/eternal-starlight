package cn.leolezury.eternalstarlight.common.platform;

import com.mojang.datafixers.util.Either;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderOwner;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

public final class FakeDeferredHolder<T> implements Holder<T> {
	private final ResourceKey<? extends T> key;
	private final ResourceKey<? extends Registry<T>> registryKey;

	public FakeDeferredHolder(ResourceKey<? extends T> key,
							  ResourceKey<? extends Registry<T>> registryKey) {
		this.key = key;
		this.registryKey = registryKey;
	}

	@SuppressWarnings("unchecked")
	private Registry<T> registry() {
		return (Registry<T>) BuiltInRegistries.REGISTRY.get(registryKey.location());
	}

	@Override
	@SuppressWarnings("unchecked")
	public T value() {
		return (T) registry().get((ResourceKey<T>) key);
	}

	@Override
	public boolean isBound() {
		return true;
	}

	@Override
	public boolean is(ResourceLocation loc) {
		return key.location().equals(loc);
	}

	@Override
	public boolean is(ResourceKey<T> k) {
		return key.equals(k);
	}

	@Override
	public boolean is(Predicate<ResourceKey<T>> predicate) {
		return predicate.test((ResourceKey<T>) key);
	}

	@Override
	public boolean is(TagKey<T> tag) {
		return false;
	}

	@Override
	public Stream<TagKey<T>> tags() {
		return Stream.empty();
	}

	@Override
	public Either<ResourceKey<T>, T> unwrap() {
		return Either.left((ResourceKey<T>) key);
	}

	@Override
	public Optional<ResourceKey<T>> unwrapKey() {
		return Optional.of((ResourceKey<T>) key);
	}

	@Override
	public Kind kind() {
		return Kind.REFERENCE;
	}

	@Override
	public boolean canSerializeIn(HolderOwner<T> owner) {
		return true;
	}
}
