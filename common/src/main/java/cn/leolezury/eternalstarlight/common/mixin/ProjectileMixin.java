package cn.leolezury.eternalstarlight.common.mixin;

import cn.leolezury.eternalstarlight.common.registry.ESAttributes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrownPotion;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Projectile.class)
public abstract class ProjectileMixin {

	@Shadow @Nullable
	public abstract Entity getOwner();

	@Inject(method = "shoot(DDDFF)V", at = @At("HEAD"), cancellable = true)
	private void es$modifyPotionVelocity(double x, double y, double z, float velocity, float inaccuracy, CallbackInfo ci) {
		Projectile self = (Projectile)(Object)this;

		if (!(self instanceof ThrownPotion)) return;
		if (!(getOwner() instanceof LivingEntity living)) return;

		double factor = 1.0;

		if (living.getAttributes().hasAttribute(ESAttributes.THROWN_POTION_DISTANCE.asHolder())) {
			AttributeInstance inst = living.getAttribute(ESAttributes.THROWN_POTION_DISTANCE.get());
			if (inst != null) factor = inst.getValue();
		}

		double nx = x * factor;
		double ny = y * factor;
		double nz = z * factor;

		self.shoot(nx, ny, nz, velocity, inaccuracy);

		ci.cancel();
	}
}

