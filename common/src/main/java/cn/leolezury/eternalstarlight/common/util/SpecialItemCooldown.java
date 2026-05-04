package cn.leolezury.eternalstarlight.common.util;

import cn.leolezury.eternalstarlight.common.registry.ESDataAttachments;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.List;

public record SpecialItemCooldown(Holder<Item> item, int cooldown) {

	public void write(FriendlyByteBuf buf) {
		// Write item registry ID
		buf.writeVarInt(BuiltInRegistries.ITEM.getId(item.value()));
		// Write cooldown
		buf.writeVarInt(cooldown);
	}

	public static SpecialItemCooldown read(FriendlyByteBuf buf) {
		// Read item registry ID
		int itemId = buf.readVarInt();
		Item item = BuiltInRegistries.ITEM.byId(itemId);
		// Read cooldown
		int cooldown = buf.readVarInt();
		return new SpecialItemCooldown(item.builtInRegistryHolder(), cooldown);
	}

	public static boolean isOnCooldown(Entity entity, Item item) {
		return getCooldown(entity, item) > 0;
	}

	public static int getCooldown(Entity entity, Item item) {
		return ESDataAttachments.SPECIAL_ITEM_COOLDOWNS.getData(entity)
			.stream()
			.filter(c -> c.item().value() == item)
			.map(SpecialItemCooldown::cooldown)
			.findFirst()
			.orElse(0);
	}

	public static void setCooldown(Entity entity, Item item, int time) {
		List<SpecialItemCooldown> cooldowns =
			new ArrayList<>(ESDataAttachments.SPECIAL_ITEM_COOLDOWNS.getData(entity));

		cooldowns.removeIf(c -> c.item().value() == item);
		cooldowns.add(new SpecialItemCooldown(item.builtInRegistryHolder(), time));

		ESDataAttachments.SPECIAL_ITEM_COOLDOWNS.setData(entity, cooldowns);
	}

	public static void removeCooldown(Entity entity, Item item) {
		setCooldown(entity, item, 0);
	}

	public static void tick(Entity entity) {
		if (entity.level().isClientSide) return;

		List<SpecialItemCooldown> cooldowns =
			new ArrayList<>(ESDataAttachments.SPECIAL_ITEM_COOLDOWNS.getData(entity));

		if (!cooldowns.isEmpty()) {
			List<SpecialItemCooldown> newCooldowns = new ArrayList<>();

			for (SpecialItemCooldown cd : cooldowns) {
				if (cd.cooldown() > 0) {
					newCooldowns.add(new SpecialItemCooldown(cd.item(), cd.cooldown() - 1));
				}
			}

			ESDataAttachments.SPECIAL_ITEM_COOLDOWNS.setData(entity, newCooldowns);
		}
	}
}
