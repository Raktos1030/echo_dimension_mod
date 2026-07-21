package com.raktos.echodimension.dimension;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.resources.RegistryDataPackCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.Dimension;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.ChunkGeneratorSettings;
import net.minecraft.world.level.levelgen.DimensionGenerators;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.WorldDimensions;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Echo Dimension Provider - Creates the Echo Dimension stem for world generation
 * This is used when creating the world dimensions
 */
public class EchoDimensionProvider {

    /**
     * Creates the Echo Dimension stem with custom settings
     */
    public static LevelStem createEchoDimensionStem(
            ResourceKey<DimensionType> dimensionTypeKey,
            ServerLevel world,
            ChunkGeneratorSettings settings
    ) {
        return new LevelStem(
                world.registryAccess().registryOrThrow(net.minecraft.core.registries.BuiltInRegistries.DIMENSION_TYPE).getOrThrow(dimensionTypeKey),
                createEchoChunkGenerator(world, settings)
        );
    }

    /**
     * Creates the echo chunk generator
     * Uses custom noise settings for the echo terrain
     */
    private static NoiseBasedChunkGenerator createEchoChunkGenerator(
            ServerLevel world,
            ChunkGeneratorSettings settings
    ) {
        // Use overworld-like terrain but with echo modifications
        Holder<NoiseGeneratorSettings> generatorSettings = settings.withSettings();

        var biomeSource = new net.minecraft.world.level.biome.source.MultiNoiseBiomeSource(
                world.registryAccess(),
                net.minecraft.world.level.biome.source.MultiNoiseBiomeSource.Preset.AMETHYST_GRID,
                false
        );

        var noiseRouter = DimensionGenerators.createDefaultNoiseRouter(
                biomeSource, 
                generatorSettings, 
                world.getServer().getWorldData().worldGenOptions().seed()
        );

        var generationShapeConfig = DimensionGenerators.createDefaultGenerationShapeConfig(
                generatorSettings.value()
        );

        return new NoiseBasedChunkGenerator(biomeSource, noiseRouter, generationShapeConfig);
    }

    /**
     * Gets the dimensions for registration
     */
    public static List<WorldDimensions.BonusChestInfo> getBonusChestLoot() {
        return List.of();
    }
}
