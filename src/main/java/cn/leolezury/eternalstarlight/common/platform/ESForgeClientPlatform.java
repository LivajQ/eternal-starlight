package cn.leolezury.eternalstarlight.common.platform;

import cn.leolezury.eternalstarlight.common.client.ESForgeDimensionSpecialEffects;
import cn.leolezury.eternalstarlight.common.client.model.item.ForgeGlowingBakedModel;
import cn.leolezury.eternalstarlight.common.network.ESPacket;
import cn.leolezury.eternalstarlight.common.network.handler.ESForgeNetworkHandler;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.resources.model.BakedModel;

public class ESForgeClientPlatform implements ESClientPlatform {

	@Override
	public DimensionSpecialEffects getDimEffect() {
		return new ESForgeDimensionSpecialEffects(160.0F, false, DimensionSpecialEffects.SkyType.NONE, false, false);
	}

	@Override
	public BakedModel getGlowingBakedModel(BakedModel origin) {
		return new ForgeGlowingBakedModel(origin);
	}

	@Override
	public void sendToServer(ESPacket packet) {
		ESForgeNetworkHandler.sendToServer(packet);
	}

}
