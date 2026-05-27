package cn.leolezury.eternalstarlight.common.client.model.entity;

import cn.leolezury.eternalstarlight.common.entity.living.monster.LonestarSkeleton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.SkeletonModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class LonestarSkeletonModel<T extends LonestarSkeleton> extends SkeletonModel<T> {
	private final ModelPart innerHead;

	public LonestarSkeletonModel(ModelPart root) {
		super(root);
		if (getHead().hasChild("inner_head")) {
			this.innerHead = getHead().getChild("inner_head");
		} else {
			this.innerHead = null;
		}
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = SkeletonModel.createMesh(CubeDeformation.NONE, 0.0F);
		PartDefinition root = mesh.getRoot();
		root.getChild("head").addOrReplaceChild("inner_head", CubeListBuilder.create().texOffs(0, 32).addBox(-3.5F, -3.5F, -3.5F, 7.0F, 7.0F, 7.0F), PartPose.offset(0.0F, -4.0F, 0.0F));
		root.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(48, 16).addBox(-1, -2, -1, 2, 6, 2).texOffs(48, 24).addBox(-1, 4, -1, 2, 2, 2), PartPose.offset(-5.0F, 2.0F, 0.0F));
		return LayerDefinition.create(mesh, 64, 64);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		if (innerHead != null) {
			innerHead.resetPose();
			if (!Minecraft.getInstance().isPaused()) {
				innerHead.x += (float) (entity.getRandom().nextGaussian() * 0.25);
				innerHead.y += (float) (entity.getRandom().nextGaussian() * 0.25);
			}
		}
	}
}
