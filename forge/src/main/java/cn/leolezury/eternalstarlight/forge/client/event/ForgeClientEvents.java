package cn.leolezury.eternalstarlight.forge.client.event;

import cn.leolezury.eternalstarlight.common.EternalStarlight;
import cn.leolezury.eternalstarlight.common.client.handler.ESClientHandler;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ComputeFovModifierEvent;
import net.minecraftforge.client.event.CustomizeGuiOverlayEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = EternalStarlight.ID, value = Dist.CLIENT)
public class ForgeClientEvents {

	@SubscribeEvent
	public static void onClientTick(TickEvent.ClientTickEvent event) {
		if (event.phase == TickEvent.Phase.END) {
			ESClientHandler.onClientTick();
		}
	}

	@SubscribeEvent
	public static void onComputeCameraAngles(ViewportEvent.ComputeCameraAngles event) {
		Vec3 angle = ESClientHandler.onComputeCameraAngles(new Vec3(event.getPitch(), event.getYaw(), event.getRoll()));
		event.setPitch((float) angle.x);
		event.setYaw((float) angle.y);
	}

	@SubscribeEvent
	public static void onComputeFovModifier(ComputeFovModifierEvent event) {
		ESClientHandler.onComputeFovModifier(event.getFovModifier()).ifPresent(d -> event.setNewFovModifier((float) d));
	}

	@SubscribeEvent
	public static void onRenderFog(ViewportEvent.RenderFog event) {
		ESClientHandler.onRenderFog(event.getCamera(), event.getMode());
	}

	@SubscribeEvent
	public static void onRenderBossBar(CustomizeGuiOverlayEvent.BossEventProgress event) {
		if (ESClientHandler.renderBossBar(event.getGuiGraphics(), event.getBossEvent(), event.getX(), event.getY())) {
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public static void onRenderLevelStage(RenderLevelStageEvent event) {
		float partial = event.getPartialTick();

		if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_ENTITIES) {
			ESClientHandler.onAfterRenderEntities(event.getLevelRenderer().renderBuffers.bufferSource(), event.getPoseStack(), partial);
		}

		if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_PARTICLES) {
			ESClientHandler.onAfterRenderParticles();
		}

		if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_WEATHER) {
			ESClientHandler.onAfterRenderWeather(partial);
		}

		if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_LEVEL) {
			ESClientHandler.onAfterRenderLevel();
		}
	}
}
