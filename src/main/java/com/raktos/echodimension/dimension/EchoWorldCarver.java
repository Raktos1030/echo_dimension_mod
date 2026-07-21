package com.raktos.echodimension.dimension;

import com.mojang.serialization.Codec;
import com.raktos.echodimension.EchoDimensionMod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.carvers.CarverConfiguration;
import net.minecraft.world.level.levelgen.carvers.CarverDebugSettings;
import net.minecraft.world.level.levelgen.carvers.CaveWorldCarver;
import net.minecraft.world.level.levelgen.heightmaps.Heightmap;

/**
 * Echo World Carver - Custom cave generation for Echo Dimension
 * Creates more distorted, ghostly cave systems
 */
public class EchoWorldCarver extends CaveWorldCarver {

    public static final Codec<EchoWorldCarver> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    CarverConfiguration.CODEC.fieldOf("config").forGetter(carver -> carver.config)
            ).apply(instance, EchoWorldCarver::new)
    );

    // Echo Dimension uses standard generation but with modifications
    public static final ResourceKey<net.minecraft.world.level.levelgen.carvers.CarverDebugSettings> ECHO_CAVE_CARVER = net.minecraft.resources.ResourceKey.create(
            net.minecraft.core.registries.BuiltInRegistries.CARVER,
            net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(EchoDimensionMod.MOD_ID, "echo_cave")
    );

    public static final ResourceKey<net.minecraft.world.level.levelgen.carvers.CarverDebugSettings> ECHO_NETHER_CAVE_CARVER = net.minecraft.resources.ResourceKey.create(
            net.minecraft.core.registries.BuiltInRegistries.CARVER,
            net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(EchoDimensionMod.MOD_ID, "echo_nether_cave")
    );

    public EchoWorldCarver(CarverConfiguration config) {
        super(config);
    }

    @Override
    protected Codec<EchoWorldCarver> codec() {
        return CODEC;
    }

    @Override
    public boolean isBelowSeaLevel(int y, ChunkGenerator chunkGenerator, boolean b) {
        return y < chunkGenerator.getSeaLevel();
    }

    @Override
    public boolean canCarve(CarverConfiguration config) {
        return config.probability > 0.0F;
    }
}
