package com.echodimension.items;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.PortalShape;
import net.neoforged.neoforge.common.util.ITeleporter;

import javax.annotation.Nullable;
import java.util.function.Function;

/**
 * Item qui permet de créer/activer le portail vers la Dimension Écho.
 * TODO Phase 3: Implémenter la création du portail et la téléportation.
 */
public class EchoPortalTriggerItem extends Item {
    
    public EchoPortalTriggerItem() {
        super(new Properties().stacksTo(1));
    }
    
    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        Level level = context.getLevel();
        
        if (player == null || level.isClientSide()) {
            return InteractionResult.PASS;
        }
        
        // TODO Phase 3: Logique de création du portail
        // 1. Vérifier si un portail existe déjà à cette position
        // 2. Si non, vérifier si l'item est utilisé sur un bloc de cadre
        // 3. Créer le bloc central du portail
        // 4. Afficher un message au joueur
        
        return InteractionResult.SUCCESS;
    }
    
    /**
     * Téléporte le joueur vers la Dimension Écho.
     * TODO Phase 3: Implémenter la téléportation.
     */
    public static void teleportToEchoDimension(Player player) {
        // TODO Phase 3: Téléporter le joueur vers la Dimension Écho
        // ServerLevel echoLevel = ...;
        // player.changeDimension(echoLevel, new EchoPortalCoreBlock.EchoTeleporter(echoLevel));
    }
}