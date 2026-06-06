package cn.leolezury.eternalstarlight.common.item.combat;

import cn.leolezury.eternalstarlight.common.EternalStarlight;
import cn.leolezury.eternalstarlight.common.platform.ESPlatform;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class GreatswordItem extends SwordItem {

	private final float attackDamage;
	private final float attackSpeed;
	private final float reach;

	public GreatswordItem(Tier tier, int damage, float speed, float reach, Properties properties) {
		super(tier, damage, speed, properties);
		this.attackDamage = damage + tier.getAttackDamageBonus();
		this.attackSpeed = speed;
		this.reach = reach;
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
		if (slot == EquipmentSlot.MAINHAND) {
			ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();

			builder.put(
				Attributes.ATTACK_DAMAGE,
				new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "greatsword_damage", this.attackDamage, AttributeModifier.Operation.ADDITION)
			);

			builder.put(
				Attributes.ATTACK_SPEED,
				new AttributeModifier(BASE_ATTACK_SPEED_UUID, "greatsword_speed", this.attackSpeed, AttributeModifier.Operation.ADDITION)
			);

			Attribute reach = ESPlatform.INSTANCE.getReach();
			if (reach != null) {
				builder.put(
					reach,
					new AttributeModifier(
						EternalStarlight.id("weapon.entity_reach").toString(),
						this.reach,
						AttributeModifier.Operation.ADDITION
					)
				);
			}

			Attribute blockReach = ESPlatform.INSTANCE.getBlockReach();
			if (blockReach != null) {
				builder.put(
					blockReach,
					new AttributeModifier(
						EternalStarlight.id("weapon.block_reach").toString(),
						this.reach,
						AttributeModifier.Operation.ADDITION
					)
				);
			}

			return builder.build();
		}

		return super.getDefaultAttributeModifiers(slot);
	}


	@Override
	public int getUseDuration(ItemStack itemStack) {
		return 72000;
	}

	@Override
	public UseAnim getUseAnimation(ItemStack itemStack) {
		return UseAnim.BLOCK;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
		ItemStack itemStack = player.getItemInHand(interactionHand);
		if (itemStack.getDamageValue() >= itemStack.getMaxDamage() - 1) {
			return InteractionResultHolder.fail(itemStack);
		} else {
			player.startUsingItem(interactionHand);
			return InteractionResultHolder.consume(itemStack);
		}
	}
}
