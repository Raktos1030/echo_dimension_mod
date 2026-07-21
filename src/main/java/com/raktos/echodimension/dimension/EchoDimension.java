package com.raktos.echodimension.dimension;

import com.raktos.echodimension.EchoDimensionMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.OptionalLong;

/**
 * Echo Dimension - The dark reflection of the Overworld
 * Access via EchoPortalBlock, triggered by DimensionAccessItem
 */
public class EchoDimension {

    public static final String MOD_ID = EchoDimensionMod.MOD_ID;

    public static final ResourceKey<Level> DIMENSION_KEY = ResourceKey.create(
            Registries.DIMENSION,
            new ResourceLocation(MOD_ID, "echo_dimension")
    );

    public static final ResourceKey<DimensionType> DIMENSION_TYPE_KEY = ResourceKey.create(
            Registries.DIMENSION_TYPE,
            new ResourceLocation(MOD_ID, "echo_type")
    );

    // DeferredRegister for dimension type
    public static final DeferredRegister<DimensionType> DIMENSION_TYPES = DeferredRegister.create(
            Registries.DIMENSION_TYPE,
            MOD_ID
    );

    public static final DeferredHolder<DimensionType, DimensionType> ECHO_DIMENSION_TYPE = DIMENSION_TYPES.register(
            "echo_type",
            () -> new DimensionType(
                    false,                              // hasCeiling
                    OptionalLong.empty(),               // fixedTime (none = follows world)
                    true,                               // hasSkylight
                    false,                              // natural
                    1.0D,                               // coordinateScale
                    384,                                // minY
                    384,                                // height
                    384,                                // logicalHeight
                    net.minecraft.world.level.material.MaterialColor.COLOR_PURPLE,  // biomeAmbientParticle
                    false,                              // infiniburn (has skylight)
                    "minecraft:infiniburn_overworld",   // infiniburn tag
                    0.0F,                               // ambientDarkness
                    0.0F,                               // ambientLight
                    false,                              // piglinSafe
                    false,                              // natural piglin immune
                    false,                              // respawnAnchorAllow
                    false,                              // respawnAnchorChargeable
                    false,                              // hasRaids
                    0,                                  // minYFromTop
                    0,                                  // absoluteMinY
                    DimensionType.MIN_Y + 64,           // logicalHeight (start at 64)
                    320,                                // yCarverRange
                    0.03125D,                           // heightPercent
                    0.0F,                               // minSurfaceLevel
                    true,                               // nonTravelable
                    true                                // fixedTime override (false = none)
            )
    );

    /**
     * Check if a level is the Echo Dimension
     */
    public static boolean isEchoDimension(Level level) {
        return level.dimension() == DIMENSION_KEY;
    }

    /**
     * Get spawn position in Echo Dimension
     */
    public static BlockPos getSpawnPosition() {
        return new BlockPos(0, 80, 0);
    }

    @SubscribeEvent
    public static void onWorldLoad(LevelEvent.CreateSpawns event) {
        // Custom spawn placement logic if needed
    }

    // Import BlockPos
    private static class BlockPos {
        public BlockPos(int x, int y, int z) {}
    }
}