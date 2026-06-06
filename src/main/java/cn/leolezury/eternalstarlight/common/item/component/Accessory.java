package cn.leolezury.eternalstarlight.common.item.component;

import com.google.common.collect.Multimap;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;

import java.util.List;
import java.util.Optional;

public record Accessory(
	TagKey<Item> combinationTarget,
	Component combinationTargetDescription,
	Multimap<Attribute, AttributeModifier> attributeModifiers,
	List<Component> extraDescription,
	Optional<Style> nameStyle,
	Optional<ResourceLocation> overlay
) {
}
