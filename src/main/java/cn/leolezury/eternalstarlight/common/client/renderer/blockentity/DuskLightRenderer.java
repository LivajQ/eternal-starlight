package cn.leolezury.eternalstarlight.common.client.renderer.blockentity;

import cn.leolezury.eternalstarlight.common.EternalStarlight;
import cn.leolezury.eternalstarlight.common.block.entity.AbstractDuskLightBlockEntity;
import cn.leolezury.eternalstarlight.common.block.entity.DuskLightBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class DuskLightRenderer<T extends AbstractDuskLightBlockEntity> implements BlockEntityRenderer<T> {
	public static final ResourceLocation DUSK_BEAM_TEXTURE = EternalStarlight.id("textures/entity/dusk_beam.png");

	public DuskLightRenderer(BlockEntityRendererProvider.Context context) {

	}

	@Override
	public void render(T blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
		for (Direction direction : Direction.values()) {
			float progress = Mth.lerp(partialTicks, blockEntity.getOldBeamProgresses().getOrDefault(direction, 0), blockEntity.getBeamProgresses().getOrDefault(direction, 0));
			Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
			/*float yaw = camera.getYRot() + 90;
			float pitch = -camera.getXRot();
			Vec3 sight = ESMathUtil.rotationToPosition(1, pitch, yaw);*/
			Vec3 sight = camera.getPosition().subtract(blockEntity.getBlockPos().getCenter());
			Vec3 start = new Vec3(0.5, 0.5, 0.5).add(new Vec3(direction.getStepX(), direction.getStepY(), direction.getStepZ()).scale(0.5));
			Vec3 end = start.add(new Vec3(direction.getStepX(), direction.getStepY(), direction.getStepZ()).scale(progress * DuskLightBlockEntity.MAX_LENGTH));
			Vec3 sideOffset = end.subtract(start).cross(sight).normalize().scale(0.25);
			PoseStack.Pose pose = poseStack.last();
			VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(DUSK_BEAM_TEXTURE));
			Matrix4f poseMat = pose.pose();
			Matrix3f normalMat = pose.normal();

			float r = 1f, g = 1f, b = 1f, a = 1f;

			Vec3 p0 = start.add(sideOffset);
			Vec3 p1 = start.add(sideOffset.scale(-1));
			Vec3 p2 = end.add(sideOffset.scale(-1));
			Vec3 p3 = end.add(sideOffset);

			vertexConsumer.vertex(poseMat, (float)p0.x, (float)p0.y, (float)p0.z)
				.color(r, g, b, a)
				.uv(0f, 0f)
				.overlayCoords(OverlayTexture.NO_OVERLAY)
				.uv2(LightTexture.FULL_BRIGHT)
				.normal(normalMat, 0f, 1f, 0f)
				.endVertex();

			vertexConsumer.vertex(poseMat, (float)p1.x, (float)p1.y, (float)p1.z)
				.color(r, g, b, a)
				.uv(0f, 1f)
				.overlayCoords(OverlayTexture.NO_OVERLAY)
				.uv2(LightTexture.FULL_BRIGHT)
				.normal(normalMat, 0f, 1f, 0f)
				.endVertex();

			vertexConsumer.vertex(poseMat, (float)p2.x, (float)p2.y, (float)p2.z)
				.color(r, g, b, a)
				.uv(1f, 1f)
				.overlayCoords(OverlayTexture.NO_OVERLAY)
				.uv2(LightTexture.FULL_BRIGHT)
				.normal(normalMat, 0f, 1f, 0f)
				.endVertex();

			vertexConsumer.vertex(poseMat, (float)p3.x, (float)p3.y, (float)p3.z)
				.color(r, g, b, a)
				.uv(1f, 0f)
				.overlayCoords(OverlayTexture.NO_OVERLAY)
				.uv2(LightTexture.FULL_BRIGHT)
				.normal(normalMat, 0f, 1f, 0f)
				.endVertex();
		}
	}

	@Override
	public boolean shouldRenderOffScreen(T blockEntity) {
		return true;
	}

	@Override
	public int getViewDistance() {
		return 256;
	}
}
