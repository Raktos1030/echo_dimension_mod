package com.raktos.echodimension.dimension;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.source.BiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.synth.NoiseRouter;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * Echo Chunk Generator - Creates the dark, distorted echo landscape
 * Based on noise terrain but with eerie modifications
 */
public class EchoChunkGenerator extends ChunkGenerator {

    public static final Codec<EchoChunkGenerator> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    BiomeSource.CODEC.fieldOf("biome_source").forGetter(EchoChunkGenerator::getBiomeSource),
                    NoiseRouter.CODEC.fieldOf("noise_router").forGetter(gen -> gen.noiseRouter),
                    GenerationShapeConfig.CODEC.fieldOf("generation_shape_settings").forGetter(gen -> gen.generationShapeConfig)
            ).apply(instance, instance.stable(EchoChunkGenerator::new))
    );

    private final NoiseRouter noiseRouter;
    private final GenerationShapeConfig generationShapeConfig;

    public EchoChunkGenerator(BiomeSource biomeSource, NoiseRouter noiseRouter, GenerationShapeConfig config) {
        super(biomeSource, biomeSource);
        this.noiseRouter = noiseRouter;
        this.generationShapeConfig = config;
    }

    @Override
    protected Codec<? extends ChunkGenerator> codec() {
        return CODEC;
    }

    @Override
    public void buildSurface(WorldGenRegion region, StructureManager structures, ChunkGeneratorConfiguration config, 
                             net.minecraft.world.level.chunk.Chunk chunk) {
        // Echo-specific surface modifications will be added here
    }

    @Override
    public int getGenDepth() {
        return generationShapeConfig.genDepth();
    }

    @Override
    public int getSeaLevel() {
        return 62; // Standard sea level
    }

    @Override
    public int getMinY() {
        return generationShapeConfig.minY();
    }

    @Override
    public CompletableFuture<ChunkResult> fillFromNoise(Executor executor, StructureManager structureManager, 
                                                         ChunkGeneratorConfiguration config, WorldGenRegion region) {
        return super.fillFromNoise(executor, structureManager, config, region);
    }

    @Override
    public String getDebugString(long seed, BlockPos pos) {
        return "EchoChunkGenerator[" + seed + " at " + pos + "]";
    }

    @Override
    public Holder<Biome> getBiome(WorldGenRegion region, int x, int y, int z, net.minecraft.world.level.dimension.DimensionHeightToken token) {
        return biomeSource.getNoiseBiome(x, y, z);
    }

    @Override
    public NoiseColumn getBaseColumn(int x, int z) {
        return new NoiseColumn(getMinY(), new int[0]);
    }

    @Override
    public void applyBiomeDecoration(WorldGenRegion region, StructureManager structureManager, 
                                    ChunkGeneratorConfiguration config) {
        // Echo dimension has modified biome decoration
    }

    @Override
    public void applyGlobalFeature(StructureManager structures, ChunkGeneratorConfiguration config, 
                                   WorldGenRegion region) {
        // Optional: add echo-specific features
    }
}
