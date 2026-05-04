package cn.leolezury.eternalstarlight.common.block;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.List;
import java.util.Optional;

public class ESGrassBlock extends ESSpreadingSnowyDirtBlock implements BonemealableBlock {
	public static final MapCodec<ESGrassBlock> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
		BuiltInRegistries.BLOCK.byNameCodec().fieldOf("spreads_on").forGetter((block) -> block.spreadsOn),
		propertiesCodec()
	).apply(instance, ESGrassBlock::new));

	public ESGrassBlock(Block spreadOn, BlockBehaviour.Properties properties) {
		super(spreadOn, properties);
	}

	@Override
	protected MapCodec<ESGrassBlock> codec() {
		return CODEC;
	}

	@Override
	public boolean isValidBonemealTarget(LevelReader levelReader, BlockPos blockPos, BlockState blockState) {
		return levelReader.getBlockState(blockPos.above()).isAir();
	}

	@Override
	public boolean isBonemealSuccess(Level level, RandomSource randomSource, BlockPos pos, BlockState state) {
		return true;
	}

	@Override
	public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
		BlockPos abovePos = pos.above();
		BlockState shortGrass = Blocks.SHORT_GRASS.defaultBlockState();
		Optional<Holder.Reference<PlacedFeature>> optional = level.registryAccess()
			.registryOrThrow(Registries.PLACED_FEATURE)
			.getHolder(VegetationPlacements.GRASS_BONEMEAL);

		outer:
		for (int i = 0; i < 128; i++) {
			BlockPos currentPos = abovePos;

			for (int j = 0; j < i / 16; j++) {
				currentPos = currentPos.offset(random.nextInt(3) - 1, (random.nextInt(3) - 1) * random.nextInt(3) / 2, random.nextInt(3) - 1);
				if (!level.getBlockState(currentPos.below()).is(this) || level.getBlockState(currentPos).isCollisionShapeFullBlock(level, currentPos)) {
					continue outer;
				}
			}

			BlockState currentState = level.getBlockState(currentPos);
			if (currentState.is(shortGrass.getBlock()) && random.nextInt(10) == 0) {
				((BonemealableBlock) shortGrass.getBlock()).performBonemeal(level, random, currentPos, currentState);
			}

			if (currentState.isAir()) {
				Holder<PlacedFeature> holder;
				if (random.nextInt(8) == 0) {
					List<ConfiguredFeature<?, ?>> list = level.getBiome(currentPos).value().getGenerationSettings().getFlowerFeatures();
					if (list.isEmpty()) {
						list = level.getBiome(currentPos).value().getGenerationSettings().features().stream().flatMap(HolderSet::stream).map(Holder::value).flatMap(PlacedFeature::getFeatures).filter((configuredFeature) -> configuredFeature.feature() == Feature.RANDOM_PATCH).collect(ImmutableList.toImmutableList());
						if (list.isEmpty()) {
							continue;
						}
					}
					holder = ((RandomPatchConfiguration) list.get(random.nextInt(list.size())).config()).feature();
				} else {
					if (optional.isEmpty()) {
						continue;
					}
					holder = optional.get();
				}
				holder.value().place(level, level.getChunkSource().getGenerator(), random, currentPos);
			}
		}
	}

	@Override
	public BonemealableBlock.Type getType() {
		return Type.NEIGHBOR_SPREADER;
	}
}
