package cn.leolezury.eternalstarlight.common.item.misc;

import cn.leolezury.eternalstarlight.common.EternalStarlight;
import cn.leolezury.eternalstarlight.common.network.ParticlePacket;
import cn.leolezury.eternalstarlight.common.particle.ExplosionShockParticleOptions;
import cn.leolezury.eternalstarlight.common.platform.ESPlatform;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;

import javax.annotation.Nullable;
import java.util.List;

public class EthericEyeItem extends Item {
	public EthericEyeItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		int y = level.getHeight(Heightmap.Types.WORLD_SURFACE, player.getBlockX(), player.getBlockZ());

		if (y != player.getY()) {
			if (level instanceof ServerLevel serverLevel) {
				RandomSource random = player.getRandom();
				for (int i = 0; i <= 15; i++) {
					double direction = Math.signum(y - player.getY());

					ESPlatform.INSTANCE.sendToAllClients(serverLevel,
						new ParticlePacket(
							ExplosionShockParticleOptions.ETHER,
							player.getX() + (random.nextFloat() - 0.5f) * player.getBbWidth() * 5,
							player.getY() + (0.5 - 0.5 * direction) * player.getBbHeight(),
							player.getZ() + (random.nextFloat() - 0.5f) * player.getBbWidth() * 5,
							0, direction, 0
						)
					);

					ESPlatform.INSTANCE.sendToAllClients(serverLevel,
						new ParticlePacket(
							ExplosionShockParticleOptions.ETHER,
							player.getX() + (random.nextFloat() - 0.5f) * player.getBbWidth() * 5,
							y + (0.5 - 0.5 * direction) * player.getBbHeight(),
							player.getZ() + (random.nextFloat() - 0.5f) * player.getBbWidth() * 5,
							0, direction, 0
						)
					);
				}
			}

			player.resetFallDistance();
			player.teleportTo(player.getX(), y, player.getZ());
			stack.shrink(1);
			player.awardStat(Stats.ITEM_USED.get(this));
			player.getCooldowns().addCooldown(this, 20);

			return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
		}

		return InteractionResultHolder.pass(stack);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level,
								List<Component> tooltip, TooltipFlag flag) {

		super.appendHoverText(stack, level, tooltip, flag);

		tooltip.add(CommonComponents.EMPTY);
		tooltip.add(Component.translatable("tooltip." + EternalStarlight.ID + ".use")
			.withStyle(ChatFormatting.GRAY));

		tooltip.add(
			Component.literal(" ")
				.append(Component.translatable("tooltip." + EternalStarlight.ID + ".etheric_eye")
					.withStyle(s -> s.withColor(0xdafff1)))
		);
	}
}
