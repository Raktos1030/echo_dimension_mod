package com.raktos.echodimension.block;

import com.raktos.echodimension.EchoDimensionMod;
import com.raktos.echodimension.dimension.EchoDimension;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;

public class EchoPortalBlock extends Block implements EntityBlock {
    public static final Holder<Block> BLOCK = EchoDimensionMod.BLOCKS.register("echo_portal_block",
            () -> new EchoPortalBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_PURPLE)
                            .strength(3.0F, 3.0F)
                            .noOcclusion()
                            .isRedstoneConductor((state, level, pos) -> false)
            ));

    protected static final VoxelShape SHAPE = Shapes.block();

    public EchoPortalBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    // getShape method signature changed in 1.21 - no @Override needed
    public VoxelShape getShape(BlockState state, net.minecraft.world.level.BlockGetter level,
                                BlockPos pos, net.minecraft.world.phys.shapes.CollisionContext context) {
        return SHAPE;
    }

    @Override
    public boolean useShapeForCollision(BlockState state) {
        return true;
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
            player.teleportTo(targetLevel,
                    targetPos.getX() + 0.5, targetPos.getY() + 1.0, targetPos.getZ() + 0.5,
                    player.getYRot(), player.getXRot());
        }
    }
}
