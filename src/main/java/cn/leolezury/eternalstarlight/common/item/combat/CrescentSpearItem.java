package cn.leolezury.eternalstarlight.common.item.combat;

import cn.leolezury.eternalstarlight.common.client.renderer.ESForgeItemStackRenderer;
import cn.leolezury.eternalstarlight.common.registry.ESDataAttachments;
import cn.leolezury.eternalstarlight.common.registry.ESItems;
import cn.leolezury.eternalstarlight.common.registry.ESSoundEvents;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.BlockPos;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.function.Consumer;

public class CrescentSpearItem extends TieredItem {

	private final float attackDamage;
	private final float attackSpeed;

	public CrescentSpearItem(Tier tier, float damage, float speed, Properties props) {
		super(tier, props);
		this.attackDamage = damage + tier.getAttackDamageBonus();
		this.attackSpeed = speed;
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
		if (slot == EquipmentSlot.MAINHAND) {
			ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();

			builder.put(
				Attributes.ATTACK_DAMAGE,
				new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "crescent_spear_damage", this.attackDamage, AttributeModifier.Operation.ADDITION)
			);

			builder.put(
				Attributes.ATTACK_SPEED,
				new AttributeModifier(BASE_ATTACK_SPEED_UUID, "crescent_spear_speed", this.attackSpeed, AttributeModifier.Operation.ADDITION)
			);

			return builder.build();
		}

		return super.getDefaultAttributeModifiers(slot);
	}

	@Override
	public boolean canAttackBlock(BlockState state, Level level, BlockPos pos, Player player) {
		return !player.isCreative();
	}

	@Override
	public UseAnim getUseAnimation(ItemStack stack) {
		return UseAnim.SPEAR;
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return 72000;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		player.startUsingItem(hand);
		return InteractionResultHolder.consume(stack);
	}

	@Override
	public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
		if (!(entity instanceof Player player)) return;

		int useTime = this.getUseDuration(stack) - timeLeft;
		BlockHitResult result = level.clip(new ClipContext(
			player.position(),
			player.position().add(0, -5, 0),
			ClipContext.Block.COLLIDER,
			ClipContext.Fluid.ANY,
			player
		));

		if (useTime >= 10 && (result.getType() != HitResult.Type.MISS || player.getAbilities().instabuild)) {

			//float spinStrength = EnchantmentHelper.getTridentSpinAttackStrength(stack, player) + 1.75f; TODO ?
			float spinStrength = 2.0F;

			if (!level.isClientSide) {
				stack.hurtAndBreak(1, player, p ->
					p.broadcastBreakEvent(
						entity.getUsedItemHand() == InteractionHand.MAIN_HAND
							? EquipmentSlot.MAINHAND
							: EquipmentSlot.OFFHAND
					)
				);
			}

			float yaw = player.getYRot();
			float pitch = player.getXRot();

			float xSpeed = -Mth.sin(yaw * Mth.DEG_TO_RAD) * Mth.cos(pitch * Mth.DEG_TO_RAD);
			float ySpeed = -Mth.sin(pitch * Mth.DEG_TO_RAD);
			float zSpeed = Mth.cos(yaw * Mth.DEG_TO_RAD) * Mth.cos(pitch * Mth.DEG_TO_RAD);

			float len = Mth.sqrt(xSpeed * xSpeed + ySpeed * ySpeed + zSpeed * zSpeed);
			xSpeed *= spinStrength / len;
			ySpeed *= spinStrength / len;
			zSpeed *= spinStrength / len;

			player.push(xSpeed, ySpeed, zSpeed);

			ESDataAttachments.CRESCENT_SPEAR_DASH.setData(player, true);

			//float damage = (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE) * 1.5f;
			player.startAutoSpinAttack(20);

			player.getCooldowns().addCooldown(this, 20);
			player.playSound(ESSoundEvents.CRESCENT_SPEAR_THROW.get());

			if (player.onGround()) {
				player.move(MoverType.SELF, new Vec3(0.0, 1.2, 0.0));
			}

			player.awardStat(Stats.ITEM_USED.get(this));
		}
	}

	@Override
	public int getEnchantmentValue() {
		return 1;
	}

	@Override
	public boolean isValidRepairItem(ItemStack stack, ItemStack repairCandidate) {
		return repairCandidate.is(ESItems.TENACIOUS_PETAL.get())
			|| repairCandidate.is(ESItems.TENACIOUS_VINE.get())
			|| super.isValidRepairItem(stack, repairCandidate);
	}

	@Override
	public void initializeClient(Consumer<IClientItemExtensions> consumer) {
		consumer.accept(ESForgeItemStackRenderer.CLIENT_ITEM_EXTENSION);
	}
}