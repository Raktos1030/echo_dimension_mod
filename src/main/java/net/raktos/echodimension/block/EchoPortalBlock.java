package net.raktos.echodimension.block;

import java.util.Set;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
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

    public static final ResourceKey<Level> ECHO_LEVEL = ResourceKey.create(
            Registries.DIMENSION,
            Identifier.fromNamespaceAndPath("echo_dimension", "echo"));

    public static final String TAG_ECHO_RETURN = "echo_return";

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

    /**
     * Portal linking logic:
     * - Has return position stored? → Teleport to it, clear the binding (round trip complete)
     * - No return position? → Teleport to other dimension, store return pos in NBT,
     *                         create a portal in the target dimension at same X/Z
     */
    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity,
                                InsideBlockEffectApplier applier, boolean unused) {
        if (level.isClientSide()) return;
        if (!(entity instanceof ServerPlayer player)) return;
        if (player.isOnPortalCooldown()) return;

        CompoundTag persistentData = player.getPersistedAccess().getPersistentData();
        boolean inEcho = level.dimension().equals(ECHO_LEVEL);

        // Check if player has a stored return position (toggle: return vs. go)
        if (persistentData.contains(TAG_ECHO_RETURN)) {
            // Return trip: use stored position
            CompoundTag returnData = persistentData.getCompound(TAG_ECHO_RETURN);
            ResourceKey<Level> sourceDim = ResourceKey.create(
                    Registries.DIMENSION,
                    Identifier.tryParse(returnData.getString("sourceDim")));
            ServerLevel sourceLevel = level.getServer().getLevel(sourceDim);
            if (sourceLevel == null) return;

            int sx = returnData.getInt("sx");
            int sy = returnData.getInt("sy");
            int sz = returnData.getInt("sz");

            double x = sx + 0.5;
            double z = sz + 0.5;
            double y = sourceLevel.getHeight(Heightmap.Types.MOTION_BLOCKING, sx, sz) + 1.0;

            player.setPortalCooldown();
            player.teleportTo(sourceLevel, x, y, z, Set.of(),
                    player.getYRot(), player.getXRot(), true);
            sourceLevel.playSound(null, new BlockPos(sx, (int) y, sz),
                    SoundEvents.PORTAL_TRAVEL, SoundSource.PLAYERS, 0.5F, 0.7F);
            persistentData.remove(TAG_ECHO_RETURN);
            return;
        }

        // First traversal: store current position as return, go to other dimension
        ServerLevel target = level.getServer().getLevel(
                inEcho ? Level.OVERWORLD : ECHO_LEVEL);
        if (target == null) return;

        int rx = player.blockPosition().getX();
        int ry = player.blockPosition().getY();
        int rz = player.blockPosition().getZ();

        // Store return position and current dimension
        CompoundTag returnData = new CompoundTag();
        returnData.putString("sourceDim", level.dimension().location().toString());
        returnData.putInt("sx", rx);
        returnData.putInt("sy", ry);
        returnData.putInt("sz", rz);
        persistentData.put(TAG_ECHO_RETURN, returnData);

        // Target X,Z same as source
        int x = rx;
        int z = rz;
        int y = target.getHeight(Heightmap.Types.MOTION_BLOCKING, x, z) + 1;

        player.setPortalCooldown();
        player.teleportTo(target, x + 0.5, y, z + 0.5, Set.of(),
                player.getYRot(), player.getXRot(), true);
        target.playSound(null, new BlockPos(x, y, z),
                SoundEvents.PORTAL_TRAVEL, SoundSource.PLAYERS, 0.5F, 0.7F);

        // Create a portal in the target dimension at same X,Z (3x3, depth 1)
        if (!inEcho) {
            createTargetPortal(target, x, z, state.getValue(AXIS) == Direction.Axis.X
                    ? Direction.Axis.Z : Direction.Axis.X);
        } else {
            createTargetPortal(target, x, z, Direction.Axis.X);
        }
    }

    /**
     * Creates a 3x3 portal area (depth 1) in the target dimension at (x, z) center.
     */
    private void createTargetPortal(ServerLevel target, int x, int z, Direction.Axis portalAxis) {
        int y = target.getHeight(Heightmap.Types.MOTION_BLOCKING, x, z);
        if (y < target.getMinY() + 3) return;

        Direction.Axis searchAxis = portalAxis == Direction.Axis.X
                ? Direction.Axis.Z : Direction.Axis.X;

        // Scan a vertical column to find the first air gap at least 3 blocks tall
        int portalY = -1;
        for (int cy = y; cy >= target.getMinY(); cy--) {
            boolean allAir = true;
            for (int dy = 0; dy < 3; dy++) {
                if (!target.getBlockState(new BlockPos(x, cy + dy, z)).isAir()) {
                    allAir = false;
                    break;
                }
            }
            if (allAir) {
                portalY = cy;
                break;
            }
        }
        if (portalY == -1) return;

        // Determine axis direction based on the portal axis
        Direction.Axis axisX = searchAxis;
        Direction.Axis axisZ = (axisX == Direction.Axis.X) ? Direction.Axis.Z : Direction.Axis.X;

        // Scan to find the best orientation for the 3x3 portal
        // Try both X-aligned and Z-aligned placements
        int bestY = -1;
        BlockPos bestPos = null;

        for (Direction.Axis tryAxis : new Direction.Axis[]{Direction.Axis.X, Direction.Axis.Z}) {
            for (int step = -2; step <= 2; step++) {
                int px, pz;
                if (tryAxis == Direction.Axis.X) {
                    px = x;
                    pz = z + step;
                } else {
                    pz = z;
                    px = x + step;
                }

                for (int cy = portalY - 2; cy <= portalY + 2; cy++) {
                    if (cy < target.getMinY()) continue;
                    boolean valid = true;
                    for (int dy = 0; dy < 3 && valid; dy++) {
                        for (int dx = 0; dx < 3 && valid; dx++) {
                            BlockPos check;
                            if (tryAxis == Direction.Axis.X) {
                                check = new BlockPos(x + dx - 1, cy + dy, pz);
                            } else {
                                check = new BlockPos(px, cy + dy, z + dx - 1);
                            }
                            if (!target.getBlockState(check).isAir()) {
                                valid = false;
                            }
                        }
                    }
                    if (valid) {
                        if (bestY == -1 || cy < bestY) {
                            bestY = cy;
                            bestPos = tryAxis == Direction.Axis.X
                                    ? new BlockPos(x, bestY, pz)
                                    : new BlockPos(px, bestY, z);
                        }
                    }
                }
            }
        }

        if (bestPos == null) return;

        // Place the 3x3 portal blocks
        Direction.Axis finalAxis = (x == bestPos.getX()) ? Direction.Axis.Z : Direction.Axis.X;
        BlockState portalState = defaultBlockState().setValue(AXIS, finalAxis);

        for (int dx = 0; dx < 3; dx++) {
            for (int dy = 0; dy < 3; dy++) {
                BlockPos place;
                if (finalAxis == Direction.Axis.X) {
                    place = bestPos.relative(Direction.EAST, dx).above(dy);
                } else {
                    place = bestPos.relative(Direction.SOUTH, dx).above(dy);
                }
                if (target.getBlockState(place).isAir()) {
                    target.setBlock(place, portalState, 2);
                }
            }
        }
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
                    (random.nextDouble() - 0.5) * 0.3,
                    -random.nextDouble() * 0.2,
                    (random.nextDouble() - 0.5) * 0.3);
        }
    }
}
