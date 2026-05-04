package cn.leolezury.eternalstarlight.forge.platform;

import cn.leolezury.eternalstarlight.common.network.ESPacket;
import cn.leolezury.eternalstarlight.common.platform.ESClientPlatform;
import cn.leolezury.eternalstarlight.forge.client.model.item.ForgeGlowingBakedModel;
import cn.leolezury.eternalstarlight.forge.network.ESForgeNetworkHandler;
import com.google.auto.service.AutoService;
import net.minecraft.client.resources.model.BakedModel;

@AutoService(ESClientPlatform.class)
public class ESForgeClientPlatform implements ESClientPlatform {


	@Override
	public BakedModel getGlowingBakedModel(BakedModel origin) {
		return new ForgeGlowingBakedModel(origin);
	}

	@Override
	public void sendToServer(ESPacket packet) {
		ESForgeNetworkHandler.sendToServer(packet);
	}

}
