package cn.leolezury.eternalstarlight.common.client.renderer.blockentity;

import cn.leolezury.eternalstarlight.common.block.entity.FlareSpawner;
import cn.leolezury.eternalstarlight.common.block.entity.FlareSpawnerBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public class FlareSpawnerRenderer implements BlockEntityRenderer<FlareSpawnerBlockEntity> {
	private final EntityRenderDispatcher entityRenderer;

	public FlareSpawnerRenderer(BlockEntityRendererProvider.Context context) {
		this.entityRenderer = context.getEntityRenderer();
	}

	@Override
	public void render(FlareSpawnerBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
		Level level = blockEntity.getLevel();
		if (level == null) return;

		FlareSpawner spawner = blockEntity.getSpawner();
		Entity entity = spawner.getOrCreateDisplayEntity(level, blockEntity.getBlockPos());
		if (entity == null) return;

		poseStack.pushPose();

		float spin = (float) spawner.getSpin();
		float oSpin = (float) spawner.getOSpin();
		float rotation = Mth.lerp(partialTick, oSpin, spin);

		poseStack.translate(0.5D, 0.0D, 0.5D);
		poseStack.scale(0.53125F, 0.53125F, 0.53125F);
		poseStack.translate(0.0D, 1.5D, 0.0D);
		poseStack.mulPose(Axis.YP.rotationDegrees(rotation));
		poseStack.mulPose(Axis.XP.rotationDegrees(-30.0F));

		EntityRenderDispatcher dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
		dispatcher.setRenderShadow(false);

		dispatcher.render(entity, 0.0D, 0.0D, 0.0D, 0.0F, partialTick, poseStack, bufferSource, packedLight);

		dispatcher.setRenderShadow(true);

		poseStack.popPose();
	}

}
