package cn.leolezury.eternalstarlight.common.client.renderer.blockentity;

import cn.leolezury.eternalstarlight.common.EternalStarlight;
import cn.leolezury.eternalstarlight.common.block.LootChestBlock;
import cn.leolezury.eternalstarlight.common.block.entity.LootChestBlockEntity;
import cn.leolezury.eternalstarlight.common.client.ESRenderType;
import cn.leolezury.eternalstarlight.common.client.model.animation.AnimatedModel;
import cn.leolezury.eternalstarlight.common.client.model.animation.definition.LootChestAnimation;
import cn.leolezury.eternalstarlight.common.util.Easing;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class LootChestRenderer implements BlockEntityRenderer<LootChestBlockEntity> {
	private final LootChestModel chestModel;

	private static final ResourceLocation CHEST_TEXTURE = EternalStarlight.id("textures/entity/loot_chest/loot_chest.png");
	private static final ResourceLocation CHEST_OUTLINE_TEXTURE = EternalStarlight.id("textures/entity/loot_chest/loot_chest_outline.png");

	public LootChestRenderer(BlockEntityRendererProvider.Context context) {
		this.chestModel = new LootChestModel(context.bakeLayer(LootChestModel.LAYER_LOCATION));
	}

	@Override
	public void render(LootChestBlockEntity blockEntity, float partialTicks, PoseStack stack, MultiBufferSource bufferSource, int light, int overlay) {
		stack.pushPose();
		stack.scale(-1.0F, -1.0F, 1.0F);
		stack.translate(-0.5F, -1.5F, 0.5F);
		stack.mulPose(Axis.YP.rotationDegrees(blockEntity.getBlockState().getValue(LootChestBlock.FACING).toYRot() + 180));
		this.chestModel.root().getAllParts().forEach(ModelPart::resetPose);
		this.chestModel.animate(blockEntity.openAnimationState, LootChestAnimation.OPEN, blockEntity.clientTickCount + partialTicks);
		this.chestModel.animate(blockEntity.closeAnimationState, LootChestAnimation.CLOSE, blockEntity.clientTickCount + partialTicks);
		if (blockEntity.isEjecting() && !blockEntity.openAnimationState.isStarted() && !blockEntity.closeAnimationState.isStarted()) {
			this.chestModel.lid.xRot = -Mth.HALF_PI;
		}

		int color = 0xFF000000 | blockEntity.getColor();

		float r = FastColor.ARGB32.red(color) / 255f;
		float g = FastColor.ARGB32.green(color) / 255f;
		float b = FastColor.ARGB32.blue(color) / 255f;
		float a = FastColor.ARGB32.alpha(color) / 255f;

		this.chestModel.renderToBuffer(stack, bufferSource.getBuffer(RenderType.entityCutoutNoCull(CHEST_TEXTURE)), light, overlay, r, g, b, a);

		int outline = 0xFF000000 | blockEntity.getOutlineColor();

		float or = FastColor.ARGB32.red(outline) / 255f;
		float og = FastColor.ARGB32.green(outline) / 255f;
		float ob = FastColor.ARGB32.blue(outline) / 255f;
		float oa = FastColor.ARGB32.alpha(outline) / 255f;

		this.chestModel.renderToBuffer(stack, bufferSource.getBuffer(RenderType.entityCutoutNoCull(CHEST_OUTLINE_TEXTURE)), light, overlay, or, og, ob, oa);

		stack.popPose();
		float flashAnimation = Math.max((blockEntity.clientTickCount + partialTicks) - blockEntity.flashStartTickCount, 0);
		VertexConsumer consumer = bufferSource.getBuffer(ESRenderType.DRAGON_RAYS_QUADS);
		PoseStack.Pose pose = stack.last();
		if (flashAnimation < 10) {
			float flashProgress = Easing.IN_OUT_SINE.calculate(1 - Math.abs(flashAnimation - 5) / 5);
			float flashDistFromCenter = Mth.lerp(flashProgress, flashAnimation < 5 ? 0.2f : 0.25f, 0.15f);
			float flashHeight = 0.6f * flashProgress;

			int rgb = blockEntity.rareFlash ? blockEntity.getRareFlashColor() : blockEntity.getFlashColor();
			int alphaBase = Math.round(Mth.lerp(flashProgress, 0.5F, 1.0F) * 255);
			int baseColor = FastColor.ARGB32.color(alphaBase, FastColor.ARGB32.red(rgb), FastColor.ARGB32.green(rgb), FastColor.ARGB32.blue(rgb));
			int topColor = FastColor.ARGB32.color(0, FastColor.ARGB32.red(rgb), FastColor.ARGB32.green(rgb), FastColor.ARGB32.blue(rgb));

			Matrix4f poseMat = pose.pose();
			Matrix3f normalMat = pose.normal();

			float baseR = FastColor.ARGB32.red(baseColor) / 255f;
			float baseG = FastColor.ARGB32.green(baseColor) / 255f;
			float baseB = FastColor.ARGB32.blue(baseColor) / 255f;
			float baseA = FastColor.ARGB32.alpha(baseColor) / 255f;

			float topR = FastColor.ARGB32.red(topColor) / 255f;
			float topG = FastColor.ARGB32.green(topColor) / 255f;
			float topB = FastColor.ARGB32.blue(topColor) / 255f;
			float topA = FastColor.ARGB32.alpha(topColor) / 255f;

			float x0 = 0.5f - flashDistFromCenter;
			float x1 = 0.5f + flashDistFromCenter;
			float y0 = 0.5625f;
			float y1 = 0.5625f + flashHeight;
			float z0 = 0.5f - flashDistFromCenter;
			float z1 = 0.5f + flashDistFromCenter;

			consumer.vertex(poseMat, x0, y0, z0).color(baseR, baseG, baseB, baseA).uv(0,0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BRIGHT).normal(normalMat,0,1,0).endVertex();
			consumer.vertex(poseMat, x0, y0, z1).color(baseR, baseG, baseB, baseA).uv(0,1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BRIGHT).normal(normalMat,0,1,0).endVertex();
			consumer.vertex(poseMat, x0, y1, z1).color(topR, topG, topB, topA).uv(1,1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BRIGHT).normal(normalMat,0,1,0).endVertex();
			consumer.vertex(poseMat, x0, y1, z0).color(topR, topG, topB, topA).uv(1,0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BRIGHT).normal(normalMat,0,1,0).endVertex();

			consumer.vertex(poseMat, x0, y0, z0).color(baseR, baseG, baseB, baseA).uv(0,0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BRIGHT).normal(normalMat,0,1,0).endVertex();
			consumer.vertex(poseMat, x1, y0, z0).color(baseR, baseG, baseB, baseA).uv(0,1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BRIGHT).normal(normalMat,0,1,0).endVertex();
			consumer.vertex(poseMat, x1, y1, z0).color(topR, topG, topB, topA).uv(1,1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BRIGHT).normal(normalMat,0,1,0).endVertex();
			consumer.vertex(poseMat, x0, y1, z0).color(topR, topG, topB, topA).uv(1,0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BRIGHT).normal(normalMat,0,1,0).endVertex();

			consumer.vertex(poseMat, x1, y0, z0).color(baseR, baseG, baseB, baseA).uv(0,0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BRIGHT).normal(normalMat,0,1,0).endVertex();
			consumer.vertex(poseMat, x1, y0, z1).color(baseR, baseG, baseB, baseA).uv(0,1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BRIGHT).normal(normalMat,0,1,0).endVertex();
			consumer.vertex(poseMat, x1, y1, z1).color(topR, topG, topB, topA).uv(1,1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BRIGHT).normal(normalMat,0,1,0).endVertex();
			consumer.vertex(poseMat, x1, y1, z0).color(topR, topG, topB, topA).uv(1,0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BRIGHT).normal(normalMat,0,1,0).endVertex();

			consumer.vertex(poseMat, x0, y0, z1).color(baseR, baseG, baseB, baseA).uv(0,0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BRIGHT).normal(normalMat,0,1,0).endVertex();
			consumer.vertex(poseMat, x1, y0, z1).color(baseR, baseG, baseB, baseA).uv(0,1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BRIGHT).normal(normalMat,0,1,0).endVertex();
			consumer.vertex(poseMat, x1, y1, z1).color(topR, topG, topB, topA).uv(1,1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BRIGHT).normal(normalMat,0,1,0).endVertex();
			consumer.vertex(poseMat, x0, y1, z1).color(topR, topG, topB, topA).uv(1,0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BRIGHT).normal(normalMat,0,1,0).endVertex();
		}
	}

	public static final class LootChestModel extends Model implements AnimatedModel {
		public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(EternalStarlight.id("loot_chest"), "main");

		public final ModelPart root;
		public final ModelPart lid;

		public LootChestModel(ModelPart root) {
			super(RenderType::entityCutoutNoCull);
			this.root = root.getChild("root");
			this.lid = this.root.getChild("lid");
		}

		public static LayerDefinition createLayer() {
			MeshDefinition meshdefinition = new MeshDefinition();
			PartDefinition partdefinition = meshdefinition.getRoot();

			PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create().texOffs(0, 21).addBox(-7.0F, -9.0F, -15.0F, 14.0F, 8.0F, 14.0F, new CubeDeformation(0.0F))
				.texOffs(0, 43).addBox(-8.0F, -10.0F, -16.0F, 3.0F, 10.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(0, 43).mirror().addBox(5.0F, -10.0F, -16.0F, 3.0F, 10.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 43).addBox(-8.0F, -10.0F, -3.0F, 3.0F, 10.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(0, 43).mirror().addBox(5.0F, -10.0F, -3.0F, 3.0F, 10.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 24.0F, 8.0F));

			root.addOrReplaceChild("lid", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, -7.0F, -14.0F, 14.0F, 7.0F, 14.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, -8.0F, -1.0F));

			return LayerDefinition.create(meshdefinition, 64, 64);
		}

		@Override
		public ModelPart root() {
			return root;
		}

		@Override
		public void renderToBuffer(PoseStack stack, VertexConsumer consumer, int light, int overlay, float r, float g, float b, float a) {
			this.root.render(stack, consumer, light, overlay, r, g, b, a);
		}
	}

	@Override
	public boolean shouldRenderOffScreen(LootChestBlockEntity blockEntity) {
		return true;
	}

	@Override
	public int getViewDistance() {
		return 256;
	}
}
