package cn.leolezury.eternalstarlight.common.client.renderer.blockentity;

import cn.leolezury.eternalstarlight.common.block.ESPortalBlock;
import cn.leolezury.eternalstarlight.common.block.entity.ESPortalBlockEntity;
import cn.leolezury.eternalstarlight.common.client.ESRenderType;
import cn.leolezury.eternalstarlight.common.client.handler.ESClientHandler;
import cn.leolezury.eternalstarlight.common.config.ESConfig;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class ESPortalRenderer<T extends ESPortalBlockEntity> implements BlockEntityRenderer<T> {
	public ESPortalRenderer(BlockEntityRendererProvider.Context context) {

	}

	@Override
	public void render(T portal, float partialTicks, PoseStack stack, MultiBufferSource bufferSource, int light, int overlay) {
		if (ESConfig.enablePortalShader.get() && portal.getBlockState().getValue(ESPortalBlock.CENTER)) {
			VertexConsumer consumer = ESClientHandler.AFTER_LEVEL_BUFFER_SOURCE.getBuffer(ESRenderType.PORTAL);
			PoseStack.Pose pose = stack.last();
			Matrix4f poseMat = pose.pose();
			Matrix3f normalMat = pose.normal();

			float framePartial = Minecraft.getInstance().getFrameTime();
			float radius = 0.6f
				* portal.getBlockState().getValue(ESPortalBlock.SIZE)
				* (Math.min(portal.getClientSideTickCount() + framePartial, 60f) / 60f);

			float r = 1f, g = 1f, b = 1f, a = 1f;

			if (portal.getBlockState().getValue(ESPortalBlock.AXIS) == Direction.Axis.X) {
				consumer.vertex(poseMat, -radius, -radius, 0.5f)
					.color(r, g, b, a)
					.uv(0f, 0f)
					.overlayCoords(OverlayTexture.NO_OVERLAY)
					.uv2(LightTexture.FULL_BRIGHT)
					.normal(normalMat, 0f, 1f, 0f)
					.endVertex();

				consumer.vertex(poseMat, -radius, 1f + radius, 0.5f)
					.color(r, g, b, a)
					.uv(0f, 1f)
					.overlayCoords(OverlayTexture.NO_OVERLAY)
					.uv2(LightTexture.FULL_BRIGHT)
					.normal(normalMat, 0f, 1f, 0f)
					.endVertex();

				consumer.vertex(poseMat, 1f + radius, 1f + radius, 0.5f)
					.color(r, g, b, a)
					.uv(1f, 1f)
					.overlayCoords(OverlayTexture.NO_OVERLAY)
					.uv2(LightTexture.FULL_BRIGHT)
					.normal(normalMat, 0f, 1f, 0f)
					.endVertex();

				consumer.vertex(poseMat, 1f + radius, -radius, 0.5f)
					.color(r, g, b, a)
					.uv(1f, 0f)
					.overlayCoords(OverlayTexture.NO_OVERLAY)
					.uv2(LightTexture.FULL_BRIGHT)
					.normal(normalMat, 0f, 1f, 0f)
					.endVertex();
			} else {
				consumer.vertex(poseMat, 0.5f, -radius, -radius)
					.color(r, g, b, a)
					.uv(0f, 0f)
					.overlayCoords(OverlayTexture.NO_OVERLAY)
					.uv2(LightTexture.FULL_BRIGHT)
					.normal(normalMat, 0f, 1f, 0f)
					.endVertex();

				consumer.vertex(poseMat, 0.5f, 1f + radius, -radius)
					.color(r, g, b, a)
					.uv(0f, 1f)
					.overlayCoords(OverlayTexture.NO_OVERLAY)
					.uv2(LightTexture.FULL_BRIGHT)
					.normal(normalMat, 0f, 1f, 0f)
					.endVertex();

				consumer.vertex(poseMat, 0.5f, 1f + radius, 1f + radius)
					.color(r, g, b, a)
					.uv(1f, 1f)
					.overlayCoords(OverlayTexture.NO_OVERLAY)
					.uv2(LightTexture.FULL_BRIGHT)
					.normal(normalMat, 0f, 1f, 0f)
					.endVertex();

				consumer.vertex(poseMat, 0.5f, -radius, 1f + radius)
					.color(r, g, b, a)
					.uv(1f, 0f)
					.overlayCoords(OverlayTexture.NO_OVERLAY)
					.uv2(LightTexture.FULL_BRIGHT)
					.normal(normalMat, 0f, 1f, 0f)
					.endVertex();
			}
		}
	}
}
