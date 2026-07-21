package com.raktos.echodimension.block;

import com.raktos.echodimension.EchoDimensionMod;
import com.raktos.echodimension.dimension.EchoDimension;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.registries.DeferredHolder;

public class EchoPortalBlock extends Block implements EntityBlock {
    public static final DeferredHolder<Block, EchoPortalBlock> BLOCK =
            EchoDimensionMod.BLOCKS.register("echo_portal_block", EchoPortalBlock::new);

    public EchoPortalBlock() {
        super(Properties.of().strength(3.0f, 3.0f).noOcclusion());
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new EchoPortalBlockEntity(pos, state);
    }

    public static void teleportPlayer(ServerPlayer player) {
        ServerLevel targetLevel;
        BlockPos targetPos;
        if (EchoDimension.isEchoDimension(player.serverLevel())) {
            targetLevel = player.serverLevel().getServer().getLevel(net.minecraft.world.level.Level.OVERWORLD);
            targetPos = new BlockPos(0, 64, 0);
        } else {
            targetLevel = player.serverLevel().getServer().getLevel(EchoDimension.DIMENSION_KEY);
            targetPos = new BlockPos(0, 80, 0);
        }
        if (targetLevel != null) {
            player.teleportTo(targetLevel, targetPos.getX() + 0.5, targetPos.getY() + 1.0, targetPos.getZ() + 0.5,
                    player.getYRot(), player.getXRot());
        }
    }
}
