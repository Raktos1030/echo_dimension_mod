package com.raktos.echodimension.dimension;

import com.raktos.echodimension.EchoDimensionMod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import java.util.OptionalLong;

public class EchoDimension {
    public static final String MOD_ID = EchoDimensionMod.MOD_ID;
    public static final ResourceKey<Level> DIMENSION_KEY = ResourceKey.create(
            Registries.DIMENSION, new ResourceLocation(MOD_ID, "echo_dimension"));

    public static final DeferredRegister<DimensionType> DIMENSION_KEYS = DeferredRegister.create(
            Registries.DIMENSION_TYPE, MOD_ID);

    public static final DeferredHolder<DimensionType, DimensionType> ECHO_DIMENSION_TYPE =
            DIMENSION_KEYS.register("echo_type", () -> new DimensionType(
                    OptionalLong.empty(), false, true, true, 1.0D, 384, 384, 384,
                    net.minecraft.world.level.material.MaterialColor.COLOR_PURPLE,
                    false, "minecraft:infiniburn_overworld",
                    0.0F, 0.0F, false, false, false, false, false, 0, 0,
                    64, 320, 0.03125D, 0.0F, false, true));

    public static boolean isEchoDimension(Level level) {
        return level.dimension() == DIMENSION_KEY;
    }

    public static BlockPos getSpawnPosition() {
        return new BlockPos(0, 80, 0);
    }
}
