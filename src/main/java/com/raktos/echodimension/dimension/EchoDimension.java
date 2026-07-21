package com.raktos.echodimension.dimension;

import com.raktos.echodimension.EchoDimensionMod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.OptionalLong;

public class EchoDimension {
    // Fixed: ResourceKey<Level> directly, not ResourceKey<ResourceKey<Level>>
    public static final ResourceKey<Level> DIMENSION_KEY = ResourceKey.create(
            Registries.DIMENSION,
            EchoDimensionMod.location("echo_dimension")
    );

    public static final DeferredRegister<DimensionType> DIMENSION_KEYS = DeferredRegister.create(
            Registries.DIMENSION_TYPE, EchoDimensionMod.MOD_ID);

    // Fixed: use DimensionType.Builder for 1.21.1 API
    public static final DeferredHolder<DimensionType, DimensionType> ECHO_DIMENSION_TYPE =
            DIMENSION_KEYS.register("echo_type", () -> DimensionType.Builder.create()
                    .hasSkylight(true)
                    .hasCeiling(false)
                    .ultraWarm(false)
                    .natural(false)
                    .coordinateScale(1.0)
                    .bedWorks(false)
                    .respawnAnchorWorks(false)
                    .minY(-64)
                    .height(384)
                    .logicalHeight(384)
                    .ambientLight(0.0f)
                    .infiniburnLocation(net.minecraft.core.registries.BuiltInRegistries.BLOCK
                            .getResourceKey(net.minecraft.world.level.block.Blocks.INFINIBURN_OVERWORLD).orElseThrow())
                    .build()
            );

    public static boolean isEchoDimension(Level level) {
        return level.dimension().equals(DIMENSION_KEY);
    }

    public static BlockPos getSpawnPosition() {
        return new BlockPos(0, 80, 0);
    }
}
