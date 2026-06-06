package cn.leolezury.eternalstarlight.common.item.combat;

import cn.leolezury.eternalstarlight.common.client.renderer.ESForgeItemStackRenderer;
import cn.leolezury.eternalstarlight.common.entity.projectile.ThrownPungencyFruitSpear;
import cn.leolezury.eternalstarlight.common.entity.projectile.ThrownSpear;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class PungencyFruitSpearItem extends SpearItem {

	public PungencyFruitSpearItem(Tier tier, int attackDamage, float attackSpeed, Properties properties) {
		super(tier, attackDamage, attackSpeed, properties);
	}

	@Override
	public ThrownSpear createSpear(Level level, @Nullable LivingEntity owner, double x, double y, double z, ItemStack pickupItemStack) {
		return new ThrownPungencyFruitSpear(level, owner, x, y, z, pickupItemStack);
	}

	@Override
	public void initializeClient(Consumer<IClientItemExtensions> consumer) {
		consumer.accept(ESForgeItemStackRenderer.CLIENT_ITEM_EXTENSION);
	}
}
