package com.echodimension.items;

import com.echodimension.core.EchoDimensionMod;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;

import java.util.function.Supplier;

public class ModItems {
    // TODO Phase 2: Définir les items du mod
    
    // Item déclencheur du portail
    public static final Supplier<Item> ECHO_PORTAL_TRIGGER = null; // TODO: register("echo_portal_trigger", new EchoPortalTriggerItem(...))
    
    // Items de capture (pour enregistrer les actions)
    public static final Supplier<Item> ECHO_RECORDER = null; // TODO: register("echo_recorder", new Item(...))
    
    // Items de réparation
    public static final Supplier<Item> ECHO_SHARD = null; // TODO: register("echo_shard", new Item(...))
    public static final Supplier<Item> REPAIRHAMMER = null; // TODO: register("repair_hammer", new Item(...))
    
    // Item de boss (Phase 6)
    public static final Supplier<Item> ECHO_ESSENCE = null; // TODO: register("echo_essence", new Item(...))
    
    public static void register(IEventBus eventBus) {
        EchoDimensionMod.LOGGER.info("Registering items for Echo Dimension...");
        // TODO: Enregistrer les items via DeferredRegister
    }
}