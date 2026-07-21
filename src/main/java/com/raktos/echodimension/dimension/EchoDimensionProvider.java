package com.raktos.echodimension.dimension;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.FixedBiomeSource;
import net.minecraft.resources.ResourceLocation;

public class EchoDimensionProvider {
    public static final ResourceKey<DimensionType> ECHO_DIM_TYPE_KEY = ResourceKey.create(
            Registries.DIMENSION_TYPE, new ResourceLocation(EchoDimension.MOD_ID, "echo_type"));

    public static LevelStem createStem(ServerLevel world) {
        HolderGetter<DimensionType> dimTypes = world.registryAccess().registryOrThrow(Registries.DIMENSION_TYPE);
        HolderGetter<NoiseGeneratorSettings> noiseSettings = world.registryAccess().registryOrThrow(Registries.NOISE_SETTINGS);
        HolderGetter<StructureSet> structureSets = world.registryAccess().registryOrThrow(Registries.STRUCTURE_SET);
        HolderGetter<Biome> biomes = world.registryAccess().registryOrThrow(Registries.BIOME);

        Holder<DimensionType> dimType = dimTypes.getOrThrow(ECHO_DIM_TYPE_KEY);
        Holder<NoiseGeneratorSettings> genSettings = noiseSettings.getOrThrow(NoiseGeneratorSettings.OVERWORLD);

        Holder<Biome> echoBiome = biomes.getOrThrow(net.minecraft.world.level.biome.Biomes.DARK_OAKS)
                .or(() -> biomes.getOrThrow(net.minecraft.world.level.biome.Biomes.PLAINS))
                .orElseThrow();

        BiomeSource biomeSource = new FixedBiomeSource(echoBiome);

        NoiseBasedChunkGenerator chunkGen = new NoiseBasedChunkGenerator(
                biomeSource, structureSets, genSettings);

        return new LevelStem(dimType, chunkGen);
    }
}
