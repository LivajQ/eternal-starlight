package cn.leolezury.eternalstarlight.common.item.combat;

import cn.leolezury.eternalstarlight.common.entity.projectile.AethersentMeteor;
import cn.leolezury.eternalstarlight.common.item.interfaces.SwingAttackWeapon;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class RageOfStarsItem extends SwordItem implements SwingAttackWeapon {

	public RageOfStarsItem(Tier tier, int attackDamage, float attackSpeed, Properties properties) {
		super(tier, attackDamage, attackSpeed, properties);
	}

	private void performSpecialAttack(LivingEntity entity) {
		double range = 20;
		Vec3 eyePosition = entity.getEyePosition();
		Vec3 viewVector = entity.getViewVector(1.0F);
		Vec3 vec3 = eyePosition.add(viewVector.x * range, viewVector.y * range, viewVector.z * range);

		AABB aabb = entity.getBoundingBox()
			.expandTowards(viewVector.scale(range))
			.inflate(1.0D, 1.0D, 1.0D);

		EntityHitResult hit = ProjectileUtil.getEntityHitResult(
			entity,
			eyePosition,
			vec3,
			aabb,
			e -> !e.isSpectator() && e instanceof LivingEntity,
			range * range
		);

		if (hit != null && hit.getEntity() instanceof LivingEntity living && living.level() instanceof ServerLevel server) {
			Vec3 loc = living.position();
			AethersentMeteor.createMeteorShower(server, entity, living, loc.x, loc.y, loc.z, 30, 60);
			return;
		}

		if (entity.level() instanceof ServerLevel server) {
			Vec3 loc = eyePosition.add(viewVector.x * 10, viewVector.y * 10, viewVector.z * 10);
			AethersentMeteor.createMeteorShower(server, entity, null, loc.x, loc.y, loc.z, 30, 60);
		}
	}

	@Override
	public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		boolean result = super.hurtEnemy(stack, target, attacker);
		performSpecialAttack(attacker);
		return result;
	}

	@Override
	public void performSwingAttack(ItemStack stack, Player player) {
		performSpecialAttack(player);
	}
}
