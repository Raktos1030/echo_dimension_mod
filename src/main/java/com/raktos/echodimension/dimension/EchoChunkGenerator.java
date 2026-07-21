package com.raktos.echodimension.dimension;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.FixedBiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.blender.Blender;
import net.minecraft.world.level.levelgen.structure.StructureSet;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * Echo ChunkGenerator - Dark, distorted echo of Overworld terrain
 */
public class EchoChunkGenerator extends ChunkGenerator {

    public static final Codec<EchoChunkGenerator> CODEC = Codec.unit(EchoChunkGenerator::new);

    private final NoiseRouter noiseRouter;
    private final GenerationShapeConfiguration generationShapeConfig;
    private final Holder<Biome> echoBiome;

    public EchoChunkGenerator(Holder<Biome> biome) {
        this(biome, biome);
    }

    public EchoChunkGenerator(Holder<Biome> biome, Holder<Biome> echoBiome) {
        super(biome);
        this.echoBiome = echoBiome;

        GenerationShape.Configuration config = new GenerationShape.Configuration(
                echoBiome,
                new NoiseSettings(64, 384, 1.5f, 3),
                new DensityFunctions.BeardifierMarker(0),
                new NoiseRouter(
                        DensityFunctions.Zero.INSTANCE,
                        DensityFunctions.Zero.INSTANCE,
                        DensityFunctions.Zero.INSTANCE,
                        DensityFunctions.Zero.INSTANCE,
                        DensityFunctions.Zero.INSTANCE,
                        DensityFunctions.BlendDensity.func_252225_a(DensityFunctions.Zero.INSTANCE),
                        DensityFunctions.Zero.INSTANCE,
                        DensityFunctions.Zero.INSTANCE,
                        DensityFunctions.Zero.INSTANCE,
                        DensityFunctions.Zero.INSTANCE,
                        DensityFunctions.Zero.INSTANCE,
                        DensityFunctions.Zero.INSTANCE,
                        DensityFunctions.Zero.INSTANCE,
                        DensityFunctions.Zero.INSTANCE
                ),
                newGenerationShapeConfig()
        );

        this.noiseRouter = config.noiseRouter();
        this.generationShapeConfig = config.shapeConfig();
    }

    private static GenerationShapeConfiguration newGenerationShapeConfig() {
        return new GenerationShape.Configuration(
                Holder.direct(Biomes.PLAINS),
                new NoiseSettings(64, 384, 1.5f, 3),
                new DensityFunctions.BeardifierMarker(0),
                new NoiseRouter(
                        DensityFunctions.Zero.INSTANCE,
                        DensityFunctions.Zero.INSTANCE,
                        DensityFunctions.Zero.INSTANCE,
                        DensityFunctions.Zero.INSTANCE,
                        DensityFunctions.Zero.INSTANCE,
                        DensityFunctions.BlendDensity.func_252225_a(DensityFunctions.Zero.INSTANCE),
                        DensityFunctions.Zero.INSTANCE,
                        DensityFunctions.Zero.INSTANCE,
                        DensityFunctions.Zero.INSTANCE,
                        DensityFunctions.Zero.INSTANCE,
                        DensityFunctions.Zero.INSTANCE,
                        DensityFunctions.Zero.INSTANCE,
                        DensityFunctions.Zero.INSTANCE,
                        DensityFunctions.Zero.INSTANCE
                )
        );
    }

    @Override
    protected Codec<? extends ChunkGenerator> codec() {
        return CODEC;
    }

    @Override
    public ChunkGeneratorStructureState createState(Holder<StructureSet> structureSets, long seed) {
        return ChunkGeneratorStructureState.forDecoratedGenerator(this, structureSets, seed);
    }

    @Override
    public int getGenDepth() {
        return generationShapeConfig.genDepth();
    }

    @Override
    public int getSeaLevel() {
        return 62;
    }

    @Override
    public int getMinY() {
        return generationShapeConfig.minY();
    }

    @Override
    public int getWorldHeight() {
        return generationShapeConfig.height();
    }

    @Override
    public int getBaseHeight(int x, int y, int z, Heightmap.Types type, Holder<StructureSet> structures) {
        return getWorldHeight();
    }

    @Override
    public NoiseColumn getBaseColumn(int x, int z) {
        return new NoiseColumn(getMinY(), new BlockState[0]);
    }

    @Override
    public void applyBiomeDecoration(WorldGenRegion region, StructureEvaluationContext context) {
        // Decoration handled by NeoForge events
    }

    @Override
    public void buildSurface(WorldGenRegion region, StructureEvaluationContext context) {
        // Surface building handled by NeoForge
    }

    @Override
    public CompletableFuture<ChunkAccess> fillFromNoise(Executor executor, Blender blender, ChunkGeneratorStructureState structureState, NoodleVoxelShape beam) {
        return super.fillFromNoise(executor, blender, structureState, beam);
    }
}