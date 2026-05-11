package cn.leolezury.eternalstarlight.common.enchantment;

import cn.leolezury.eternalstarlight.common.entity.projectile.ThrownPungencyFruitSpear;
import cn.leolezury.eternalstarlight.common.particle.ESSmokeParticleOptions;
import cn.leolezury.eternalstarlight.common.registry.ESMobEffects;
import cn.leolezury.eternalstarlight.common.util.ESTags;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import org.joml.Vector3f;

public class TearingEnchantment extends Enchantment {

	public TearingEnchantment() {
		super(Rarity.COMMON, EnchantmentCategory.WEAPON, new EquipmentSlot[]{ EquipmentSlot.MAINHAND });
	}

	@Override
	public int getMinCost(int level) {
		return 25;
	}

	@Override
	public int getMaxCost(int level) {
		return 75;
	}

	@Override
	public int getMaxLevel() {
		return 1;
	}

	@Override
	public boolean canEnchant(ItemStack stack) {
		return stack.is(ESTags.Items.PUNGENCY_FRUIT_SPEAR_ENCHANTABLE)
			|| super.canEnchant(stack);
	}

	@Override
	public void doPostAttack(LivingEntity attacker, Entity target, int level) {
		if (!(target instanceof LivingEntity victim)) return;

		DamageSource source = victim.getLastDamageSource();
		if (source == null) return;

		Entity direct = source.getDirectEntity();

		if (!(direct instanceof ThrownPungencyFruitSpear)) return;

		int durationTicks = (int)(2.5F * 20);
		victim.addEffect(new MobEffectInstance(
			ESMobEffects.TEARY.get(),
			durationTicks,
			0
		));

		if (attacker.level() instanceof ServerLevel server) {
			for (int i = 0; i < 12; i++) {
				double ox = direct.getX() + (server.random.nextDouble() - 0.5) * direct.getBbWidth();
				double oy = direct.getY() + server.random.nextDouble() * direct.getBbHeight();
				double oz = direct.getZ() + (server.random.nextDouble() - 0.5) * direct.getBbWidth();

				double vx = (server.random.nextDouble() - 0.5) * 0.1;
				double vy = server.random.nextDouble() * 0.1;
				double vz = (server.random.nextDouble() - 0.5) * 0.1;

				server.sendParticles(
					new ESSmokeParticleOptions(
						new Vector3f(0.34117648F, 0.227451F, 0.27058825F),
						new Vector3f(0.7019608F,  0.454902F, 0.454902F),
						1.0F,
						1.0F,
						0.3F,
						true
					)
					,
					ox, oy, oz,
					1,
					vx, vy, vz,
					0.0
				);

			}
		}

		super.doPostAttack(attacker, target, level);
	}
}
