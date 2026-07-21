package com.raktos.echodimension;

import com.raktos.echodimension.data.PlayerEchoData;
import com.raktos.echodimension.dimension.EchoDimension;
import com.raktos.echodimension.event.PlayerActionRecorder;
import com.raktos.echodimension.network.EchoPacketHandler;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(EchoDimensionMod.MOD_ID)
public class EchoDimensionMod {
    public static final String MOD_ID = "echodimension";
    public static final Logger LOGGER = LogManager.getLogger();

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, MOD_ID);

    public static ResourceLocation location(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    public EchoDimensionMod(IEventBus modEventBus) {
        LOGGER.info("Initializing Echo Dimension Mod for NeoForge 1.21.1");

        EchoDimension.DIMENSION_KEYS.register(modEventBus);
        BLOCKS.register(modEventBus);
        BLOCK_ENTITY_TYPES.register(modEventBus);
        ITEMS.register(modEventBus);
        PlayerActionRecorder.register(modEventBus);
        PlayerEchoData.register(modEventBus);
        EchoPacketHandler.register(modEventBus);

        LOGGER.info("Echo Dimension Mod initialized successfully");
    }
}
