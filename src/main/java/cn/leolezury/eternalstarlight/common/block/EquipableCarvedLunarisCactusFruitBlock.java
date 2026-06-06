package cn.leolezury.eternalstarlight.common.block;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Equipable;

public class EquipableCarvedLunarisCactusFruitBlock extends CarvedLunarisCactusFruitBlock implements Equipable {

	public EquipableCarvedLunarisCactusFruitBlock(Properties properties) {
		super(properties);
	}

	@Override
	public EquipmentSlot getEquipmentSlot() {
		return EquipmentSlot.HEAD;
	}
}
