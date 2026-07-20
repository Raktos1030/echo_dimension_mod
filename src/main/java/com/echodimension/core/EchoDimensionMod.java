package com.echodimension.core;

import com.echodimension.blocks.ModBlocks;
import com.echodimension.data.ModDataAttachments;
import com.echodimension.dimensions.ModDimensions;
import com.echodimension.entities.ModEntities;
import com.echodimension.events.ModEvents;
import com.echodimension.items.ModItems;
import com.echodimension.network.ModNetwork;
import com.echodimension.registry.ModRegistries;
import com.echodimension.world.ModWorldGeneration;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(EchoDimensionMod.MOD_ID)
public class EchoDimensionMod {
    public static final String MOD_ID = "echo_dimension";
    public static final Logger LOGGER = LoggerFactory.getLogger(EchoDimensionMod.class);

    public EchoDimensionMod(IEventBus modEventBus) {
        LOGGER.info("Echo Dimension mod initializing...");
        
        ModRegistries.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModItems.register(modEventBus);
        ModEntities.register(modEventBus);
        ModDimensions.register(modEventBus);
        ModDataAttachments.register(modEventBus);
        ModWorldGeneration.register(modEventBus);
        ModNetwork.register(modEventBus);
        ModEvents.register(modEventBus);
        
        LOGGER.info("Echo Dimension mod initialized successfully!");
    }
}