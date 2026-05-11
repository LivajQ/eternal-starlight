package cn.leolezury.eternalstarlight.common.effect;

import cn.leolezury.eternalstarlight.common.EternalStarlight;
import cn.leolezury.eternalstarlight.common.data.ESDamageTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class CrystalInfectionEffect extends MobEffect {
	public static final ResourceLocation ARMOR_MODIFIER_ID = EternalStarlight.id("armor.crystal_infection");
	public static final double ARMOR_ADDITION = -1.5;

	public CrystalInfectionEffect(MobEffectCategory category, int color) {
		super(category, color);
		this.addAttributeModifier(Attributes.ARMOR, ARMOR_MODIFIER_ID.toString(), ARMOR_ADDITION, AttributeModifier.Operation.ADDITION);
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
