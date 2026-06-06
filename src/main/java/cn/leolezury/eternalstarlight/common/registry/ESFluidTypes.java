package cn.leolezury.eternalstarlight.common.registry;

import cn.leolezury.eternalstarlight.common.EternalStarlight;
import cn.leolezury.eternalstarlight.common.client.handler.ESClientHandler;
import cn.leolezury.eternalstarlight.common.platform.registry.RegistrationProvider;
import cn.leolezury.eternalstarlight.common.platform.registry.RegistryObject;
import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.ForgeRegistries;
import org.joml.Vector3f;

import java.util.function.Consumer;

public class ESFluidTypes {
	public static final RegistrationProvider<FluidType> FLUID_TYPES = RegistrationProvider.get(ForgeRegistries.Keys.FLUID_TYPES, EternalStarlight.ID);
	public static final RegistryObject<FluidType, FluidType> ETHER = FLUID_TYPES.register("ether", () -> new FluidType(
		FluidType.Properties.create()
			.canExtinguish(true)
			.canSwim(false)
			.canDrown(false)
			.pathType(BlockPathTypes.DANGER_OTHER)
			.adjacentPathType(null)
			.lightLevel(15)
			.density(3000)
			.viscosity(6000)
	) {
		@Override
		public double motionScale(Entity entity) {
			return 0.0014;
		}

		@Override
		public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
			consumer.accept(new IClientFluidTypeExtensions() {
				@Override
				public ResourceLocation getStillTexture() {
					return EternalStarlight.id("block/ether");
				}

				@Override
				public ResourceLocation getFlowingTexture() {
					return EternalStarlight.id("block/ether_flow");
				}

				@Override
				public int getTintColor(FluidState state, BlockAndTintGetter getter, BlockPos pos) {
					return ESClientHandler.getEtherTint(getter, pos);
				}

				@Override
				public Vector3f modifyFogColor(Camera camera, float partialTick, ClientLevel level, int renderDistance, float darkenWorldAmount, Vector3f fluidFogColor) {
					return new Vector3f(232/255f, 255/255f, 222/255f);
				}

				@Override
				public void modifyFogRender(Camera camera, FogRenderer.FogMode mode, float renderDistance, float partialTick, float nearDistance, float farDistance, FogShape shape) {
					RenderSystem.setShaderFogStart(0.0F);
					RenderSystem.setShaderFogEnd(3.0F);
				}
			});
		}
	});

	public static void loadClass() {
	}
}
