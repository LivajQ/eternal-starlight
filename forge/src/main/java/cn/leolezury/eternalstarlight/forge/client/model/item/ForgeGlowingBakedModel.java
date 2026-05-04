package cn.leolezury.eternalstarlight.forge.client.model.item;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.BakedModelWrapper;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ForgeGlowingBakedModel extends BakedModelWrapper<BakedModel>
{
	private static final int FULLBRIGHT = 0xF000F0; // 15728880

	public ForgeGlowingBakedModel(BakedModel original) {
		super(original);
	}

	@Override
	public List<BakedQuad> getQuads(@Nullable BlockState state,
									@Nullable Direction side,
									RandomSource rand) {
		return transform(super.getQuads(state, side, rand));
	}

	private static List<BakedQuad> transform(List<BakedQuad> old) {
		List<BakedQuad> out = new ArrayList<>(old.size());
		for (BakedQuad quad : old) {
			out.add(applyGlow(quad));
		}
		return out;
	}

	private static BakedQuad applyGlow(BakedQuad quad) {
		int[] data = quad.getVertices().clone();

		final int STRIDE = 8;

		for (int v = 0; v < 4; v++) {
			int uv2Index = v * STRIDE + 6;
			data[uv2Index] = FULLBRIGHT;
		}

		return new BakedQuad(
			data,
			quad.getTintIndex(),
			quad.getDirection(),
			quad.getSprite(),
			quad.isShade()
		);
	}

	@Override
	public BakedModel applyTransform(ItemDisplayContext ctx,
									 PoseStack poseStack,
									 boolean leftHand) {
		this.getTransforms().getTransform(ctx).apply(leftHand, poseStack);
		return this;
	}

	@Override
	public List<BakedModel> getRenderPasses(ItemStack stack, boolean fabulous) {
		return List.of(this);
	}
}

