package com.echodimension.events;

import com.echodimension.core.EchoDimensionMod;
import com.echodimension.dimensions.ModDimensions;
import com.echodimension.entities.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

/**
 * Gestionnaire des événements du mod.
 * TODO Phase 4: Implémenter la capture des actions du joueur.
 */
public class ModEvents {
    
    /**
     * Capture la mort d'un mob et crée un écho dans la Dimension Écho.
     * TODO Phase 4: Implémenter la création d'échos.
     */
    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        Entity entity = event.getEntity();
        Level level = entity.level();
        
        if (level.isClientSide()) return;
        
        // TODO Phase 4: Vérifier si l'entité morte est un monster
        // TODO Phase 4: Vérifier si le tueur est un joueur (via source de dégâts)
        // TODO Phase 4: Capturer les informations du mob mort
        // TODO Phase 4: Enregistrer dans EchoWorldData
        // TODO Phase 4: Créer un écho dans la Dimension Écho
        
        if (entity instanceof Monster monster && event.getSource().getEntity() instanceof Player player) {
            EchoDimensionMod.LOGGER.info("Player {} killed {} - Creating echo in Echo Dimension...", 
                player.getName().getString(), entity.getType().getRegistryName());
            // TODO: CaptureActionEvent.captureKill(player, entity);
        }
    }
    
    /**
     * Capture la destruction de blocs (minage) et crée des fragments dans la Dimension Écho.
     * TODO Phase 4: Implémenter la capture du minage.
     */
    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        BlockPos pos = event.getBlock().getBlockPos();
        Level level = player.level();
        
        if (level.isClientSide()) return;
        
        // TODO Phase 4: Enregistrer le bloc détruit par le joueur
        // TODO Phase 4: Créer des fragments dans la Dimension Écho
        
        EchoDimensionMod.LOGGER.debug("Player {} broke block at {} - Creating fragment in Echo Dimension...", 
            player.getName().getString(), pos);
        // TODO: CaptureActionEvent.captureBlockBreak(player, pos, event.getBlock());
    }
    
    /**
     * Capture la construction de blocs (placement) et crée des structures dans la Dimension Écho.
     * TODO Phase 4: Implémenter la capture de construction.
     */
    @SubscribeEvent
    public static void onPlayerPlaceBlock(PlayerEvent.PlaceBlock event) {
        // TODO Phase 4: Enregistrer le bloc placé par le joueur
        // TODO Phase 4: Créer une structure écho dans la Dimension Écho
    }
    
    /**
     * Gère la téléportation entre les dimensions.
     * TODO Phase 3: Implémenter la logique de portail.
     */
    @SubscribeEvent
    public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        // TODO Phase 3: Gérer les effets de transition entre dimensions
    }
}