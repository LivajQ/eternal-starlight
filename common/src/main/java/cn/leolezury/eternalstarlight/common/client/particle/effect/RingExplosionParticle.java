package cn.leolezury.eternalstarlight.common.client.particle.effect;

import cn.leolezury.eternalstarlight.common.client.ESRenderType;
import cn.leolezury.eternalstarlight.common.client.handler.ESClientHandler;
import cn.leolezury.eternalstarlight.common.particle.RingExplosionParticleOptions;
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

public class RingExplosionParticle extends SimpleAnimatedParticle {
	private final float scale;

	protected RingExplosionParticle(ClientLevel clientLevel, double x, double y, double z, double dx, double dy, double dz, int fromColor, int toColor, float scale, SpriteSet spriteSet) {
		super(clientLevel, x, y, z, spriteSet, 0);
		this.xd = dx;
		this.yd = dy;
		this.zd = dz;
		this.quadSize = 1.5f;
		this.lifetime = 60;
		this.setColor(fromColor);
		this.setFadeColor(toColor);
		this.scale = scale;
		this.setSpriteFromAge(spriteSet);
	}

	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.CUSTOM;
	}

	@Override
	public void render(VertexConsumer consumer, Camera camera, float partialTicks) {
		alpha = Easing.OUT_CUBIC.interpolate(Math.min((age + partialTicks) / lifetime, 1), 1, 0);
		Quaternionf quaternionf = new Quaternionf();
		quaternionf.rotateX(Mth.PI / 2);
		VertexConsumer vertexConsumer = ESClientHandler.DELAYED_BUFFER_SOURCE.getBuffer(ESRenderType.PARTICLE_NO_DEPTH);
		this.renderRotatedQuad(vertexConsumer, camera, quaternionf, partialTicks);
		quaternionf = new Quaternionf();
		quaternionf.rotateY(-Mth.PI).rotateX(-Mth.PI / 2);
		this.renderRotatedQuad(vertexConsumer, camera, quaternionf, partialTicks);
	}

	private void renderRotatedQuad(VertexConsumer consumer, Camera camera, Quaternionf rotation, float partialTicks) {
		Vec3 camPos = camera.getPosition();

		float x = (float) (Mth.lerp(partialTicks, xo, this.x) - camPos.x);
		float y = (float) (Mth.lerp(partialTicks, yo, this.y) - camPos.y);
		float z = (float) (Mth.lerp(partialTicks, zo, this.z) - camPos.z);

		float size = this.getQuadSize(partialTicks);

		// Base quad corners before rotation
		Vector3f p0 = new Vector3f(-size, -size, 0);
		Vector3f p1 = new Vector3f(-size,  size, 0);
		Vector3f p2 = new Vector3f( size,  size, 0);
		Vector3f p3 = new Vector3f( size, -size, 0);

		// Apply rotation
		p0.rotate(rotation);
		p1.rotate(rotation);
		p2.rotate(rotation);
		p3.rotate(rotation);

		// Translate to particle position
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

		// 1.20.1 vertex format (hopefully)
		consumer.vertex(p0.x, p0.y, p0.z).color(r, g, b, a).uv(u0, v1).uv2(LightTexture.FULL_BRIGHT).endVertex();
		consumer.vertex(p1.x, p1.y, p1.z).color(r, g, b, a).uv(u0, v0).uv2(LightTexture.FULL_BRIGHT).endVertex();
		consumer.vertex(p2.x, p2.y, p2.z).color(r, g, b, a).uv(u1, v0).uv2(LightTexture.FULL_BRIGHT).endVertex();
		consumer.vertex(p3.x, p3.y, p3.z).color(r, g, b, a).uv(u1, v1).uv2(LightTexture.FULL_BRIGHT).endVertex();
	}

	@Override
	public float getQuadSize(float partialTicks) {
		return this.quadSize * Easing.OUT_QUINT.interpolate((age + partialTicks) / lifetime, scale / 10, scale);
	}

	public static class Provider implements ParticleProvider<RingExplosionParticleOptions> {
		private final SpriteSet sprites;

		public Provider(SpriteSet spriteSet) {
			this.sprites = spriteSet;
		}

		@Override
		public Particle createParticle(RingExplosionParticleOptions options, ClientLevel level, double x, double y, double z, double dx, double dy, double dz) {
			return new RingExplosionParticle(level, x, y, z, dx, dy, dz, Color.rgbd(options.fromColor().x, options.fromColor().y, options.fromColor().z).rgb(), Color.rgbd(options.toColor().x, options.toColor().y, options.toColor().z).rgb(), options.scale(), this.sprites);
		}
	}
}
