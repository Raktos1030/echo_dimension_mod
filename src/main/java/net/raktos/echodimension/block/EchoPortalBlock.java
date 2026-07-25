package net.raktos.echodimension.block;

import java.util.Set;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.raktos.echodimension.item.EchoCompassItem;
import net.raktos.echodimension.registry.ModBlocks;

public class EchoPortalBlock extends Block {

    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.HORIZONTAL_AXIS;

    private static final VoxelShape SHAPE_X = Block.box(0, 0, 6, 16, 16, 10);
    private static final VoxelShape SHAPE_Z = Block.box(6, 0, 0, 10, 16, 16);

    public EchoPortalBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(AXIS, Direction.Axis.X));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AXIS);
    }

    @Override
    protected VoxelShape getShape(BlockState state, net.minecraft.world.level.BlockGetter level,
                                  BlockPos pos, CollisionContext context) {
        return state.getValue(AXIS) == Direction.Axis.X ? SHAPE_X : SHAPE_Z;
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, net.minecraft.world.level.BlockGetter level,
                                           BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks,
                                     BlockPos pos, Direction direction, BlockPos neighborPos,
                                     BlockState neighborState, RandomSource random) {
        if (!isStillValid(level, pos, state.getValue(AXIS))) {
            return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(state, level, ticks, pos, direction, neighborPos, neighborState, random);
    }

    private boolean isStillValid(LevelReader level, BlockPos pos, Direction.Axis axis) {
        Direction[] toCheck = (axis == Direction.Axis.X)
                ? new Direction[]{Direction.UP, Direction.DOWN, Direction.EAST, Direction.WEST}
                : new Direction[]{Direction.UP, Direction.DOWN, Direction.NORTH, Direction.SOUTH};

        for (Direction dir : toCheck) {
            BlockState neighbor = level.getBlockState(pos.relative(dir));
            boolean ok = neighbor.is(this) || neighbor.is(ModBlocks.ECHO_STONE.get());
            if (!ok) return false;
        }
        return true;
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity,
                                InsideBlockEffectApplier applier, boolean unused) {
        if (level.isClientSide()) return;
        if (!(entity instanceof ServerPlayer player)) return;
        if (player.isOnPortalCooldown()) {
            player.setPortalCooldown();
            return;
        }

        boolean inEcho = level.dimension().equals(EchoCompassItem.ECHO_LEVEL);
        ServerLevel target = player.level().getServer()
                .getLevel(inEcho ? Level.OVERWORLD : EchoCompassItem.ECHO_LEVEL);
        if (target == null) return;

        double x = player.getX();
        double z = player.getZ();
        double y = target.getHeight(Heightmap.Types.MOTION_BLOCKING, (int) x, (int) z) + 1;

        player.setPortalCooldown();
        player.teleportTo(target, x, y, z, Set.of(), player.getYRot(), player.getXRot(), true);
        target.playSound(null, player.blockPosition(),
                SoundEvents.PORTAL_TRAVEL, SoundSource.PLAYERS, 0.5F, 0.7F);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (random.nextInt(100) == 0) {
            level.playLocalSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                    SoundEvents.PORTAL_AMBIENT, SoundSource.BLOCKS, 0.5F, 0.6F, false);
        }
        for (int i = 0; i < 2; i++) {
            double x = pos.getX() + random.nextDouble();
            double y = pos.getY() + random.nextDouble();
            double z = pos.getZ() + random.nextDouble();
            level.addParticle(ParticleTypes.REVERSE_PORTAL, x, y, z,
                    (random.nextDouble() - 0.5) * 0.3, -random.nextDouble() * 0.2,
                    (random.nextDouble() - 0.5) * 0.3);
        }
    }
}