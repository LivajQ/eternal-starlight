package cn.leolezury.eternalstarlight.common.util;

import cn.leolezury.eternalstarlight.common.platform.ESPlatform;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;

public class ESAttributeUtil {

	public static double getEntityReach(LivingEntity entity) {
		Attribute attr = ESPlatform.INSTANCE.getReach();
		if (attr != null) {
			AttributeInstance inst = entity.getAttribute(attr);
			if (inst != null) return inst.getValue();
		}
		return 3.0D;
	}

	public static double getBlockReach(LivingEntity entity) {
		Attribute attr = ESPlatform.INSTANCE.getBlockReach();
		if (attr != null) {
			AttributeInstance inst = entity.getAttribute(attr);
			if (inst != null) return inst.getValue();
		}
		return 4.5D;
	}

}
