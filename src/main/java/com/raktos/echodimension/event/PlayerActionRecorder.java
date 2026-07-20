package com.raktos.echodimension.event;

import com.raktos.echodimension.EchoDimensionMod;
import com.raktos.echodimension.data.PlayerEchoData;
import com.raktos.echodimension.dimension.EchoDimension;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damage.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.EntityEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Player Action Recorder - Captures player actions in the Overworld
 * Records structures, kills, and mining to create echoes in the Echo Dimension
 */
public class PlayerActionRecorder {

    public static final Logger LOGGER = LogManager.getLogger();

    /**
     * Register event handlers
     */
    public static void register(net.neoforged.bus.api.IEventBus bus) {
        bus.register(PlayerActionRecorder.class);
    }

    /**
     * Record block break (mining)
     */
    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        if (event.getLevel().isClientSide()) return;
        if (event.getPlayer() == null) return;

        // Don't record in Echo Dimension
        if (EchoDimension.isEchoDimension(event.getLevel())) return;

        ServerPlayer player = (ServerPlayer) event.getPlayer();
        String blockType = event.getState().getBlockHolder().unwrapKey()
                .map(key -> key.location().toString())
                .orElse("unknown");

        PlayerEchoData data = PlayerEchoData.get(player);
        data.recordResource(blockType, player);

        LOGGER.info("Block mined: {} by {} at {}", blockType, player.getName().getString(), event.getPos());
    }

    /**
     * Record entity kill
     */
    @SubscribeEvent
    public static void onEntityDeath(LivingDeathEvent event) {
        if (event.getLevel().isClientSide()) return;
        if (!(event.getSource().getEntity() instanceof ServerPlayer player)) return;

        // Don't record in Echo Dimension
        if (EchoDimension.isEchoDimension(event.getLevel())) return;

        String entityType = event.getEntity().getType().unwrapKey()
                .map(key -> key.location().toString())
                .orElse("unknown");

        PlayerEchoData data = PlayerEchoData.get(player);
        data.recordKill(entityType, player);

        LOGGER.info("Entity killed: {} by {} (source: {})", entityType, player.getName().getString(), event.getSource().typeHolder.unwrapKey().map(k -> k.location().toString()).orElse("unknown"));
    }

    /**
     * Record player login
     */
    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            LOGGER.info("Player {} joined. Echo data loaded.", player.getName().getString());
        }
    }

    /**
     * Record player respawn (switch to living entity event)
     */
    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            LOGGER.info("Player {} respawned.", player.getName().getString());
        }
    }
}
