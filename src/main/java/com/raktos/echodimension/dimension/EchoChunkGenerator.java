package com.raktos.echodimension.dimension;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.FixedBiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.heightmap.Heightmap;
import net.minecraft.world.level.NoiseColumn;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class EchoChunkGenerator extends NoiseBasedChunkGenerator {
    public static final Codec<EchoChunkGenerator> CODEC = codec();

    public EchoChunkGenerator(BiomeSource biomeSource, HolderGetter<StructureSet> structureSets,
                              Holder<NoiseGeneratorSettings> settings) {
        super(biomeSource, structureSets, settings);
    }

    public EchoChunkGenerator(Holder<Biome> biome) {
        this(new FixedBiomeSource(biome),
                net.minecraft.core.RegistryAccess.EMPTY.holderLookupOrThrow(Registries.STRUCTURE_SET),
                net.minecraft.core.RegistryAccess.EMPTY.holderLookupOrThrow(Registries.NOISE_SETTINGS).getOrThrow(NoiseGeneratorSettings.OVERWORLD));
    }

    @Override
    protected Codec<? extends NoiseBasedChunkGenerator> codec() {
        return CODEC;
    }

    @Override
    public int getSeaLevel() {
        return 62;
    }

    @Override
    public int getMinY() {
        return -64;
    }

    @Override
    public int getBaseHeight(int x, int y, int z, Heightmap.Types type,
                             net.minecraft.world.level.chunk.ChunkGeneratorStructureState.StructureNeighbors neighbors) {
        return super.getBaseHeight(x, y, z, type, neighbors);
    }

    @Override
    public NoiseColumn getBaseColumn(int x, int z) {
        return super.getBaseColumn(x, z);
    }

    @Override
    public void applyBiomeDecoration(net.minecraft.world.level.chunk.ChunkGeneratorDecorationContext context) {
    }

    @Override
    public CompletableFuture<net.minecraft.world.level.chunk.ChunkAccess> fillFromNoise(
            Executor executor, net.minecraft.world.level.levelgen.blender.Blender blender,
            net.minecraft.world.level.chunk.ChunkGeneratorStructureState structureState,
            net.minecraft.world.level.levelgen.NoodleVoxelShape noodle) {
        return super.fillFromNoise(executor, blender, structureState, noodle);
    }

    @Override
    public void buildSurface(net.minecraft.world.level.chunk.ChunkGeneratorStructureState state,
                             net.minecraft.world.level.chunk.ChunkAccess chunk) {
    }
}
