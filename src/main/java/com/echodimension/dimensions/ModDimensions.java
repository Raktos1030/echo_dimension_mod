package com.echodimension.dimensions;

import com.echodimension.core.EchoDimensionMod;
import net.minecraft.core.registries.RegistrationAccess;
import net.minecraft.core.registries.RegistryKeys;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.dimension.LevelStem;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.util.Optional;

/**
 * Gestionnaire de la Dimension Écho.
 * TODO Phase 2: Configurer la dimension avec un ChunkGenerator spécifique.
 */
public class ModDimensions {
    
    public static final ResourceKey<LevelStem> ECHO_DIMENSION_KEY = ResourceKey.create(
        RegistryKeys.LEVEL_STEM,
        new ResourceLocation(EchoDimensionMod.MOD_ID, "echo_dimension")
    );
    
    public static void register(IEventBus eventBus) {
        EchoDimensionMod.LOGGER.info("Registering Echo Dimension...");
        
        // Écouter l'événement de démarrage du serveur pour initialiser la dimension
        eventBus.addListener(ModDimensions::onServerStarting);
    }
    
    private static void onServerStarting(ServerStartingEvent event) {
        MinecraftServer server = event.getServer();
        
        // TODO Phase 2: Initialiser la Dimension Écho
        // La dimension sera automatiquement créée par NeoForge lors du premier accès
        EchoDimensionMod.LOGGER.info("Echo Dimension registered, will be created on first access.");
    }
    
    /**
     * Retourne le ServerLevel de la Dimension Écho.
     * TODO Phase 3: Utiliser pour la téléportation.
     */
    public static Optional<ServerLevel> getEchoDimension() {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server == null) return Optional.empty();
        
        return Optional.ofNullable(server.getLevel(ModDimensions.ECHO_DIMENSION_KEY));
    }
}