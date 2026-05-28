package cn.leolezury.eternalstarlight.common.client.renderer.blockentity;

import cn.leolezury.eternalstarlight.common.EternalStarlight;
import cn.leolezury.eternalstarlight.common.block.EnergyTransmitterBlock;
import cn.leolezury.eternalstarlight.common.block.entity.EnergyTransmitterBlockEntity;
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
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class EnergyTransmitterRenderer<T extends EnergyTransmitterBlockEntity> implements BlockEntityRenderer<T> {
	public static final ResourceLocation ENERGY_TRANSMITTER_TEXTURE = EternalStarlight.id("textures/entity/energy_transmitter.png");

	public EnergyTransmitterRenderer(BlockEntityRendererProvider.Context context) {

	}

	@Override
	public void render(T blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
		Vec3i inputOffset = blockEntity.getInputOffset();
		BlockState state = blockEntity.getBlockState();
		BlockState inputState = blockEntity.getInputState();
		Direction inputFacing = inputState.hasProperty(EnergyTransmitterBlock.FACING) ? inputState.getValue(EnergyTransmitterBlock.FACING) : Direction.UP;

		int receiverPower = state.hasProperty(EnergyTransmitterBlock.POWER)
			? state.getValue(EnergyTransmitterBlock.POWER)
			: 0;

		if (!inputOffset.equals(Vec3i.ZERO) && receiverPower > 0) {

			Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
			Vec3 sight = camera.getPosition().subtract(blockEntity.getBlockPos().getCenter());

			Vec3 start = new Vec3(0.5, 0.5, 0.5)
				.add(new Vec3(state.getValue(EnergyTransmitterBlock.FACING).step()).scale(-0.125));

			Vec3 end = Vec3.atCenterOf(inputOffset)
				.add(new Vec3(inputFacing.step()).scale(-0.125));

			Vec3 sideOffset = end.subtract(start).cross(sight).normalize().scale(1.0 / 32.0);

			PoseStack.Pose pose = poseStack.last();
			Matrix4f poseMat = pose.pose();
			Matrix3f normalMat = pose.normal();

			VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(ENERGY_TRANSMITTER_TEXTURE));

			float r = 1f, g = 1f, b = 1f, a = 1f;

			Vec3 p0 = start.add(sideOffset);
			Vec3 p1 = start.add(sideOffset.scale(-1));
			Vec3 p2 = end.add(sideOffset.scale(-1));
			Vec3 p3 = end.add(sideOffset);

			consumer.vertex(poseMat, (float)p0.x, (float)p0.y, (float)p0.z)
				.color(r, g, b, a)
				.uv(0f, 0f)
				.overlayCoords(OverlayTexture.NO_OVERLAY)
				.uv2(LightTexture.FULL_BRIGHT)
				.normal(normalMat, 0f, 1f, 0f)
				.endVertex();

			consumer.vertex(poseMat, (float)p1.x, (float)p1.y, (float)p1.z)
				.color(r, g, b, a)
				.uv(0f, 1f)
				.overlayCoords(OverlayTexture.NO_OVERLAY)
				.uv2(LightTexture.FULL_BRIGHT)
				.normal(normalMat, 0f, 1f, 0f)
				.endVertex();

			consumer.vertex(poseMat, (float)p2.x, (float)p2.y, (float)p2.z)
				.color(r, g, b, a)
				.uv(1f, 1f)
				.overlayCoords(OverlayTexture.NO_OVERLAY)
				.uv2(LightTexture.FULL_BRIGHT)
				.normal(normalMat, 0f, 1f, 0f)
				.endVertex();

			consumer.vertex(poseMat, (float)p3.x, (float)p3.y, (float)p3.z)
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
