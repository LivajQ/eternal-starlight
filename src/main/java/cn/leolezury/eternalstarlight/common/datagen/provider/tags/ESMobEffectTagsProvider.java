package cn.leolezury.eternalstarlight.common.datagen.provider.tags;

import cn.leolezury.eternalstarlight.common.EternalStarlight;
import cn.leolezury.eternalstarlight.common.util.ESTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.effect.MobEffects;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class ESMobEffectTagsProvider extends MobEffectTagsProvider {
	public ESMobEffectTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
		super(output, lookupProvider, EternalStarlight.ID, existingFileHelper);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		tag(ESTags.MobEffects.DEEPSILVER_ARMOR_CAN_REMOVE)
			.add(
				MobEffects.POISON,
				MobEffects.WITHER,
				MobEffects.CONFUSION,
				MobEffects.HUNGER,
				MobEffects.MOVEMENT_SLOWDOWN,
				//MobEffects.INFESTED,
				MobEffects.BLINDNESS
			);
	}
}