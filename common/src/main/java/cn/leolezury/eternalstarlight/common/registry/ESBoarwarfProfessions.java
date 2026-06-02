package cn.leolezury.eternalstarlight.common.registry;

import cn.leolezury.eternalstarlight.common.EternalStarlight;
import cn.leolezury.eternalstarlight.common.entity.living.npc.boarwarf.AbstractBoarwarfProfession;
import cn.leolezury.eternalstarlight.common.entity.living.npc.boarwarf.profession.*;
import cn.leolezury.eternalstarlight.common.platform.registry.RegistrationProvider;
import cn.leolezury.eternalstarlight.common.platform.registry.RegistryObject;
import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class ESBoarwarfProfessions {
	public static final RegistrationProvider<AbstractBoarwarfProfession> PROFESSIONS = RegistrationProvider.newRegistry(ResourceKey.createRegistryKey(EternalStarlight.id("boarwarf_profession")), EternalStarlight.ID);
	public static final Codec<AbstractBoarwarfProfession> CODEC = ResourceLocation.CODEC.xmap(ESBoarwarfProfessions::getById, ESBoarwarfProfessions::getId);
	public static final RegistryObject<AbstractBoarwarfProfession, BoarwarfBlacksmithProfession> BLACKSMITH =
		PROFESSIONS.register("blacksmith", BoarwarfBlacksmithProfession::new);
	public static final RegistryObject<AbstractBoarwarfProfession, BoarwarfChefProfession> CHEF =
		PROFESSIONS.register("chef", BoarwarfChefProfession::new);
	public static final RegistryObject<AbstractBoarwarfProfession, BoarwarfDruidProfession> DRUID =
		PROFESSIONS.register("druid", BoarwarfDruidProfession::new);
	public static final RegistryObject<AbstractBoarwarfProfession, BoarwarfDyerProfession> DYER =
		PROFESSIONS.register("dyer", BoarwarfDyerProfession::new);
	public static final RegistryObject<AbstractBoarwarfProfession, BoarwarfSilversmithProfession> SILVERSMITH =
		PROFESSIONS.register("silversmith", BoarwarfSilversmithProfession::new);

	public static AbstractBoarwarfProfession getById(ResourceLocation id) {
		return PROFESSIONS.get(id);
	}

	public static ResourceLocation getId(AbstractBoarwarfProfession prof) {
		return PROFESSIONS.getId(prof);
	}

	public static void loadClass() {}
}
