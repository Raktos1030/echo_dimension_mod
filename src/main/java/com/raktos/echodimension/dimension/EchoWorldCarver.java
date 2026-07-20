package com.raktos.echodimension.dimension;

import com.mojang.serialization.Codec;
import com.raktos.echodimension.EchoDimensionMod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.ChunkRand;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.ArgumentBasedDimension;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.engine.BuiltInLakes;
import net.minecraft.world.level.engine.LakeArgument;
import net.minecraft.world.level.levelgen.DimensionGenerators;
import net.minecraft.world.level.levelgen.WorldGenContext;
import net.minecraft.world.level.levelgen.carvers.CarverConfiguration;
import net.minecraft.world.level.levelgen.carvers.CarverDebugSettings;
import net.minecraft.world.level.levelgen.carvers.CaveCarver;
import net.minecraft.world.level.levelgen.carvers.CaveWorldCarver;
import net.minecraft.world.level.levelgen.heightmaps.Heightmap;

import java.util.function.Function;

/**
 * Echo World Carver - Custom cave generation for Echo Dimension
 * Creates more distorted, ghostly cave systems
 */
public class EchoWorldCarver extends CaveWorldCarver {

    public static final ResourceKey<ArgumentBasedDimension<?, ?>> WORLD_CARVER = 
            net.minecraft.resources.ResourceKey.create(
                    net.minecraft.core.registries.BuiltInRegistries.DIMENSION,
                    net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(EchoDimensionMod.MOD_ID, "echo_carver")
            );

    // Echo Dimension uses standard generation but with modifications
    public static final ResourceKey<CaveCarver<?>> ECHO_CAVE_CARVER = net.minecraft.resources.ResourceKey.create(
            net.minecraft.core.registries.BuiltInRegistries.CARVER,
            net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(EchoDimensionMod.MOD_ID, "echo_cave")
    );

    public static final ResourceKey<CaveCarver<?>> ECHO_NETHER_CAVE_CARVER = net.minecraft.resources.ResourceKey.create(
            net.minecraft.core.registries.BuiltInRegistries.CARVER,
            net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(EchoDimensionMod.MOD_ID, "echo_nether_cave")
    );

    // Configuration for echo caves - slightly more aggressive carving
    public static final CarverConfiguration ECHO_CAVE_CONFIG = new CarverConfiguration(
            0.1F,   // probability (slightly higher than standard)
            Heightmap.OCEAN_FLOOR_WG,
            CarverDebugSettings.NONE,
            null
    );
}
