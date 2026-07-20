package com.echodimension.entities;

import com.echodimension.core.EchoDimensionMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.NewRegistryEvent;

import java.util.function.Supplier;

/**
 * Registre des entités de la Dimension Écho.
 * TODO Phase 4: Définir et enregistrer les entités.
 */
public class ModEntities {
    
    // TODO Phase 4: Déclarer les entités
    
    // Échos de mobs
    public static final Supplier<EntityType<?>> ECHO_PHANTOM = null; // TODO: register("echo_phantom", EntityType.Builder...)
    
    // Échos de constructions
    public static final Supplier<EntityType<?>> ECHO_STRUCTURE = null; // TODO: register("echo_structure", EntityType.Builder...)
    
    // Boss (Phase 6)
    public static final Supplier<EntityType<?>> ECHO_WRAITH = null; // TODO: register("echo_wraith", EntityType.Builder...)
    
    public static void register(IEventBus eventBus) {
        EchoDimensionMod.LOGGER.info("Registering entities for Echo Dimension...");
        
        eventBus.addListener((NewRegistryEvent event) -> {
            // TODO Phase 4: Enregistrer les entités
            // event.create(RegistryBuilder.create(Registries.ENTITY_TYPE, "echo_dimension"));
        });
    }
}