package cn.leolezury.eternalstarlight.common.item.combat;

import cn.leolezury.eternalstarlight.common.EternalStarlight;
import cn.leolezury.eternalstarlight.common.platform.ESPlatform;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class ScytheItem extends TieredItem {

	private final boolean canTill;
	private final float attackDamage;
	private final float attackSpeed;
	private final float reach;
	private final float sweep;

	public ScytheItem(Tier tier, boolean canTill, int damage, float speed, float reach, float sweep, Properties props) {
		super(tier, props);
		this.canTill = canTill;
		this.attackDamage = damage + tier.getAttackDamageBonus();
		this.attackSpeed = speed;
		this.reach = reach;
		this.sweep = sweep;
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
		if (slot == EquipmentSlot.MAINHAND) {
			ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();

			builder.put(
				Attributes.ATTACK_DAMAGE,
				new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "scythe_damage", this.attackDamage, AttributeModifier.Operation.ADDITION)
			);

			builder.put(
				Attributes.ATTACK_SPEED,
				new AttributeModifier(BASE_ATTACK_SPEED_UUID, "scythe_speed", this.attackSpeed, AttributeModifier.Operation.ADDITION)
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

			/* TODO sweep needs sth else
			Attribute sweep = ESPlatform.INSTANCE.getSweep();
			if (sweep != null) {
				builder.put(
					sweep,
					new AttributeModifier(
						EternalStarlight.id("weapon.sweep").toString(),
						this.sweep,
						AttributeModifier.Operation.ADDITION
					)
				);
			}
			 */

			return builder.build();
		}

		return super.getDefaultAttributeModifiers(slot);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		if (!canTill) return InteractionResult.PASS;

		Level level = context.getLevel();
		BlockPos pos = context.getClickedPos();
		Pair<Predicate<UseOnContext>, Consumer<UseOnContext>> pair = ESPlatform.INSTANCE.getToolTillAction(context);

		if (pair == null) return InteractionResult.PASS;

		Predicate<UseOnContext> predicate = pair.getFirst();
		Consumer<UseOnContext> consumer = pair.getSecond();

		if (predicate.test(context)) {
			Player player = context.getPlayer();
			level.playSound(player, pos, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0F, 1.0F);

			if (!level.isClientSide) {
				consumer.accept(context);

				if (player != null) {
					context.getItemInHand().hurtAndBreak(
						1,
						player,
						(p) -> p.broadcastBreakEvent(context.getHand() == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND)
					);
				}
			}

			return InteractionResult.sidedSuccess(level.isClientSide);
		}

		return InteractionResult.PASS;
	}

	@Override
	public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		stack.hurtAndBreak(1, attacker, (e) -> e.broadcastBreakEvent(EquipmentSlot.MAINHAND));
		return true;
	}
}
