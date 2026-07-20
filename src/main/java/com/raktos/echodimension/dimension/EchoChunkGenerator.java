package com.raktos.echodimension.dimension;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryDataPackCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.FixedBiomeSource;
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
                    GenerationShapeConfig.CODEC.fieldOf("generation_shape_settings").forGetter(gen -> gen.generationShapeConfig),
                    WorldgenContext.CODEC.optionalFieldOf("worldgen_context").forGetter(gen -> Optional.of(gen.context))
            ).apply(instance, instance.stable(EchoChunkGenerator::new))
    );

    private final NoiseRouter noiseRouter;
    private final GenerationShapeConfig generationShapeConfig;
    private final WorldgenContext context;
    private final EchoTerrainModifier terrainModifier;

    public EchoChunkGenerator(BiomeSource biomeSource, NoiseRouter noiseRouter, 
                              GenerationShapeConfig config, WorldgenContext context) {
        super(biomeSource, biomeSource, context.getLevel().registryAccess(), config);
        this.noiseRouter = noiseRouter;
        this.generationShapeConfig = config;
        this.context = context != null ? context : new WorldgenContext();
        this.terrainModifier = new EchoTerrainModifier();
    }

    @Override
    protected Codec<? extends ChunkGenerator> codec() {
        return CODEC;
    }

    @Override
    public void buildSurface(WorldGenRegion region, StructureManager structures, ChunkGeneratorConfiguration config, 
                             net.minecraft.world.level.chunk.Chunk chunk) {
        // Apply echo-specific surface modifications
        terrainModifier.applyEchoSurface(region, chunk);
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

    /**
     * Terrain modifier for Echo Dimension
     * Creates darker, more distorted terrain
     */
    private static class EchoTerrainModifier {
        public void applyEchoSurface(WorldGenRegion region, net.minecraft.world.level.chunk.Chunk chunk) {
            // Place echo stone at varying depths
            // Create ghostly, distorted terrain appearance
        }
    }
}

/**
 * Context for world generation
 */
class WorldgenContext implements net.minecraft.core.ContextHolder {
    private final net.minecraft.server.level.ServerLevel level;
    
    public WorldgenContext(net.minecraft.server.level.ServerLevel level) {
        this.level = level;
    }

    public net.minecraft.server.level.ServerLevel getLevel() {
        return level;
    }

    @Override
    public <T> T get(net.minecraft.core.Registry<T> registry) {
        return level.registryAccess().registry(registry).orElse(null);
    }

    public static final Codec<WorldgenContext> CODEC = RecordCodecBuilder.create(
            instance -> instance.apply(instance, WorldgenContext::new)
    );
}
