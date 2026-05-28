package cn.leolezury.eternalstarlight.common.client.renderer.blockentity;

import cn.leolezury.eternalstarlight.common.block.entity.EclipseCoreBlockEntity;
import cn.leolezury.eternalstarlight.common.client.ESRenderType;
import cn.leolezury.eternalstarlight.common.client.handler.ESClientHandler;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class EclipseCoreRenderer extends DuskLightRenderer<EclipseCoreBlockEntity> {
	public EclipseCoreRenderer(BlockEntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public void render(EclipseCoreBlockEntity blockEntity, float f, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j) {
		super.render(blockEntity, f, poseStack, multiBufferSource, i, j);
		VertexConsumer vertexConsumer = ESClientHandler.AFTER_LEVEL_BUFFER_SOURCE.getBuffer(ESRenderType.ECLIPSE);
		PoseStack.Pose pose = poseStack.last();
		float height = 0.5f + 7 * blockEntity.getEclipseProgress(f);
		float size = 32 * blockEntity.getEclipseProgress(f);
		float x0 = size + 1, z0 = size + 1;
		float x1 = size + 1, z1 = -size;
		float x2 = -size,    z2 = -size;
		float x3 = -size,    z3 = size + 1;

		Matrix4f poseMat = pose.pose();
		Matrix3f normalMat = pose.normal();

		float r = 1f, g = 1f, b = 1f, a = 1f;

		vertexConsumer.vertex(poseMat, x0, height, z0)
			.color(r, g, b, a)
			.uv(0f, 0f)
			.overlayCoords(OverlayTexture.NO_OVERLAY)
			.uv2(LightTexture.FULL_BRIGHT)
			.normal(normalMat, 0f, 1f, 0f)
			.endVertex();

		vertexConsumer.vertex(poseMat, x1, height, z1)
			.color(r, g, b, a)
			.uv(0f, 1f)
			.overlayCoords(OverlayTexture.NO_OVERLAY)
			.uv2(LightTexture.FULL_BRIGHT)
			.normal(normalMat, 0f, 1f, 0f)
			.endVertex();

		vertexConsumer.vertex(poseMat, x2, height, z2)
			.color(r, g, b, a)
			.uv(1f, 1f)
			.overlayCoords(OverlayTexture.NO_OVERLAY)
			.uv2(LightTexture.FULL_BRIGHT)
			.normal(normalMat, 0f, 1f, 0f)
			.endVertex();

		vertexConsumer.vertex(poseMat, x3, height, z3)
			.color(r, g, b, a)
			.uv(1f, 0f)
			.overlayCoords(OverlayTexture.NO_OVERLAY)
			.uv2(LightTexture.FULL_BRIGHT)
			.normal(normalMat, 0f, 1f, 0f)
			.endVertex();
	}

	@Override
	public boolean shouldRenderOffScreen(EclipseCoreBlockEntity blockEntity) {
		return true;
	}

	@Override
	public int getViewDistance() {
		return 256;
	}
}
