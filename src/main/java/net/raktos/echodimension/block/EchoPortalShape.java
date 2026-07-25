package net.raktos.echodimension.block;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.raktos.echodimension.registry.ModBlocks;

/**
 * Detecte et remplit un cadre de portail en Echo Stone.
 * Inspire de la logique du portail du Nether, version simplifiee.
 */
public class EchoPortalShape {

    private static final int MIN_WIDTH = 2, MAX_WIDTH = 21;
    private static final int MIN_HEIGHT = 3, MAX_HEIGHT = 21;

    private final Level level;
    private final Direction.Axis axis;
    private final Direction rightDir;
    private BlockPos bottomLeft;
    private int width;
    private int height;

    private EchoPortalShape(Level level, BlockPos interiorStart, Direction.Axis axis) {
        this.level = level;
        this.axis = axis;
        this.rightDir = (axis == Direction.Axis.X) ? Direction.EAST : Direction.SOUTH;
        compute(interiorStart);
    }

    @Nullable
    public static EchoPortalShape find(Level level, BlockPos interiorStart) {
        for (Direction.Axis axis : new Direction.Axis[]{Direction.Axis.X, Direction.Axis.Z}) {
            EchoPortalShape shape = new EchoPortalShape(level, interiorStart, axis);
            if (shape.isValid()) return shape;
        }
        return null;
    }

    @Nullable
    public static EchoPortalShape find(Level level, BlockPos interiorStart, Direction.Axis axis) {
        EchoPortalShape shape = new EchoPortalShape(level, interiorStart, axis);
        return shape.isValid() ? shape : null;
    }

    private void compute(BlockPos start) {
        BlockPos pos = start;

        while (pos.getY() > level.getMinY() && isEmpty(level.getBlockState(pos.below()))) {
            pos = pos.below();
        }
        if (!isFrame(pos.below())) {
            width = 0;
            return;
        }

        Direction leftDir = rightDir.getOpposite();
        while (isEmpty(level.getBlockState(pos.relative(leftDir)))
                && pos.distManhattan(start) < MAX_WIDTH * 2) {
            pos = pos.relative(leftDir);
            if (!isFrame(pos.below())) {
                width = 0;
                return;
            }
        }

        if (!isFrame(pos.relative(leftDir))) {
            width = 0;
            return;
        }

        this.bottomLeft = pos;

        int w = 1;
        while (w <= MAX_WIDTH && isEmpty(level.getBlockState(bottomLeft.relative(rightDir, w)))) {
            if (!isFrame(bottomLeft.relative(rightDir, w).below())) {
                width = 0;
                return;
            }
            w++;
        }

        if (!isFrame(bottomLeft.relative(rightDir, w))) {
            width = 0;
            return;
        }
        this.width = w;

        int h = 1;
        outer:
        while (h <= MAX_HEIGHT) {
            for (int i = 0; i < width; i++) {
                BlockPos p = bottomLeft.relative(rightDir, i).above(h);
                if (!isEmpty(level.getBlockState(p))) break outer;
            }

            if (!isFrame(bottomLeft.relative(leftDir).above(h))
                    || !isFrame(bottomLeft.relative(rightDir, width).above(h))) {
                width = 0;
                return;
            }
            h++;
        }

        for (int i = 0; i < width; i++) {
            if (!isFrame(bottomLeft.relative(rightDir, i).above(h))) {
                width = 0;
                return;
            }
        }

        this.height = h;
    }

    private boolean isFrame(BlockPos pos) {
        return level.getBlockState(pos).is(ModBlocks.ECHO_STONE.get());
    }

    private boolean isEmpty(BlockState state) {
        return state.isAir() || state.is(ModBlocks.ECHO_PORTAL.get());
    }

    public boolean isValid() {
        return bottomLeft != null
                && width >= MIN_WIDTH && width <= MAX_WIDTH
                && height >= MIN_HEIGHT && height <= MAX_HEIGHT;
    }

    public BlockPos getPortalBlockPos() {
        int centerI = this.width / 2;
        return bottomLeft.relative(rightDir, centerI).above(1);
    }

    public void createPortalBlocks() {
        Direction.Axis portalAxis = (axis == Direction.Axis.X)
                ? Direction.Axis.Z
                : Direction.Axis.X;

        BlockState portal = ModBlocks.ECHO_PORTAL.get().defaultBlockState()
                .setValue(EchoPortalBlock.AXIS, portalAxis);

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                level.setBlock(bottomLeft.relative(rightDir, i).above(j), portal, 2);
            }
        }
    }
}