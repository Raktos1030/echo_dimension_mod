package com.echodimension.registry;

import com.echodimension.core.EchoDimensionMod;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;

public class ModRegistries {
    // TODO Phase 4: Déclarer les Registry custom (EchoActionType, EchoType, etc.)
    
    public static void register(IEventBus eventBus) {
        eventBus.listen(NewRegistryEvent.class, event -> {
            EchoDimensionMod.LOGGER.info("Registering custom registries for Echo Dimension...");
            // TODO: event.register(RegistryBuilder.create(...));
        });
    }
}