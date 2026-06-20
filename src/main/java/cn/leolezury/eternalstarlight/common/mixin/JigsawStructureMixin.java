package cn.leolezury.eternalstarlight.common.mixin;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.structures.JigsawStructure;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

// copied from Huge Structure Blocks mod since, for reasons only known to god, that mod causes mixin errors in completely unrelated classes for Secrets of the Void
@Mixin(
	value = {JigsawStructure.class},
	priority = 999
)
public abstract class JigsawStructureMixin {
	@Shadow
	@Final
	@Mutable
	public static final int MAX_TOTAL_STRUCTURE_RANGE = Integer.MAX_VALUE;
	@Shadow
	@Final
	@Mutable
	public static final Codec<JigsawStructure> CODEC = ExtraCodecs.validate(RecordCodecBuilder.mapCodec((instance) -> instance.group(Structure.settingsCodec(instance), StructureTemplatePool.CODEC.fieldOf("start_pool").forGetter((structure) -> structure.startPool), ResourceLocation.CODEC.optionalFieldOf("start_jigsaw_name").forGetter((structure) -> structure.startJigsawName), Codec.intRange(0, 512).fieldOf("size").forGetter((structure) -> structure.maxDepth), HeightProvider.CODEC.fieldOf("start_height").forGetter((structure) -> structure.startHeight), Codec.BOOL.fieldOf("use_expansion_hack").forGetter((structure) -> structure.useExpansionHack), Heightmap.Types.CODEC.optionalFieldOf("project_start_to_heightmap").forGetter((structure) -> structure.projectStartToHeightmap), Codec.INT.fieldOf("max_distance_from_center").forGetter((structure) -> structure.maxDistanceFromCenter)).apply(instance, JigsawStructure::new)), JigsawStructure::verifyRange).codec();

	@ModifyConstant(
		method = {"<init>(Lnet/minecraft/world/level/levelgen/structure/Structure$StructureSettings;Lnet/minecraft/core/Holder;ILnet/minecraft/world/level/levelgen/heightproviders/HeightProvider;Z)V"},
		constant = {@Constant(
			intValue = 80
		)},
		require = 0
	)
	private static int init1(int value) {
		return 512;
	}

	@ModifyConstant(
		method = {"<init>(Lnet/minecraft/world/level/levelgen/structure/Structure$StructureSettings;Lnet/minecraft/core/Holder;ILnet/minecraft/world/level/levelgen/heightproviders/HeightProvider;ZLnet/minecraft/world/level/levelgen/Heightmap$Types;)V"},
		constant = {@Constant(
			intValue = 80
		)},
		require = 0
	)
	private static int init2(int value) {
		return 512;
	}

	@ModifyConstant(
		method = {"verifyRange"},
		constant = {@Constant(
			intValue = 128
		)},
		require = 0
	)
	private static int maxDistanceFromCenter(int value) {
		return 512;
	}
}
