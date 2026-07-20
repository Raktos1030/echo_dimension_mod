package com.echodimension.blocks.portal;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Bloc de cadre pour le portail Echo.
 * Doit être placé en rectangle pour activer le portail.
 * TODO Phase 3: Implémenter la détection du cadre complet.
 */
public class EchoPortalFrameBlock extends Block {
    
    public EchoPortalFrameBlock() {
        super(Properties.of()
            .strength(3.0F)
            .noOcclusion()
        );
    }
    
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        // TODO Phase 3: Définir la forme du bloc (plus petit qu'un bloc normal pour le portail)
        return Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 16.0);
    }
    
    /**
     * Vérifie si un portail est complet autour d'une position.
     * TODO Phase 3: Implémenter la logique de détection du portail.
     */
    public static boolean isPortalComplete(BlockGetter level, BlockPos pos) {
        // TODO: Vérifier que le portail forme un rectangle complet avec un bloc central manquant
        return false;
    }
}