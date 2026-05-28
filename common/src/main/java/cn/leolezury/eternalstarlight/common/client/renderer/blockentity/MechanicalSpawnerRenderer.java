package cn.leolezury.eternalstarlight.common.client.renderer.blockentity;

import cn.leolezury.eternalstarlight.common.EternalStarlight;
import cn.leolezury.eternalstarlight.common.block.MechanicalSpawnerBlock;
import cn.leolezury.eternalstarlight.common.block.entity.MechanicalSpawner;
import cn.leolezury.eternalstarlight.common.block.entity.MechanicalSpawnerBlockEntity;
import cn.leolezury.eternalstarlight.common.client.model.animation.AnimatedModel;
import cn.leolezury.eternalstarlight.common.client.model.animation.definition.MechanicalSpawnerAnimation;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.SpawnerRenderer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public class MechanicalSpawnerRenderer implements BlockEntityRenderer<MechanicalSpawnerBlockEntity> {
	private static final ResourceLocation SPAWNER_TEXTURE = EternalStarlight.id("textures/entity/mechanical_spawner.png");

	private final EntityRenderDispatcher entityRenderer;
	private final MechanicalSpawnerModel spawnerModel;

	public MechanicalSpawnerRenderer(BlockEntityRendererProvider.Context context) {
		this.entityRenderer = context.getEntityRenderer();
		this.spawnerModel = new MechanicalSpawnerModel(context.bakeLayer(MechanicalSpawnerModel.LAYER_LOCATION));
	}

	@Override
	public void render(MechanicalSpawnerBlockEntity blockEntity, float partialTick, PoseStack stack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
		MechanicalSpawner spawner = blockEntity.getSpawner();

		stack.pushPose();
		stack.translate(0.0F, -1.0F, 0.0F);

		stack.pushPose();
		stack.scale(-1.0F, -1.0F, 1.0F);
		stack.translate(-0.5F, -1.501F, 0.5F);
		stack.scale(0.99F, 0.99F, 0.99F);
		stack.mulPose(Axis.YP.rotationDegrees(blockEntity.getBlockState().getValue(MechanicalSpawnerBlock.FACING).toYRot() + 180));

		this.spawnerModel.root().getAllParts().forEach(ModelPart::resetPose);
		this.spawnerModel.animate(
			blockEntity.idleAnimationState,
			MechanicalSpawnerAnimation.IDLE,
			blockEntity.clientTickCount + partialTick,
			1.0F,
			Mth.lerp(partialTick, spawner.getOAnimationScale(), spawner.getAnimationScale())
		);

		this.spawnerModel.renderToBuffer(
			stack,
			bufferSource.getBuffer(RenderType.entityCutoutNoCull(SPAWNER_TEXTURE)),
			packedLight,
			packedOverlay,
			1.0F, 1.0F, 1.0F, 1.0F
		);

		stack.popPose();

		Level level = blockEntity.getLevel();
		if (level != null) {
			Entity entity = spawner.getOrCreateDisplayEntity(level, blockEntity.getBlockPos());
			if (entity != null) {

				float spin = (float) spawner.getSpin();
				float oSpin = (float) spawner.getOSpin();
				float rotation = Mth.lerp(partialTick, oSpin, spin);

				stack.pushPose();

				stack.translate(0.5D, 0.0D, 0.5D);
				stack.scale(0.53125F, 0.53125F, 0.53125F);
				stack.translate(0.0D, 1.5D, 0.0D);
				stack.mulPose(Axis.YP.rotationDegrees(rotation));
				stack.mulPose(Axis.XP.rotationDegrees(-30.0F));

				EntityRenderDispatcher dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
				dispatcher.setRenderShadow(false);

				dispatcher.render(
					entity,
					0.0D, 0.0D, 0.0D,
					0.0F,
					partialTick,
					stack,
					bufferSource,
					packedLight
				);

				dispatcher.setRenderShadow(true);

				stack.popPose();
			}
		}

		stack.popPose();
	}

	public static final class MechanicalSpawnerModel extends Model implements AnimatedModel {
		public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(EternalStarlight.id("mechanical_spawner"), "main");

		public final ModelPart root;

		public MechanicalSpawnerModel(ModelPart root) {
			super(RenderType::entityCutoutNoCull);
			this.root = root.getChild("root");
		}

		public static LayerDefinition createLayer() {
			MeshDefinition meshdefinition = new MeshDefinition();
			PartDefinition partdefinition = meshdefinition.getRoot();

			PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, -32.0F, -8.0F, 16.0F, 32.0F, 16.0F, new CubeDeformation(0.0F))
				.texOffs(64, 19).addBox(-7.5F, -31.0F, -7.5F, 15.0F, 2.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

			PartDefinition side = root.addOrReplaceChild("side", CubeListBuilder.create().texOffs(0, 48).addBox(-8.0F, -9.0F, -8.0F, 16.0F, 8.0F, 16.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, -23.0F, 0.0F));

			side.addOrReplaceChild("side_stretch", CubeListBuilder.create().texOffs(64, 0).addBox(-7.5F, -4.0F, -7.5F, 15.0F, 4.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, 0.0F));

			return LayerDefinition.create(meshdefinition, 128, 128);
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
	public boolean shouldRenderOffScreen(MechanicalSpawnerBlockEntity blockEntity) {
		return true;
	}

	@Override
	public int getViewDistance() {
		return 256;
	}
}
