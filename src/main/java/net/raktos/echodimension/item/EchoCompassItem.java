package net.raktos.echodimension.item;

import java.util.Set;

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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.raktos.echodimension.EchoDimension;

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
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide() && player instanceof ServerPlayer serverPlayer) {
            boolean inEcho = level.dimension().equals(ECHO_LEVEL);
            ResourceKey<Level> targetKey = inEcho ? Level.OVERWORLD : ECHO_LEVEL;
            ServerLevel target = serverPlayer.level().getServer().getLevel(targetKey);

            if (target != null) {
                double x = serverPlayer.getX();
                double z = serverPlayer.getZ();
                double y = target.getHeight(Heightmap.Types.MOTION_BLOCKING, (int) x, (int) z) + 1;

                serverPlayer.teleportTo(target, x, y, z, Set.of(),
                        serverPlayer.getYRot(), serverPlayer.getXRot(), false);

                target.playSound(null, serverPlayer.blockPosition(),
                        SoundEvents.PORTAL_TRAVEL, SoundSource.PLAYERS, 0.5f, 0.4f);

                serverPlayer.displayClientMessage(Component.translatable(
                        inEcho ? "message.echo_dimension.return" : "message.echo_dimension.enter"), true);
            }
            player.getCooldowns().addCooldown(stack, 60);
        }
        return InteractionResult.SUCCESS;
    }
}