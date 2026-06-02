package cn.leolezury.eternalstarlight.common.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.UUID;

public class DreamCatcherEffect extends MobEffect {
	public static final double ARMOR_ADDITION = 5D;

	private static final UUID DREAM_CATCHER_UUID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

	public DreamCatcherEffect(MobEffectCategory category, int color) {
		super(category, color);

		this.addAttributeModifier(Attributes.ARMOR, DREAM_CATCHER_UUID.toString(), ARMOR_ADDITION, AttributeModifier.Operation.ADDITION
		);
	}
}
