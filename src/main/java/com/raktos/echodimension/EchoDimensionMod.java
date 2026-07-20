package com.raktos.echodimension;

import com.raktos.echodimension.block.EchoPortalBlock;
import com.raktos.echodimension.data.PlayerEchoData;
import com.raktos.echodimension.dimension.EchoDimension;
import com.raktos.echodimension.dimension.EchoDimensionType;
import com.raktos.echodimension.dimension.EchoWorldCarver;
import com.raktos.echodimension.event.PlayerActionRecorder;
import com.raktos.echodimension.item.DimensionAccessItem;
import com.raktos.echodimension.network.EchoPacketHandler;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.data.event.DataPackSetupEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Echo Dimension Mod - Entry Point
 * A mod where player actions haunt the world as playable echoes in a dark reflection dimension.
 */
@Mod(EchoDimensionMod.MOD_ID)
public class EchoDimensionMod {
    public static final String MOD_ID = "echodimension";
    public static final Logger LOGGER = LogManager.getLogger();

    public EchoDimensionMod(IEventBus modEventBus) {
        LOGGER.info("Initializing Echo Dimension Mod...");

        // Register dimensions
        EchoDimension.DIMENSION_KEY.register(modEventBus);
        EchoDimensionType.DIMENSION_TYPE_KEY.register(modEventBus);

        // Register blocks and items
        EchoPortalBlock.BLOCK.register(modEventBus);
        DimensionAccessItem.ITEM.register(modEventBus);

        // Register block entities
        EchoPortalBlockEntity.BLOCK_ENTITY.register(modEventBus);

        // Register world generation features
        EchoWorldCarver.WORLD_CARVER.register(modEventBus);

        // Register player data
        PlayerEchoData.register(modEventBus);

        // Register event handlers
        PlayerActionRecorder.register(modEventBus);

        // Register network packets
        EchoPacketHandler.register(modEventBus);

        LOGGER.info("Echo Dimension Mod initialized successfully!");
    }
}
