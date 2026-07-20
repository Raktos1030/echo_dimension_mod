package com.echodimension.world;

import com.echodimension.core.EchoDimensionMod;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.GenLayerEchoDimension;
import net.neoforged.neoforge.event.OnDatapackRegistryEvent;
import net.neoforged.neoforge.event.level.biome.BiomeLoadingEvent;
import net.neoforged.neoforge.event.level.chunk.ChunkGeneratorRegistryEvent;

/**
 * Système de génération de monde pour la Dimension Écho.
 * TODO Phase 2: Implémenter la génération de terrain custom.
 */
public class ModWorldGeneration {
    
    public static void register(IEventBus eventBus) {
        EchoDimensionMod.LOGGER.info("Registering world generation for Echo Dimension...");
        
        // TODO Phase 2: Enregistrer le chunk generator custom
        eventBus.addListener((ChunkGeneratorRegistryEvent.Register event) -> {
            // event.register(ECHO_DIMENSION_KEY, EchoChunkGenerator::createEchoLevelStem);
        });
        
        // TODO Phase 2: Ajouter des biomes custom pour la Dimension Écho
        eventBus.addListener(ModWorldGeneration::onBiomeLoading);
    }
    
    private static void onBiomeLoading(BiomeLoadingEvent event) {
        // TODO Phase 2: Modifier les biomes pour la Dimension Écho
        // if (event.getCategory() == Type.OVERWORLD) {
        //     event.getEffects().color(new Color(80, 80, 120)); // Teinte bleutée/déformée
        // }
    }
}