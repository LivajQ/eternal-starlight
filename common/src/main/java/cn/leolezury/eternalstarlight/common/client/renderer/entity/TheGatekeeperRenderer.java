package cn.leolezury.eternalstarlight.common.client.renderer.entity;

import cn.leolezury.eternalstarlight.common.EternalStarlight;
import cn.leolezury.eternalstarlight.common.client.ESRenderType;
import cn.leolezury.eternalstarlight.common.client.model.ESModelUtil;
import cn.leolezury.eternalstarlight.common.client.model.entity.TheGatekeeperModel;
import cn.leolezury.eternalstarlight.common.client.renderer.layer.TheGatekeeperClothingLayer;
import cn.leolezury.eternalstarlight.common.entity.living.boss.gatekeeper.GatekeeperTeleportPhase;
import cn.leolezury.eternalstarlight.common.entity.living.boss.gatekeeper.TheGatekeeper;
import cn.leolezury.eternalstarlight.common.util.ModelPartPose;
import cn.leolezury.eternalstarlight.common.util.ModelSnapshot;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class TheGatekeeperRenderer<T extends TheGatekeeper> extends MobRenderer<T, TheGatekeeperModel<T>> {
	private static final ResourceLocation ENTITY_TEXTURE = EternalStarlight.id("textures/entity/the_gatekeeper.png");
	private static final ResourceLocation SLIM_ENTITY_TEXTURE = EternalStarlight.id("textures/entity/the_gatekeeper_slim.png");
	private static final int PHANTOM_AMOUNT = 5;
	private static final int SNAPSHOT_LIFESPAN = 15;
	private static final Map<String, GameProfile> PROFILES = new HashMap<>();
	private final TheGatekeeperModel<T> normalModel;
	private final TheGatekeeperModel<T> slimModel;

	private boolean renderingPhantom = false;

	public TheGatekeeperRenderer(EntityRendererProvider.Context context) {
		super(context, new TheGatekeeperModel<>(context.bakeLayer(TheGatekeeperModel.LAYER_LOCATION), false), 0.5f);
		normalModel = getModel();
		slimModel = new TheGatekeeperModel<>(context.bakeLayer(TheGatekeeperModel.SLIM_LAYER_LOCATION), true);
		this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()) {
			@Override
			public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T livingEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
				if (!renderingPhantom) {
					super.render(poseStack, buffer, packedLight, livingEntity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
				}
			}
		});
		this.addLayer(new TheGatekeeperClothingLayer<>(this, context.getModelSet()));
	}

	@Override
	public void render(T entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource bufferSource, int packedLight) {
		this.shadowRadius = entity.getBehaviorState() == GatekeeperTeleportPhase.ID ? 0.0F : 0.5F;
		getGameProfile(entity).ifPresent(p -> {
			boolean slim = DefaultPlayerSkin.getSkinModelName(p.getId()).equals("slim");
			model = slim ? slimModel : normalModel;
		});
		if (entity.getBehaviorState() == GatekeeperTeleportPhase.ID) {
			entity.teleportAnimationState.updateTime(entity.tickCount + partialTicks, 1);
			float progress = Math.min((1 - Math.abs(Mth.clamp((float) entity.teleportAnimationState.getAccumulatedTime() / 1000f * 20f, 0, 37) / 37 - 0.5f) * 2) * 1.5f, 1);
			float radius = progress * 0.5f;
			renderingPhantom = true;
			getModel().alphaFactor = 1 - progress;
			super.render(entity, entityYaw, partialTicks, stack, bufferSource, packedLight);
			getModel().alphaFactor = getModel().alphaFactor / 3;
			for (int i = 0; i < PHANTOM_AMOUNT; i++) {
				stack.pushPose();
				float spin = progress * Mth.HALF_PI + i * Mth.TWO_PI / PHANTOM_AMOUNT;
				Vector3f offset = new Vector3f(radius * Mth.sin(spin), radius * Mth.cos(spin), 0);
				offset.rotate(this.entityRenderDispatcher.cameraOrientation());
				stack.translate(offset.x, offset.y, offset.z);
				super.render(entity, entityYaw, partialTicks, stack, bufferSource, packedLight);
				stack.popPose();
			}
			renderingPhantom = false;
			getModel().alphaFactor = 1;
		} else {
			super.render(entity, entityYaw, partialTicks, stack, bufferSource, packedLight);
		}
		if (entity.isAlive()) {
			double currentX = Mth.lerp(partialTicks, entity.xo, entity.getX());
			double currentY = Mth.lerp(partialTicks, entity.yo, entity.getY());
			double currentZ = Mth.lerp(partialTicks, entity.zo, entity.getZ());
			float currentTick = getBob(entity, partialTicks);
			if (entity.trailSnapshots.isEmpty() || currentTick - entity.lastTrailTick > 3) {
				if (entity.shouldAddTrailSnapshot()) {
					Map<String, ModelPartPose> snapshot = ESModelUtil.saveModelSnapshot(getModel().allPartNames, getModel()::getAnyDescendantWithName);
					entity.trailSnapshots.addFirst(Pair.of(new Vec3(currentX, currentY, currentZ), new ModelSnapshot(0, Mth.rotLerp(partialTicks, entity.yBodyRotO, entity.yBodyRot), currentTick, snapshot)));
					entity.lastTrailTick = currentTick;
				}
				entity.trailSnapshots.removeIf(p -> currentTick - p.getSecond().timestamp() > SNAPSHOT_LIFESPAN);
				while (entity.trailSnapshots.size() > 32) {
					entity.trailSnapshots.removeLast();
				}
			}
			for (int i = 0; i < entity.trailSnapshots.size(); i++) {
				stack.pushPose();
				Vec3 trailPos = entity.trailSnapshots.get(i).getFirst();
				ModelSnapshot snapshot = entity.trailSnapshots.get(i).getSecond();
				ESModelUtil.loadPoseFromSnapshot(snapshot.poses(), getModel()::getAnyDescendantWithName);
				stack.translate(trailPos.x - currentX, trailPos.y - currentY, trailPos.z - currentZ);
				getModel().alphaFactor = (1 - Mth.clamp(currentTick - snapshot.timestamp(), 0, SNAPSHOT_LIFESPAN) / SNAPSHOT_LIFESPAN) * 0.3F;
				stack.mulPose(Axis.YP.rotationDegrees(180.0F - snapshot.yRot()));
				stack.scale(-1.0F, -1.0F, 1.0F);
				this.scale(entity, stack, partialTicks);
				stack.translate(0.0F, -1.5F, 0.0F);
				RenderType renderType = ESRenderType.entityTranslucentNoDepth(getTextureLocation(entity));
				VertexConsumer vertexConsumer = bufferSource.getBuffer(renderType);
				getModel().renderToBuffer(stack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
				stack.popPose();
			}
			getModel().alphaFactor = 1;
		}
	}

	@Override
	protected void renderNameTag(T entity, Component displayName, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
		if (!renderingPhantom) {
			super.renderNameTag(entity, displayName, poseStack, bufferSource, packedLight);
		}
	}

	/*
	@Override
	protected float getShadowRadius(T mob) {
		return mob.getBehaviorState() == GatekeeperTeleportPhase.ID ? 0 : super.getShadowRadius(mob);
	}
	 */

	@Override
	public ResourceLocation getTextureLocation(T entity) {
		ResourceLocation texture = model == slimModel ? SLIM_ENTITY_TEXTURE : ENTITY_TEXTURE;

		Optional<GameProfile> profile = getGameProfile(entity);
		if (profile.isPresent()) {
			SkinManager skinManager = Minecraft.getInstance().getSkinManager();
			Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> textures =
				skinManager.getInsecureSkinInformation(profile.get());

			MinecraftProfileTexture skin = textures.get(MinecraftProfileTexture.Type.SKIN);
			if (skin != null) {
				texture = skinManager.registerTexture(skin, MinecraftProfileTexture.Type.SKIN);
			}
		}

		return texture;
	}

	public static Optional<GameProfile> getGameProfile(TheGatekeeper entity) {
		if (entity.getCustomName() == null)
			return Optional.empty();

		String name = entity.getCustomName().getString();

		// Already loaded (or loading placeholder)
		if (PROFILES.containsKey(name)) {
			return Optional.ofNullable(PROFILES.get(name));
		}

		// Mark as loading to avoid duplicate requests
		PROFILES.put(name, null);

		GameProfile profile = new GameProfile(null, name);

		SkullBlockEntity.updateGameprofile(profile, updated -> {
			if (updated != null) {
				PROFILES.put(name, updated);
			}
		});

		return Optional.empty();
	}

}
