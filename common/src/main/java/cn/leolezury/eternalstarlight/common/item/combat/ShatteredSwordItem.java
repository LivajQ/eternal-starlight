package cn.leolezury.eternalstarlight.common.item.combat;

import cn.leolezury.eternalstarlight.common.entity.projectile.ThrownShatteredBlade;
import cn.leolezury.eternalstarlight.common.registry.ESItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;

public class ShatteredSwordItem extends SwordItem {

	public static final String TAG_HAS_BLADE = "HasBlade";

	public ShatteredSwordItem(Tier tier, int attackDamage, float attackSpeed, Properties properties) {
		super(tier, attackDamage, attackSpeed, properties);
	}

	public static boolean hasBlade(ItemStack stack) {
		CompoundTag tag = stack.getTag();
		return tag == null || !tag.contains(TAG_HAS_BLADE) || tag.getBoolean(TAG_HAS_BLADE);
	}

	public static void setHasBlade(ItemStack stack, boolean hasBlade) {
		stack.getOrCreateTag().putBoolean(TAG_HAS_BLADE, hasBlade);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
		ItemStack itemStack = player.getItemInHand(interactionHand);

		if (hasBlade(itemStack)) {

			ThrownShatteredBlade blade = new ThrownShatteredBlade(level, player, itemStack);
			blade.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 2.5F, 1.0F);

			if (player.getAbilities().instabuild) {
				blade.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
			}

			level.addFreshEntity(blade);
			level.playSound(null, blade.blockPosition(), SoundEvents.TRIDENT_THROW, SoundSource.PLAYERS);

			if (!player.getAbilities().instabuild) {
				EquipmentSlot slot = interactionHand == InteractionHand.MAIN_HAND
					? EquipmentSlot.MAINHAND
					: EquipmentSlot.OFFHAND;

				itemStack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(slot));
				setHasBlade(itemStack, false);
			}

			player.awardStat(Stats.ITEM_USED.get(this));
			return InteractionResultHolder.success(itemStack);

		} else {
			if (player.getAbilities().instabuild) {
				setHasBlade(itemStack, true);
				return InteractionResultHolder.consume(itemStack);
			} else {
				for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
					ItemStack item = player.getInventory().getItem(i);
					if (item.is(ESItems.SHATTERED_SWORD_BLADE.get())) {
						setHasBlade(itemStack, true);
						item.shrink(1);
						return InteractionResultHolder.consume(itemStack);
					}
				}
			}
		}

		return InteractionResultHolder.fail(itemStack);
	}

	@Override
	public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
		if (!level.isClientSide && entity instanceof Player player && !hasBlade(stack)) {
			for (int i = 0; i < player.getInventory().getContainerSize(); ++i) {
				ItemStack item = player.getInventory().getItem(i);
				if (item.is(ESItems.SHATTERED_SWORD_BLADE.get())) {
					player.getInventory().setItem(i, ItemStack.EMPTY);
					setHasBlade(stack, true);
					break;
				}
			}
		}
	}
}
