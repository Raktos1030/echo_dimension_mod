package com.echodimension.blocks;

import com.echodimension.core.EchoDimensionMod;
import com.echodimension.blocks.portal.EchoPortalCoreBlock;
import com.echodimension.blocks.portal.EchoPortalFrameBlock;
import net.minecraft.core.registries.Registrar;
import net.minecraft.core.registries.RegistrationAccess;
import net.minecraft.core.registries.RegistryBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.NewRegistryEvent;

import java.util.function.Supplier;

public class ModBlocks {
    // TODO Phase 2: Définir les blocs du mod
    
    // Portail
    public static final Supplier<Block> ECHO_PORTAL_CORE = null; // TODO: register("echo_portal_core", new EchoPortalCoreBlock(...))
    public static final Supplier<Block> ECHO_PORTAL_FRAME = null; // TODO: register("echo_portal_frame", new EchoPortalFrameBlock(...))
    
    // Blocs de la Dimension Écho
    public static final Supplier<Block> ECHO_STONE = null; // TODO: register("echo_stone", new Block(...))
    public static final Supplier<Block> ECHO_DIRT = null; // TODO: register("echo_dirt", new Block(...))
    public static final Supplier<Block> ECHO_PLANKS = null; // TODO: register("echo_planks", new Block(...))
    
    // Blocs de réparation
    public static final Supplier<Block> FRAGMENTED_STONE = null; // TODO: register("fragmented_stone", new Block(...))
    
    // Méta-blocs
    public static final Supplier<Block> ECHO_CRYSTAL_BLOCK = null; // TODO: register("echo_crystal_block", new Block(...))
    
    public static void register(IEventBus eventBus) {
        EchoDimensionMod.LOGGER.info("Registering blocks for Echo Dimension...");
        // TODO: Registrar<Block> blocks = eventBus.addSyncEvent(NewRegistryEvent.class, event -> 
        //     event.create(RegistryBuilder.create(Registries.BLOCK, "echo_dimension")));
        // TODO: RegistryHelper.register(blocks, "echo_portal_core", new EchoPortalCoreBlock(...));
    }
}