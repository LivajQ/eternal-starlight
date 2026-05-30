package cn.leolezury.eternalstarlight.common.effect;

import cn.leolezury.eternalstarlight.common.EternalStarlight;
import cn.leolezury.eternalstarlight.common.data.ESDamageTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.UUID;

public class CrystalInfectionEffect extends MobEffect {
	public static final UUID ARMOR_MODIFIER_UUID =
		UUID.fromString("b1f4c8e2-7a3d-4c1e-9f12-3a4b5c6d7e8f");

	public static final double ARMOR_ADDITION = -1.5;

	public CrystalInfectionEffect(MobEffectCategory category, int color) {
		super(category, color);
		// pass UUID as string, as required by your addAttributeModifier
		this.addAttributeModifier(
			Attributes.ARMOR,
			ARMOR_MODIFIER_UUID.toString(),
			ARMOR_ADDITION,
			AttributeModifier.Operation.ADDITION
		);
	}
	@Override
	public void applyEffectTick(LivingEntity living, int amplifier) {
		living.hurt(ESDamageTypes.getDamageSource(living.level(), ESDamageTypes.CRYSTAL_INFECTION), amplifier + 1);
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return duration % 35 == 0;
	}
}
