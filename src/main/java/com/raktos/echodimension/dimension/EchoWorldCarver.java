package com.raktos.echodimension.dimension;

import com.mojang.serialization.Codec;
import com.raktos.echodimension.EchoDimensionMod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.carver.CarverConfiguration;
import net.minecraft.world.level.levelgen.carver.CarvingPass;
import net.minecraft.world.level.levelgen.carver.CaveWorldCarver;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

import java.util.BitSet;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Creates distorted, ghostly cave systems in the Echo Dimension
 */
public class EchoWorldCarver extends CaveWorldCarver {

    public static final Codec<EchoWorldCarver> CODEC = CaveWorldCarver.CODEC.xmap(
            EchoWorldCarver::new,
            EchoWorldCarver::new
    );

    public static final ConfiguredWorldCarver<?> ECHO_CARVER = new ConfiguredWorldCarver<>(
            EchoDimensionMod.MOD_ID + ":echo_carver",
            new EchoWorldCarver(CarverConfiguration.DEFAULT)
    );

    public EchoWorldCarver(CarverConfiguration config) {
        super(config);
    }

    @Override
    protected Codec<EchoWorldCarver> codec() {
        return CODEC;
    }

    @Override
    protected boolean canCarveFirst(int y, ChunkGenerator chunkGen) {
        return y <= chunkGen.getSeaLevel();
    }

    @Override
    protected boolean canCarveSecond(int y, ChunkGenerator chunkGen) {
        return config.probability > 0.0F;
    }

    @Override
    public boolean carve(ChunkAccess access, ChunkGenerator chunkGen, long seed, Random random,
                         int biomeX, int biomeZ, int y, CarvingPass pass, BitSet carvingMask,
                         BlockPos.MutableBlockPos mutable, AtomicBoolean exit) {
        if (!canCarveFirst(y, chunkGen)) return false;

        // Simplified carving - uses parent behavior
        return super.carve(access, chunkGen, seed, random, biomeX, biomeZ, y, pass, carvingMask, mutable, exit);
    }

    @Override
    protected boolean isBelowSeaLevel(int y, int seaLevel, ChunkGenerator chunkGen, CarvingPass pass) {
        return y < seaLevel;
    }

    @Override
    protected Fluid getFluid() {
        return Fluids.LAVA;
    }

    @Override
    protected BlockState getBarrierBlock() {
        return net.minecraft.world.level.block.Blocks.AIR.defaultBlockState();
    }

    @Override
    protected boolean skipNeighbor(ChunkAccess access, int x, int y, int z, int cutX, int cutY, int cutZ, CarvingPass pass) {
        return false;
    }
}