package cn.leolezury.eternalstarlight.common.util;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;

public class ESGuiUtil {
	// from GuiGraphics, changed int -> float
	public static void blitFloat(GuiGraphics graphics, ResourceLocation resourceLocation, float x, float y, float width, float height, float texWidth, float texHeight) {
		innerBlitFloat(graphics, resourceLocation, x, x + width, y, y + height, width / texWidth, height / texHeight);
	}

	private static void innerBlitFloat(GuiGraphics graphics, ResourceLocation texture, float x1, float x2, float y1, float y2, float uMax, float vMax) {
		RenderSystem.setShaderTexture(0, texture);
		RenderSystem.setShader(GameRenderer::getPositionTexShader);

		Matrix4f pose = graphics.pose().last().pose();
		BufferBuilder buf = Tesselator.getInstance().getBuilder();

		buf.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);

		buf.vertex(pose, x1, y1, 0).uv(0, 0).endVertex();
		buf.vertex(pose, x1, y2, 0).uv(0, vMax).endVertex();
		buf.vertex(pose, x2, y2, 0).uv(uMax, vMax).endVertex();
		buf.vertex(pose, x2, y1, 0).uv(uMax, 0).endVertex();

		BufferUploader.drawWithShader(buf.end());
	}
}