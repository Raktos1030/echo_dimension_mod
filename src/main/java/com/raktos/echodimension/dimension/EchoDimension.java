package com.raktos.echodimension.dimension;

import com.raktos.echodimension.EchoDimensionMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.neoforged.neoforge.registries.DeferredHolder;

/**
 * Echo Dimension - The dark reflection of the Overworld
 * Contains custom world generation and portal teleportation
 */
public class EchoDimension {
    public static final ResourceKey<Level> DIMENSION_KEY = ResourceKey.create(
            Registries.DIMENSION,
            ResourceLocation.fromNamespaceAndPath(EchoDimensionMod.MOD_ID, "echo")
    );

    public static final DeferredHolder<DimensionType, EchoDimensionType> DIMENSION_TYPE = EchoDimensionType.DIMENSION_TYPE_HOLDER;

    // Custom chunk generator for Echo Dimension
    public static final ResourceKey<DimensionType> DIMENSION_TYPE_KEY = ResourceKey.create(
            Registries.DIMENSION_TYPE,
            ResourceLocation.fromNamespaceAndPath(EchoDimensionMod.MOD_ID, "echo_type")
    );

    /**
     * Check if a level is the Echo Dimension
     */
    public static boolean isEchoDimension(Level level) {
        return level.dimension() == DIMENSION_KEY;
    }

    /**
     * Get spawn position in Echo Dimension
     * Places player at a safe height above void
     */
    public static SpawnPlacements getSpawnPosition() {
        return new SpawnPlacements(0, 80, 0);
    }

    public record SpawnPlacements(int x, int y, int z) {}
}
