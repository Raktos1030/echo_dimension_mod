package net.raktos.echodimension.item;

import java.util.Set;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
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
import net.minecraft.world.level.levelgen.Heightmap;
import net.raktos.echodimension.EchoDimension;
import net.raktos.echodimension.block.EchoPortalShape;
import net.raktos.echodimension.registry.ModBlocks;
import net.minecraft.core.Direction;

/**
 * Compas d'echo : clic droit pour se teleporter vers la dimension Echo,
 * ou revenir a l'Overworld si on y est deja.
 */
public class EchoCompassItem extends Item {

    public static final ResourceKey<Level> ECHO_LEVEL = ResourceKey.create(
            Registries.DIMENSION,
            Identifier.fromNamespaceAndPath(EchoDimension.MODID, "echo"));

    public EchoCompassItem(Properties properties) {
        super(properties);
    }

        @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide()) {
            level.playSound(null, player.blockPosition(),
                    SoundEvents.SCULK_SENSOR_STEP, SoundSource.PLAYERS, 1.0F, 0.6F);
            player.displayClientMessage(
                    Component.translatable("item.echo_dimension.echo_compass.hint"), true);
        }
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

        // Cherche un interieur de cadre adjacent au bloc clique
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