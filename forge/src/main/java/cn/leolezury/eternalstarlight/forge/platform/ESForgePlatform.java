package cn.leolezury.eternalstarlight.forge.platform;

import cn.leolezury.eternalstarlight.common.network.ESPacket;
import cn.leolezury.eternalstarlight.common.platform.ESPlatform;
import cn.leolezury.eternalstarlight.common.platform.EntityDataAttachment;
import cn.leolezury.eternalstarlight.common.platform.registry.*;
import cn.leolezury.eternalstarlight.common.registry.ESCreativeModeTabs;
import cn.leolezury.eternalstarlight.common.registry.ESItems;
import cn.leolezury.eternalstarlight.common.util.ESTags;
import cn.leolezury.eternalstarlight.forge.network.ESForgeNetworkHandler;
import com.google.auto.service.AutoService;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Lifecycle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.DataPackRegistryEvent;

import java.nio.file.Path;
import java.util.function.*;

@AutoService(ESPlatform.class)
public class ESForgePlatform implements ESPlatform {

	@Override
	public Loader getLoader() {
		return Loader.FORGE;
	}

	@Override
	public boolean isPhysicalClient() {
		return FMLEnvironment.dist == Dist.CLIENT;
	}

	@Override
	public Path getConfigDir() {
		return FMLPaths.CONFIGDIR.get();
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> RegistrationProvider<T> createRegistrationProvider(ResourceKey<? extends Registry<T>> key, String namespace) {
		Registry<T> reg = (Registry<T>) BuiltInRegistries.REGISTRY.get(key.location());
		if (reg == null) throw new IllegalStateException("Registry not found: " + key.location());

		return new ForgeVanillaRegistrationProvider<>(key, reg, namespace);
	}

	@Override
	public <T> RegistrationProvider<T> createNewRegistryProvider(ResourceKey<? extends Registry<T>> key, String namespace) {
		Registry<T> reg = createMojangRegistry(key);
		registerIntoRoot(key, reg);
		return new ForgeVanillaRegistrationProvider<>(key, reg, namespace);
	}

	//more garbage courtesy of forge
	@SuppressWarnings("unchecked")
	private static <T> Registry<T> createMojangRegistry(ResourceKey<? extends Registry<T>> key) {
		ResourceKey<Registry<T>> cast = (ResourceKey<Registry<T>>) key;
		return new MappedRegistry<>(cast, Lifecycle.stable(), false);
	}

	@SuppressWarnings("unchecked")
	private static <T> void registerIntoRoot(ResourceKey<? extends Registry<T>> key, Registry<T> registry) {
		MappedRegistry<Registry<?>> root = (MappedRegistry<Registry<?>>) BuiltInRegistries.REGISTRY;
		root.register((ResourceKey<Registry<?>>) key, registry, Lifecycle.stable());
	}

	static class ForgeVanillaRegistrationProvider<T> implements RegistrationProvider<T> {
		private final ResourceKey<? extends Registry<T>> key;
		private final Registry<T> registry;
		private final String namespace;

		public ForgeVanillaRegistrationProvider(ResourceKey<? extends Registry<T>> key, Registry<T> registry, String namespace) {
			this.key = key;
			this.registry = registry;
			this.namespace = namespace;
		}

		@Override
		public Registry<T> registry() {
			return registry;
		}

		@Override
		public <I extends T> RegistryObject<T, I> register(String id, Supplier<? extends I> supplier) {
			ResourceLocation rl = new ResourceLocation(namespace, id);
			I instance = supplier.get();
			Registry.register(registry, rl, instance);

			return new SimpleRegistryObject<>(rl, instance, key);
		}
	}

	@Override
	public <T> void registerDatapackRegistry(ResourceKey<Registry<T>> key, Codec<T> codec, Codec<T> networkCodec) {
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

		bus.addListener((DataPackRegistryEvent.NewRegistry event) -> {
			if (networkCodec != null) {
				event.dataPackRegistry(key, codec, networkCodec);
			} else {
				event.dataPackRegistry(key, codec);
			}
		});
	}

	@Override
	public CreativeModeTab getESTab() {
		return CreativeModeTab.builder()
			.icon(() -> new ItemStack(ESItems.STARLIGHT_FLOWER.get()))
			.title(Component.translatable("itemGroup.eternal_starlight"))
			.displayItems((displayParameters, output) -> {
				for (ResourceKey<Item> entry : ESItems.REGISTERED_ITEMS) {
					Item item = BuiltInRegistries.ITEM.get(entry);
					if (item != null) {
						output.accept(item);

						if (item == ESItems.STARLIT_PAINTING.get()) {
							displayParameters.holders()
								.lookup(Registries.PAINTING_VARIANT)
								.ifPresent(registryLookup ->
									ESCreativeModeTabs.generatePresetPaintings(
										output,
										displayParameters.holders(),
										registryLookup,
										holder -> holder.is(ESTags.PaintingVariants.PLACEABLE)
									)
								);
						}
					}
				}
			})
			.build();
	}

	@Override
	public <T> EntityDataAttachment<T> createAttachment(ResourceLocation id, Supplier<T> defaultValue, boolean copyOnDeath, BiPredicate<T, T> shouldSync, BiConsumer<FriendlyByteBuf, T> writer, Function<FriendlyByteBuf, T> reader) {
		return new ForgeEntityDataAttachment<>(id, defaultValue, copyOnDeath, shouldSync, writer, reader);
	}

	@Override
	public boolean isShears(ItemStack stack) {
		return stack.canPerformAction(ToolActions.SHEARS_CARVE);
	}

	@Override
	public boolean isShield(ItemStack stack) {
		return stack.getItem() instanceof ShieldItem;
	}

	@Override
	public Pair<Predicate<UseOnContext>, Consumer<UseOnContext>> getToolTillAction(UseOnContext context) {
		return Pair.of(
			c -> c.getItemInHand().canPerformAction(ToolActions.HOE_TILL),
			c -> {
				BlockPos pos = c.getClickedPos();
				Level level = c.getLevel();
				BlockState state = level.getBlockState(pos);
				state.use(level, c.getPlayer(), InteractionHand.MAIN_HAND, c.getHitResult());
			}
		);
	}

	@Override
	public void sendToClient(ServerPlayer player, ESPacket packet) {
		ESForgeNetworkHandler.sendToClient(player, packet);
	}

	@Override
	public void sendToTrackingClientsImpl(ServerLevel level, Entity entity, ESPacket packet) {
		ESForgeNetworkHandler.sendToTracking(level, entity, packet);
	}

	@Override
	public Attribute getReach() {
		return ForgeMod.ENTITY_REACH.get();
	}

	@Override
	public Attribute getBlockReach() {
		return ForgeMod.BLOCK_REACH.get();
	}

}
