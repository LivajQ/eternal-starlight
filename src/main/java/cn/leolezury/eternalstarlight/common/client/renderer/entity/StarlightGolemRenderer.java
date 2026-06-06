package cn.leolezury.eternalstarlight.common.client.renderer.entity;

import cn.leolezury.eternalstarlight.common.EternalStarlight;
import cn.leolezury.eternalstarlight.common.client.ESRenderType;
import cn.leolezury.eternalstarlight.common.client.model.ESModelUtil;
import cn.leolezury.eternalstarlight.common.client.model.entity.StarlightGolemModel;
import cn.leolezury.eternalstarlight.common.client.renderer.layer.StarlightGolemEyesLayer;
import cn.leolezury.eternalstarlight.common.client.renderer.layer.StarlightGolemGlowLayer;
import cn.leolezury.eternalstarlight.common.client.renderer.layer.StarlightGolemHalloweenLayer;
import cn.leolezury.eternalstarlight.common.entity.living.boss.golem.StarlightGolem;
import cn.leolezury.eternalstarlight.common.util.Easing;
import cn.leolezury.eternalstarlight.common.util.ModelPartPose;
import cn.leolezury.eternalstarlight.common.util.ModelSnapshot;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

import java.util.Map;

public class StarlightGolemRenderer<T extends StarlightGolem> extends MobRenderer<T, StarlightGolemModel<T>> {
	private static final int SNAPSHOT_LIFESPAN = 15;

	private static final ResourceLocation ENTITY_TEXTURE = EternalStarlight.id("textures/entity/starlight_golem/starlight_golem.png");
	private static final ResourceLocation CRACKED_TEXTURE = EternalStarlight.id("textures/entity/starlight_golem/starlight_golem_cracked.png");

	public StarlightGolemRenderer(EntityRendererProvider.Context context) {
		super(context, new StarlightGolemModel<>(context.bakeLayer(StarlightGolemModel.LAYER_LOCATION)), 0.5f);
		this.addLayer(new StarlightGolemHalloweenLayer<>(this, context.getModelSet()));
		this.addLayer(new StarlightGolemGlowLayer<>(this, context.getModelSet()));
		this.addLayer(new StarlightGolemEyesLayer<>(this));
	}

	@Override
	public void render(T entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
		float deathProgress = Mth.clamp(Mth.lerp(partialTicks, entity.oldDeathAnimationTime, entity.deathAnimationTime) / 110, 0, 1);
		if (deathProgress > 0) {
			poseStack.pushPose();
			poseStack.translate(0, entity.getBbHeight() / 4, 0);
			poseStack.scale(deathProgress + Mth.cos((entity.tickCount + partialTicks) * 4f) * 0.2f * deathProgress, deathProgress + Mth.cos((entity.tickCount + partialTicks) * 4f) * 0.2f * deathProgress, deathProgress + Mth.cos((entity.tickCount + partialTicks) * 4f) * 0.2f * deathProgress);
			poseStack.mulPose(new Quaternionf(this.entityRenderDispatcher.cameraOrientation()).rotateY(Mth.PI));
			PoseStack.Pose pose = poseStack.last();
			//VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.dragonRays());
			//TODO also check if fits
			VertexConsumer vertexConsumer = buffer.getBuffer(ESRenderType.entityTranslucentGlow(getTextureLocation(entity)));

			Matrix4f poseMat = pose.pose();
			Matrix3f normalMat = pose.normal();


			for (int i = 0; i < 5; i++) {
				float aF = Easing.IN_OUT_SINE.interpolate(deathProgress, 0.5F, 1.0F);
				float rF = 1.0F;
				float gF = 1.0F;
				float bF = 1.0F;

				int a = (int)(aF * 255f);
				int r = (int)(rF * 255f);
				int g = (int)(gF * 255f);
				int b = (int)(bF * 255f);

				int c0 = FastColor.ARGB32.color(a, r, g, b);

				float r0 = FastColor.ARGB32.red(c0) / 255f;
				float g0 = FastColor.ARGB32.green(c0) / 255f;
				float b0 = FastColor.ARGB32.blue(c0) / 255f;
				float a0 = FastColor.ARGB32.alpha(c0) / 255f;

				vertexConsumer.vertex(poseMat, 0f, 0f, 0f)
					.color(r0, g0, b0, a0)
					.uv(0f, 0f)
					.overlayCoords(OverlayTexture.NO_OVERLAY)
					.uv2(LightTexture.FULL_BRIGHT)
					.normal(normalMat, 0f, 1f, 0f)
					.endVertex();

				float angle = i * Mth.TWO_PI / 5 + deathProgress * Mth.PI * 1.5f;

				int c1 = FastColor.ARGB32.color(0, 64, 106, 125);
				float r1 = FastColor.ARGB32.red(c1) / 255f;
				float g1 = FastColor.ARGB32.green(c1) / 255f;
				float b1 = FastColor.ARGB32.blue(c1) / 255f;
				float a1 = FastColor.ARGB32.alpha(c1) / 255f;

				float x1 = Mth.sin(angle) * entity.getBbHeight() * 3;
				float y1 = Mth.cos(angle) * entity.getBbHeight() * 3;

				vertexConsumer.vertex(poseMat, x1, y1, 0f)
					.color(r1, g1, b1, a1)
					.uv(0f, 1f)
					.overlayCoords(OverlayTexture.NO_OVERLAY)
					.uv2(LightTexture.FULL_BRIGHT)
					.normal(normalMat, 0f, 1f, 0f)
					.endVertex();

				float largerAngle = angle + deathProgress * Mth.TWO_PI / 8;
				float x2 = Mth.sin(largerAngle) * entity.getBbHeight() * 3;
				float y2 = Mth.cos(largerAngle) * entity.getBbHeight() * 3;

				vertexConsumer.vertex(poseMat, x2, y2, 0f)
					.color(r1, g1, b1, a1)
					.uv(1f, 1f)
					.overlayCoords(OverlayTexture.NO_OVERLAY)
					.uv2(LightTexture.FULL_BRIGHT)
					.normal(normalMat, 0f, 1f, 0f)
					.endVertex();
			}

			poseStack.popPose();
		}
		super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
		if (entity.isAlive()) {
			double currentX = Mth.lerp(partialTicks, entity.xo, entity.getX());
			double currentY = Mth.lerp(partialTicks, entity.yo, entity.getY());
			double currentZ = Mth.lerp(partialTicks, entity.zo, entity.getZ());
			float currentTick = getBob(entity, partialTicks);
			if (entity.trailSnapshots.isEmpty() || currentTick - entity.lastTrailTick > 4) {
				if (entity.shouldAddTrailSnapshot()) {
					Map<String, ModelPartPose> snapshot = ESModelUtil.saveModelSnapshot(getModel().allPartNames, getModel()::getAnyDescendantWithName);
					entity.trailSnapshots.add(0, Pair.of(new Vec3(currentX, currentY, currentZ), new ModelSnapshot(0, Mth.rotLerp(partialTicks, entity.yBodyRotO, entity.yBodyRot), currentTick, snapshot)));
					entity.lastTrailTick = currentTick;
				}
				entity.trailSnapshots.removeIf(p -> currentTick - p.getSecond().timestamp() > SNAPSHOT_LIFESPAN);
				while (entity.trailSnapshots.size() > 32) {
					entity.trailSnapshots.remove(entity.trailSnapshots.size() - 1);
				}
			}
			for (int i = 0; i < entity.trailSnapshots.size(); i++) {
				poseStack.pushPose();
				Vec3 trailPos = entity.trailSnapshots.get(i).getFirst();
				ModelSnapshot snapshot = entity.trailSnapshots.get(i).getSecond();
				ESModelUtil.loadPoseFromSnapshot(snapshot.poses(), getModel()::getAnyDescendantWithName);
				poseStack.translate(trailPos.x - currentX, trailPos.y - currentY, trailPos.z - currentZ);
				getModel().alphaFactor = (1 - Mth.clamp(currentTick - snapshot.timestamp(), 0, SNAPSHOT_LIFESPAN) / SNAPSHOT_LIFESPAN) * 0.3F;
				poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - snapshot.yRot()));
				poseStack.scale(-1.0F, -1.0F, 1.0F);
				this.scale(entity, poseStack, partialTicks);
				poseStack.translate(0.0F, -1.5F, 0.0F);
				RenderType renderType = ESRenderType.entityTranslucentNoDepth(getTextureLocation(entity));
				VertexConsumer vertexConsumer = buffer.getBuffer(renderType);
				getModel().renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
				poseStack.popPose();
			}
			getModel().alphaFactor = 1;
		}
	}

	@Override
	protected float getFlipDegrees(T entity) {
		return 0;
	}

	@Override
	protected float getWhiteOverlayProgress(T entity, float partialTicks) {
		float deathProgress = Mth.lerp(partialTicks, entity.oldDeathAnimationTime, entity.deathAnimationTime) / 100;
		if (deathProgress <= 0) return 0;
		return (int) (deathProgress * 45.0) % 2 == 0 ? 0.0F : Mth.clamp(deathProgress, 0.2F, 1.0F);
	}

	@Override
	public ResourceLocation getTextureLocation(T entity) {
		return entity.getPhase() == 0 ? ENTITY_TEXTURE : CRACKED_TEXTURE;
	}
}
