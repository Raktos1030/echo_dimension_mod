package com.raktos.echodimension.block;

import com.raktos.echodimension.EchoDimensionMod;
import com.raktos.echodimension.dimension.EchoDimension;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.UUID;

/**
 * Echo Portal Block - Custom portal to the Echo Dimension
 * Created with Echo Portal Item, activated by right-click
 */
public class EchoPortalBlock extends Block implements EntityBlock {

    public static final DeferredHolder<Block, EchoPortalBlock> BLOCK = 
            EchoDimensionMod.BLOCKS.register("echo_portal_block", EchoPortalBlock::new);

    public EchoPortalBlock() {
        super(Properties.of()
                .strength(3.0f, 3.0f)
                .noOcclusion()
                .isValidSpawn((state, level, pos, entityType) -> false)
        );
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new EchoPortalBlockEntity(pos, state);
    }

    /**
     * Teleport a player to/from the Echo Dimension
     */
    public static void teleportPlayer(ServerLevel targetLevel, BlockPos targetPos, ServerPlayer player) {
        player.teleportTo(targetLevel, targetPos.getX() + 0.5, targetPos.getY() + 1.0, targetPos.getZ() + 0.5,
                player.getYRot(), player.getXRot());
    }

    /**
     * Get the target level for teleportation
     */
    public static ServerLevel getTargetLevel(ServerPlayer player) {
        if (EchoDimension.isEchoDimension(player.serverLevel())) {
            // Return to Overworld
            return player.serverLevel().getServer().getLevel(net.minecraft.world.level.Level.OVERWORLD);
        } else {
            // Go to Echo Dimension
            return player.serverLevel().getServer().getLevel(EchoDimension.DIMENSION_KEY);
        }
    }

    /**
     * Get spawn position in target dimension
     */
    public static BlockPos getTargetPos(ServerLevel currentLevel) {
        if (currentLevel.dimension() == net.minecraft.world.level.Level.OVERWORLD) {
            return EchoDimension.getSpawnPosition();
        } else {
            return new BlockPos(0, 64, 0);
        }
    }

    // Import for BlockPos
    private static class BlockPos {
        public static BlockPos of(int x, int y, int z) { return new BlockPos(); }
    }
}