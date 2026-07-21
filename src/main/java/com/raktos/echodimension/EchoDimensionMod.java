package com.raktos.echodimension;

import com.raktos.echodimension.block.EchoPortalBlock;
import com.raktos.echodimension.data.PlayerEchoData;
import com.raktos.echodimension.dimension.EchoDimension;
import com.raktos.echodimension.dimension.EchoDimensionProvider;
import com.raktos.echodimension.dimension.EchoWorldCarver;
import com.raktos.echodimension.event.PlayerActionRecorder;
import com.raktos.echodimension.item.DimensionAccessItem;
import com.raktos.echodimension.network.EchoPacketHandler;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Echo Dimension Mod - NeoForge 1.21.1
 * 
 * A dimension where your past actions haunt you as playable echoes.
 * 
 * Features:
 * - Custom Echo Dimension accessible via portal
 * - Records player actions (kills, mining, building) as echoes
 * - Ghost entities replay your past combat encounters
 * - Repair mechanic to banish echoes
 */
@Mod(EchoDimensionMod.MOD_ID)
public class EchoDimensionMod {
    public static final String MOD_ID = "echodimension";
    public static final Logger LOGGER = LogManager.getLogger();

    // Deferred Registers
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.create(MOD_ID, Registries.BLOCK);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.create(MOD_ID, Registries.ITEM);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = 
            DeferredRegister.create(MOD_ID, Registries.BLOCK_ENTITY_TYPE);
    public static final DeferredRegister<net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver<?>> CONFIGURED_CARVERS =
            DeferredRegister.create(MOD_ID, Registries.CONFIGURED_CARVER);

    public static ResourceLocation location(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

    public EchoDimensionMod(IEventBus modEventBus) {
        LOGGER.info("Initializing Echo Dimension Mod");

        // Register dimension type
        EchoDimension.DIMENSION_TYPES.register(modEventBus);

        // Register blocks and items
        BLOCKS.register(modEventBus);
        EchoPortalBlock.BLOCK.register(modEventBus);
        ITEMS.register(modEventBus);
        DimensionAccessItem.ECHO_PORTAL_ITEM.register(modEventBus);

        // Register block entities
        BLOCK_ENTITY_TYPES.register(modEventBus);

        // Register world generation
        CONFIGURED_CARVERS.register(modEventBus);

        // Register player data handlers
        PlayerEchoData.register(modEventBus);

        // Register event handlers
        PlayerActionRecorder.register(modEventBus);

        // Register network packets
        EchoPacketHandler.register(modEventBus);

        LOGGER.info("Echo Dimension Mod initialized successfully");
    }
}
