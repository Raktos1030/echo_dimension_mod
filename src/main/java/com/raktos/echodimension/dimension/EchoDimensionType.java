package com.raktos.echodimension.dimension;

import com.raktos.echodimension.EchoDimensionMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.dimension.DimensionType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Optional;
import java.util.OptionalLong;

/**
 * Echo Dimension Type - Configuration for the Echo Dimension environment
 * Dark purple atmosphere with modified physics
 */
public class EchoDimensionType {
    public static final DeferredRegister<DimensionType> DIMENSION_TYPE = DeferredRegister.create(
            Registries.DIMENSION_TYPE,
            EchoDimensionMod.MOD_ID
    );

    public static final ResourceKey<DimensionType> DIMENSION_TYPE_KEY = ResourceKey.create(
            Registries.DIMENSION_TYPE,
            ResourceLocation.fromNamespaceAndPath(EchoDimensionMod.MOD_ID, "echo_type")
    );

    public static final DeferredHolder<DimensionType, DimensionType> DIMENSION_TYPE_HOLDER = DIMENSION_TYPE.register(
            "echo_type",
            () -> new DimensionType(
                    OptionalLong.of(18000L), // Fixed time: slightly darker cycle
                    false,  // hasCeiling
                    false,  // fixedTime
                    true,   // hasSkylight
                    false,  // ultraWarm (nether-like lava)
                    false,  // natural
                    0.3f,   // ambientLight (darker atmosphere - key feature!)
                    true,   // hasRain (echoes can have ghostly rain)
                    false,  // hasThunder (no thunder in echo)
                    1.0f,   // coordinateScale
                    384,    // minY
                    384,    // height
                    384,    // logicalHeight
                    false,  // infiniburn has skylight
                    null,   // effects location (custom sky handler)
                    null,   // infiniburn tag
                    null,   // dragonFight
                    0.0f,   // ambientVendor
                    null,   // monster spawn light level
                    false   // piglinSafe (added in 1.21.1)
            )
    );
}
