package com.raktos.echodimension.dimension;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.ChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.blender.Blender;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.structure.StructureSet;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Creates Echo Dimension world using NeoForge 1.21.1 API
 */
public class EchoDimensionProvider {

    private static final ResourceKey<DimensionType> ECHO_DIMENSION_TYPE_KEY =
            ResourceKey.create(Registries.DIMENSION_TYPE, EchoDimension.MOD_ID);

    private static final ResourceKey<DimensionType> OVERWORLD_TYPE_KEY =
            ResourceKey.create(Registries.DIMENSION_TYPE, Level.OVERWORLD.location());

    /**
     * Creates the Echo Dimension LevelStem (dimension + generator)
     */
    public static LevelStem createEchoDimensionStem(ServerLevel world) {
        HolderGetter<DimensionType> dimTypes = world.registryAccess().registryOrThrow(Registries.DIMENSION_TYPE);
        HolderGetter<NoiseGeneratorSettings> noiseSettings = world.registryAccess().registryOrThrow(Registries.NOISE_SETTINGS);

        // Get or create echo dimension type
        Holder<DimensionType> echoDimType = dimTypes.get(ECHO_DIMENSION_TYPE_KEY)
                .or(() -> dimTypes.get(OVERWORLD_TYPE_KEY))
                .orElseThrow(() -> new IllegalStateException("No dimension type available"));

        // Get noise settings for the echo dimension
        Holder<NoiseGeneratorSettings> generatorSettings = noiseSettings.getOrThrow(NoiseGeneratorSettings.OVERWORLD);

        // Get structure sets
        HolderGetter<StructureSet> structureSets = world.registryAccess().registryOrThrow(Registries.STRUCTURE_SET);

        // Create the chunk generator for echo dimension
        ChunkGenerator chunkGenerator = new NoiseBasedChunkGenerator(
                Blender.forNoiseLevel(world.getServer().overworldTeachingGenNoiseLevel()),
                structureSets,
                generatorSettings
        );

        return new LevelStem(echoDimType, chunkGenerator);
    }
}