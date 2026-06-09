package cn.leolezury.eternalstarlight.common.client.particle.effect;

import cn.leolezury.eternalstarlight.common.client.ESRenderType;
import cn.leolezury.eternalstarlight.common.client.handler.ESClientHandler;
import cn.leolezury.eternalstarlight.common.particle.RingParticleOptions;
import cn.leolezury.eternalstarlight.common.util.Color;
import cn.leolezury.eternalstarlight.common.util.Easing;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class RingParticle extends SimpleAnimatedParticle {
	private final float scale;
	private final boolean gathering;

	protected RingParticle(ClientLevel clientLevel, double x, double y, double z, double dx, double dy, double dz, int fromColor, int toColor, float scale, float lifeScale, boolean gathering, SpriteSet spriteSet) {
		super(clientLevel, x, y, z, spriteSet, 0);
		this.xd = dx;
		this.yd = dy;
		this.zd = dz;
		this.quadSize = 1.5f;
		this.lifetime = Math.round(60 * lifeScale);
		this.setColor(fromColor);
		this.setFadeColor(toColor);
		this.scale = scale;
		this.gathering = gathering;
		this.setSpriteFromAge(spriteSet);
	}

	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.CUSTOM;
	}

	@Override
	public void render(VertexConsumer consumer, Camera camera, float partialTicks) {
		if (gathering) {
			alpha = Easing.OUT_CUBIC.interpolate(Math.min((age + partialTicks) / lifetime, 1), 0, 1);
		} else {
			alpha = Easing.OUT_CUBIC.interpolate(Math.min((age + partialTicks) / lifetime, 1), 1, 0);
		}
		Quaternionf rotation = new Quaternionf(camera.rotation());
		VertexConsumer vertexConsumer = ESClientHandler.DELAYED_BUFFER_SOURCE.getBuffer(ESRenderType.PARTICLE_NO_DEPTH);
		this.renderRotatedQuad(vertexConsumer, camera, rotation, partialTicks);
	}

	//copypaste from RingExplosionParticle
	private void renderRotatedQuad(VertexConsumer consumer, Camera camera, Quaternionf rotation, float partialTicks) {
		Vec3 camPos = camera.getPosition();

		float x = (float) (Mth.lerp(partialTicks, xo, this.x) - camPos.x);
		float y = (float) (Mth.lerp(partialTicks, yo, this.y) - camPos.y);
		float z = (float) (Mth.lerp(partialTicks, zo, this.z) - camPos.z);

		float size = this.getQuadSize(partialTicks);

		Vector3f p0 = new Vector3f(-size, -size, 0);
		Vector3f p1 = new Vector3f(-size,  size, 0);
		Vector3f p2 = new Vector3f( size,  size, 0);
		Vector3f p3 = new Vector3f( size, -size, 0);

		p0.rotate(rotation);
		p1.rotate(rotation);
		p2.rotate(rotation);
		p3.rotate(rotation);

		p0.add(x, y, z);
		p1.add(x, y, z);
		p2.add(x, y, z);
		p3.add(x, y, z);

		float u0 = this.getU0();
		float u1 = this.getU1();
		float v0 = this.getV0();
		float v1 = this.getV1();

		float r = this.rCol;
		float g = this.gCol;
		float b = this.bCol;
		float a = this.alpha;

		consumer.vertex(p0.x, p0.y, p0.z).uv(u0, v1).color(r, g, b, a).uv2(LightTexture.FULL_BRIGHT).endVertex();
		consumer.vertex(p1.x, p1.y, p1.z).uv(u0, v0).color(r, g, b, a).uv2(LightTexture.FULL_BRIGHT).endVertex();
		consumer.vertex(p2.x, p2.y, p2.z).uv(u1, v0).color(r, g, b, a).uv2(LightTexture.FULL_BRIGHT).endVertex();
		consumer.vertex(p3.x, p3.y, p3.z).uv(u1, v1).color(r, g, b, a).uv2(LightTexture.FULL_BRIGHT).endVertex();
	}

	@Override
	public float getQuadSize(float partialTicks) {
		return this.quadSize * (gathering
			? Easing.IN_OUT_QUAD.interpolate((age + partialTicks) / lifetime, scale, 0)
			: Easing.IN_OUT_QUAD.interpolate((age + partialTicks) / lifetime, 0, scale));
	}

	public static class Provider implements ParticleProvider<RingParticleOptions> {
		private final SpriteSet sprites;

		public Provider(SpriteSet spriteSet) {
			this.sprites = spriteSet;
		}

		@Override
		public Particle createParticle(RingParticleOptions options, ClientLevel level, double x, double y, double z, double dx, double dy, double dz) {
			return new RingParticle(level, x, y, z, dx, dy, dz, Color.rgbd(options.fromColor().x, options.fromColor().y, options.fromColor().z).rgb(), Color.rgbd(options.toColor().x, options.toColor().y, options.toColor().z).rgb(), options.scale(), options.lifeScale(), options.gathering(), this.sprites);
		}
	}
}
