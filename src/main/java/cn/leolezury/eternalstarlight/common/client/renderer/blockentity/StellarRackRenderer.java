package cn.leolezury.eternalstarlight.common.client.renderer.blockentity;

import cn.leolezury.eternalstarlight.common.EternalStarlight;
import cn.leolezury.eternalstarlight.common.block.entity.StellarRackBlockEntity;
import cn.leolezury.eternalstarlight.common.client.ESRenderType;
import cn.leolezury.eternalstarlight.common.client.handler.ESClientHandler;
import cn.leolezury.eternalstarlight.common.util.ESMathUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

public class StellarRackRenderer<T extends StellarRackBlockEntity> implements BlockEntityRenderer<T> {
	private static final RenderType STAR = ESRenderType.entityTranslucentAdditiveGlow(EternalStarlight.id("textures/entity/stellar_rack_shine.png"));

	public StellarRackRenderer(BlockEntityRendererProvider.Context context) {

	}

	@Override
	public void render(T rack, float f, PoseStack stack, MultiBufferSource bufferSource, int light, int overlay) {
		VertexConsumer consumer = ESClientHandler.DELAYED_BUFFER_SOURCE.getBuffer(STAR);

		stack.pushPose();
		stack.translate(0.5F, 0.9F, 0.5F);

		stack.pushPose();
		stack.mulPose(new Quaternionf(Minecraft.getInstance().getEntityRenderDispatcher().cameraOrientation())
			.rotateZ(rack.getStarRotation(f)));

		PoseStack.Pose pose = stack.last();
		Matrix4f poseMat = pose.pose();
		Matrix3f normalMat = pose.normal();

		float size = (float) (1.2 + Math.sin((rack.getTickCount() + f) * 0.1 * Math.PI) * 0.4);

		int col1 = rack.getColor(f);
		float r1 = FastColor.ARGB32.red(col1) / 255f;
		float g1 = FastColor.ARGB32.green(col1) / 255f;
		float b1 = FastColor.ARGB32.blue(col1) / 255f;
		float a1 = FastColor.ARGB32.alpha(col1) / 255f;

		consumer.vertex(poseMat, -size, -size, 0f)
			.color(r1, g1, b1, a1)
			.uv(0f, 0f)
			.overlayCoords(OverlayTexture.NO_OVERLAY)
			.uv2(LightTexture.FULL_BRIGHT)
			.normal(normalMat, 0f, 1f, 0f)
			.endVertex();

		consumer.vertex(poseMat, -size,  size, 0f)
			.color(r1, g1, b1, a1)
			.uv(0f, 1f)
			.overlayCoords(OverlayTexture.NO_OVERLAY)
			.uv2(LightTexture.FULL_BRIGHT)
			.normal(normalMat, 0f, 1f, 0f)
			.endVertex();

		consumer.vertex(poseMat,  size,  size, 0f)
			.color(r1, g1, b1, a1)
			.uv(1f, 1f)
			.overlayCoords(OverlayTexture.NO_OVERLAY)
			.uv2(LightTexture.FULL_BRIGHT)
			.normal(normalMat, 0f, 1f, 0f)
			.endVertex();

		consumer.vertex(poseMat,  size, -size, 0f)
			.color(r1, g1, b1, a1)
			.uv(1f, 0f)
			.overlayCoords(OverlayTexture.NO_OVERLAY)
			.uv2(LightTexture.FULL_BRIGHT)
			.normal(normalMat, 0f, 1f, 0f)
			.endVertex();

		stack.popPose();

		stack.translate(0.01F, 0.01F, 0.01F);

		stack.pushPose();
		stack.mulPose(new Quaternionf(Minecraft.getInstance().getEntityRenderDispatcher().cameraOrientation())
			.rotateZ(-rack.getStarRotation(f) / 2f));

		pose = stack.last();
		poseMat = pose.pose();
		normalMat = pose.normal();

		size = (float) (0.9 + Math.sin((rack.getTickCount() + f) * 1.2 * Math.PI) * 0.2);

		int col2 = rack.getColor(100 + f);
		float r2 = FastColor.ARGB32.red(col2) / 255f;
		float g2 = FastColor.ARGB32.green(col2) / 255f;
		float b2 = FastColor.ARGB32.blue(col2) / 255f;
		float a2 = FastColor.ARGB32.alpha(col2) / 255f;

		consumer.vertex(poseMat, -size, -size, 0f)
			.color(r2, g2, b2, a2)
			.uv(0f, 0f)
			.overlayCoords(OverlayTexture.NO_OVERLAY)
			.uv2(LightTexture.FULL_BRIGHT)
			.normal(normalMat, 0f, 1f, 0f)
			.endVertex();

		consumer.vertex(poseMat, -size,  size, 0f)
			.color(r2, g2, b2, a2)
			.uv(0f, 1f)
			.overlayCoords(OverlayTexture.NO_OVERLAY)
			.uv2(LightTexture.FULL_BRIGHT)
			.normal(normalMat, 0f, 1f, 0f)
			.endVertex();

		consumer.vertex(poseMat,  size,  size, 0f)
			.color(r2, g2, b2, a2)
			.uv(1f, 1f)
			.overlayCoords(OverlayTexture.NO_OVERLAY)
			.uv2(LightTexture.FULL_BRIGHT)
			.normal(normalMat, 0f, 1f, 0f)
			.endVertex();

		consumer.vertex(poseMat,  size, -size, 0f)
			.color(r2, g2, b2, a2)
			.uv(1f, 0f)
			.overlayCoords(OverlayTexture.NO_OVERLAY)
			.uv2(LightTexture.FULL_BRIGHT)
			.normal(normalMat, 0f, 1f, 0f)
			.endVertex();

		stack.popPose();

		ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();
		long nonEmpty = rack.getItems().stream().filter(i -> !i.isEmpty()).count();
		float angleStep = nonEmpty == 0 ? 0f : 360f / (float) nonEmpty;
		float accumulatedAngle = rack.getStarRotation(f) * 0.4f * Mth.RAD_TO_DEG;

		for (ItemStack itemStack : rack.getItems()) {
			if (!itemStack.isEmpty()) {
				accumulatedAngle += angleStep;
				stack.pushPose();

				Vec3 pos = ESMathUtil.rotationToPosition(0.9f, 0, accumulatedAngle);
				stack.translate(pos.x, 0, pos.z);
				stack.mulPose(new Quaternionf().rotateY(rack.getStarRotation(f) * 0.6f));

				renderer.render(
					itemStack,
					ItemDisplayContext.GROUND,
					false,
					stack,
					bufferSource,
					LightTexture.FULL_BRIGHT,
					OverlayTexture.NO_OVERLAY,
					renderer.getModel(itemStack, rack.getLevel(), null, 0)
				);

				stack.popPose();
			}
		}

		stack.popPose();
	}

}
