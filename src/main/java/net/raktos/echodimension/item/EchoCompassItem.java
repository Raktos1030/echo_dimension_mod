package net.raktos.echodimension.item;

import java.util.Set;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.raktos.echodimension.EchoDimension;
import net.raktos.echodimension.block.EchoPortalShape;
import net.raktos.echodimension.registry.ModBlocks;

/**
 * Compass to bind source position and create portal frame.
 * First right-click: stores source position/dimension in player NBT.
 * Right-click on Echo Stone frame: creates portal.
 */
public class EchoCompassItem extends Item {

    public static final ResourceKey<Level> ECHO_LEVEL = ResourceKey.create(
            Registries.DIMENSION,
            Identifier.fromNamespaceAndPath(EchoDimension.MODID, "echo"));

    public static final String TAG_ECHO_BINDING = "echo_binding";

    public EchoCompassItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        boolean inEcho = level.dimension().equals(ECHO_LEVEL);
        if (inEcho) {
            // In Echo dimension: teleport back to source binding
            CompoundTag data = player.getPersistedAccess()
                    .getPersistentData()
                    .getCompound(TAG_ECHO_BINDING);

            if (!data.isEmpty() && data.contains("sourceDim")) {
                ResourceKey<Level> sourceDim = ResourceKey.create(
                        Registries.DIMENSION,
                        Identifier.tryParse(data.getString("sourceDim")));
                BlockPos sourcePos = new BlockPos(
                        data.getInt("x"), data.getInt("y"), data.getInt("z"));

                ServerLevel sourceLevel = level.getServer().getLevel(sourceDim);
                if (sourceLevel != null) {
                    ServerPlayer sp = (ServerPlayer) player;
                    double x = sourcePos.getX() + 0.5;
                    double z = sourcePos.getZ() + 0.5;
                    double y = sourceLevel.getHeight(
                            net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING,
                            sourcePos.getX(), sourcePos.getZ()) + 1.0;
                    sp.setPortalCooldown();
                    sp.teleportTo(sourceLevel, x, y, z, Set.of(), sp.getYRot(), sp.getXRot(), true);
                    sourceLevel.playSound(null, sourcePos,
                            SoundEvents.PORTAL_TRAVEL, SoundSource.PLAYERS, 0.5F, 0.7F);
                    player.displayClientMessage(
                            Component.translatable("item.echo_dimension.echo_compass.return"), true);
                    return InteractionResult.SUCCESS;
                }
            }

            player.displayClientMessage(
                    Component.translatable("item.echo_dimension.echo_compass.no_binding"), true);
            return InteractionResult.SUCCESS;
        }

        // In Overworld: store current position as source binding
        CompoundTag binding = new CompoundTag();
        binding.putString("sourceDim", level.dimension().location().toString());
        binding.putInt("x", player.blockPosition().getX());
        binding.putInt("y", player.blockPosition().getY());
        binding.putInt("z", player.blockPosition().getZ());

        player.getPersistedAccess()
                .getPersistentData()
                .put(TAG_ECHO_BINDING, binding);

        level.playSound(null, player.blockPosition(),
                SoundEvents.SCULK_SENSOR_STEP, SoundSource.PLAYERS, 1.0F, 0.6F);
        player.displayClientMessage(
                Component.translatable("item.echo_dimension.echo_compass.bound"), true);
        return InteractionResult.SUCCESS;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos clicked = context.getClickedPos();

        if (!level.getBlockState(clicked).is(ModBlocks.ECHO_STONE.get())) {
            return InteractionResult.PASS;
        }
        if (level.isClientSide()) return InteractionResult.SUCCESS;

        for (Direction dir : Direction.values()) {
            BlockPos candidate = clicked.relative(dir);
            if (!level.getBlockState(candidate).isAir()) continue;
            EchoPortalShape shape = EchoPortalShape.find(level, candidate);
            if (shape != null) {
                shape.createPortalBlocks();
                level.playSound(null, clicked, SoundEvents.END_PORTAL_SPAWN,
                        SoundSource.BLOCKS, 0.7F, 1.4F);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }
}
