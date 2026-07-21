package com.raktos.echodimension.dimension;

import com.raktos.echodimension.EchoDimensionMod;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.FixedBiomeSource;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;

public class EchoDimensionProvider {
    public static ResourceKey<DimensionType> getEchoDimTypeKey() {
        return ResourceKey.create(Registries.DIMENSION_TYPE,
                EchoDimensionMod.location("echo_type"));
    }

    public static LevelStem createStem(ServerLevel world) {
        // FIX NeoForge 1.21: registryAccess().registryOrThrow() returns HolderGetter directly
        var dimTypes = world.registryAccess().registryOrThrow(Registries.DIMENSION_TYPE);
        var noiseSettings = world.registryAccess().registryOrThrow(Registries.NOISE_SETTINGS);
        var biomes = world.registryAccess().registryOrThrow(Registries.BIOME);

        Holder<DimensionType> dimType = dimTypes.getOrThrow(getEchoDimTypeKey());
        Holder<NoiseGeneratorSettings> genSettings = noiseSettings.getOrThrow(NoiseGeneratorSettings.OVERWORLD);

        Holder<Biome> echoBiome = biomes.getOrThrow(net.minecraft.world.level.biome.Biomes.FOREST);
        BiomeSource biomeSource = new FixedBiomeSource(echoBiome);

        NoiseBasedChunkGenerator chunkGen = new NoiseBasedChunkGenerator(
                biomeSource, genSettings
        );

        return new LevelStem(dimType, chunkGen);
    }
}