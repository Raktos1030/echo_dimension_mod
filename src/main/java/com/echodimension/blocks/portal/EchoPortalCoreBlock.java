package com.echodimension.blocks.portal;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.neoforged.neoforge.common.util.ITeleporter;

import javax.annotation.Nullable;
import java.util.function.Function;

/**
 * Bloc central du portail vers la Dimension Écho.
 * TODO Phase 3: Implémenter la logique de téléportation entre l'Overworld et la Dimension Écho.
 */
public class EchoPortalCoreBlock extends Block {
    
    public EchoPortalCoreBlock() {
        super(Properties.of()
            .strength(-1.0F) // Indestructible par défaut
            .noCollission()
        );
    }
    
    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        // TODO Phase 3: Implémenter la téléportation vers la Dimension Écho
        // - Vérifier si l'entité est un joueur
        // - Vérifier si le portail est "actif" (cadre complet)
        // - Créer la transition via ITeleporter
    }
    
    /**
     * Classe de téléportation pour la Dimension Écho.
     * TODO Phase 3: Compléter l'implémentation.
     */
    public static class EchoTeleporter implements ITeleporter {
        private final ServerLevel destinationLevel;
        
        public EchoTeleporter(ServerLevel level) {
            this.destinationLevel = level;
        }
        
        @Override
        public PortalInfo getPortalInfo(Entity entity, ServerLevel level, Function<ServerLevel, PortalInfo> defaultPortalInfo) {
            // TODO Phase 3: Retourner les coordonnées de spawn dans la Dimension Écho
            return defaultPortalInfo.apply(destinationLevel);
        }
        
        @Override
        public boolean isVanilla() {
            return false;
        }
    }
}