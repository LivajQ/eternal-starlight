package cn.leolezury.eternalstarlight.common.item.combat;

import cn.leolezury.eternalstarlight.common.data.ESDamageTypes;
import cn.leolezury.eternalstarlight.common.registry.ESCriteriaTriggers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;

public class DaggerOfHungerItem extends DualWieldingSwordItem {

	public DaggerOfHungerItem(Tier tier, Properties properties) {
		super(tier, 3, -2.4F, properties);
	}

	@Override
	public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		boolean result = super.hurtEnemy(stack, target, attacker);

		target.addEffect(new MobEffectInstance(MobEffects.HUNGER, 60));

		if (attacker instanceof Player player) {
			player.getFoodData().eat(3, 0);
		}

		CompoundTag tag = stack.getOrCreateTag();
		float hungerLevel = Mth.clamp(tag.getFloat("HungerLevel"), -1f, 1f);

		float newHungerLevel = Math.min(1f, hungerLevel + 0.05f);
		tag.putFloat("HungerLevel", newHungerLevel);

		if (newHungerLevel == 1f && attacker instanceof ServerPlayer player) {
			ESCriteriaTriggers.SATURATE_DAGGER_OF_HUNGER.trigger(player);
		}

		return result;
	}

	@Override
	public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
		if (!level.isClientSide && entity.tickCount % 1200 == 0) {

			CompoundTag tag = stack.getOrCreateTag();
			float hungerLevel = Mth.clamp(tag.getFloat("HungerLevel"), -1f, 1f);

			float newHungerLevel = Math.max(-1f, hungerLevel - 0.00005f * 1200);
			tag.putFloat("HungerLevel", newHungerLevel);

			if (hungerLevel == -1f) {
				entity.hurt(ESDamageTypes.getDamageSource(level, ESDamageTypes.DAGGER_OF_HUNGER), 3);

				newHungerLevel = Math.min(1f, newHungerLevel + 0.05f);
				tag.putFloat("HungerLevel", newHungerLevel);
			}
		}

		if (!level.isClientSide && entity.tickCount % 600 == 0) {

			CompoundTag tag = stack.getOrCreateTag();
			float hungerLevel = Mth.clamp(tag.getFloat("HungerLevel"), -1f, 1f);

			int state = Math.min(2, (int) ((hungerLevel + 1f) * 1.5f));
			tag.putInt("HungerState", state);  // store state for getAttributeModifiers()
		}

		super.inventoryTick(stack, level, entity, slot, selected);
	}


	@Override
	public boolean isBarVisible(ItemStack stack) {
		CompoundTag tag = stack.getTag();
		float hungerLevel = Mth.clamp(tag != null ? tag.getFloat("HungerLevel") : 0f, -1f, 1f);
		return hungerLevel < 1f;
	}

	@Override
	public int getBarWidth(ItemStack stack) {
		CompoundTag tag = stack.getTag();
		float hungerLevel = Mth.clamp(tag != null ? tag.getFloat("HungerLevel") : 0f, -1f, 1f);
		return Mth.clamp(Math.round(13.0F - (1.0F - hungerLevel) * 13.0F / 2.0F), 0, 13);
	}

	@Override
	public int getBarColor(ItemStack stack) {
		CompoundTag tag = stack.getTag();
		float hungerLevel = Mth.clamp(tag != null ? tag.getFloat("HungerLevel") : 0f, -1f, 1f);
		float progress = Mth.clamp((hungerLevel + 1.0F) / 2.0F, 0f, 1f);

		int ai = 255;
		int ri = (int)(103f * progress);
		int gi = (int)(47f * progress);
		int bi = (int)(207f * progress);

		return FastColor.ARGB32.color(ai, ri, gi, bi);
	}
}
