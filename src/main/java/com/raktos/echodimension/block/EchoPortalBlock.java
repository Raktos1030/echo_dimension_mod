package com.raktos.echodimension.block;

import com.raktos.echodimension.EchoDimensionMod;
import com.raktos.echodimension.dimension.EchoDimension;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.joml.Vector3f;

import java.util.Random;

/**
 * Echo Portal Block - The gateway between Overworld and Echo Dimension
 * Right-click with Dimension Access Item to activate teleportation
 */
public class EchoPortalBlock extends Block implements EntityBlock {

    public static final DeferredHolder<Block, EchoPortalBlock> BLOCK = 
            net.neoforged.neoforge.registries.DeferredHolder.create(
                    net.neoforged.neoforge.registries.DeferredReference.hold(
                            net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(EchoDimensionMod.MOD_ID, "echo_portal_block"),
                            new EchoPortalBlock()
                    )
            );

    public EchoPortalBlock() {
        super(Properties.of()
                .strength(3.0f, 3.0f)
                .lightLevel(state -> 10) // Glowing portal
                .noOcclusion()
        );
        this.registerDefaultState(this.stateDefinition.any());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new EchoPortalBlockEntity(pos, state);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, 
                                InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        // Check if player has the Dimension Access Item
        if (player.getItemInHand(hand).isEmpty()) {
            player.displayClientMessage(
                    Component.literal("You need the Echo Portal Activator to activate this portal!"),
                    true
            );
            return InteractionResult.FAIL;
        }

        // Teleport to Echo Dimension
        teleportPlayer(player, level, pos);
        return InteractionResult.SUCCESS;
    }

    /**
     * Teleport the player between dimensions
     */
    private void teleportPlayer(Player player, Level level, BlockPos pos) {
        ServerLevel currentLevel = (ServerLevel) level;
        ServerLevel targetLevel;

        if (EchoDimension.isEchoDimension(currentLevel)) {
            // Return to Overworld
            targetLevel = currentLevel.getServer().getLevel(net.minecraft.world.level.Level.OVERWORLD);
            player.displayClientMessage(
                    Component.literal("§5You return to the Overworld. Reality stabilizes."),
                    true
            );
            EchoDimensionMod.LOGGER.info("Player {} returning to Overworld", player.getName().getString());
        } else {
            // Go to Echo Dimension
            targetLevel = currentLevel.getServer().getLevel(EchoDimension.DIMENSION_KEY);
            player.displayClientMessage(
                    Component.literal("§5You enter the Dimension of Echoes. Your past haunts you..."),
                    true
            );
            EchoDimensionMod.LOGGER.info("Player {} entering Echo Dimension", player.getName().getString());
        }

        if (targetLevel == null) {
            player.displayClientMessage(
                    Component.literal("§cError: Could not find target dimension!"),
                    true
            );
            return;
        }

        // Calculate target position (same coordinates in target dimension)
        BlockPos targetPos = new BlockPos(pos.getX(), 64, pos.getZ()); // Spawn at y=64

        // Create teleport event
        net.minecraft.server.level.ServerPlayer serverPlayer = (net.minecraft.server.level.ServerPlayer) player;
        net.minecraft.world.teleport.PortalForcer portalForcer = new net.minecraft.world.teleport.PortalForcer(serverPlayer);

        // Perform teleport with portal mechanics
        serverPlayer.teleportTo(targetLevel, targetPos.getX() + 0.5, targetPos.getY(), targetPos.getZ() + 0.5, 
                net.minecraft.util.Mth.DEG_TO_RAD * 180, 0);

        // Play portal sound
        targetLevel.playSound(null, targetPos, SoundEvents.PORTAL_TRAVEL, SoundSource.AMBIENT, 0.5f, 1.0f);

        // Spawn particles at destination
        spawnTeleportParticles(targetLevel, targetPos);
    }

    /**
     * Spawn magical particles at teleport location
     */
    private void spawnTeleportParticles(ServerLevel level, BlockPos pos) {
        Random random = new Random();
        for (int i = 0; i < 30; i++) {
            double x = pos.getX() + 0.5 + (random.nextDouble() - 0.5) * 2;
            double y = pos.getY() + 0.5 + (random.nextDouble() - 0.5) * 2;
            double z = pos.getZ() + 0.5 + (random.nextDouble() - 0.5) * 2;

            level.sendParticles(
                    net.minecraft.core.particles.ParticleTypes.REVERSE_PORTAL,
                    x, y, z, 1, 0, 0, 0, 0.1
            );
        }
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, Random random) {
        if (random.nextInt(2) == 0) {
            // Spawn portal particles periodically
            level.addParticle(
                    ParticleTypes.REVERSE_PORTAL,
                    pos.getX() + random.nextDouble(),
                    pos.getY() + random.nextDouble() * 0.5 + 0.5,
                    pos.getZ() + random.nextDouble(),
                    0, 0.02, 0
            );
        }
    }
}
