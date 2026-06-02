package cn.leolezury.eternalstarlight.forge.registry;

import cn.leolezury.eternalstarlight.common.EternalStarlight;
import cn.leolezury.eternalstarlight.common.platform.registry.RegistrationProvider;
import cn.leolezury.eternalstarlight.common.platform.registry.RegistryObject;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.ForgeRegistries;

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
	});

	public static void loadClass() {
	}
}
