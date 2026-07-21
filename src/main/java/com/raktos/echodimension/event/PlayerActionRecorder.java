package com.raktos.echodimension.event;

import com.raktos.echodimension.EchoDimensionMod;
import com.raktos.echodimension.data.PlayerEchoData;
import com.raktos.echodimension.dimension.EchoDimension;
import net.minecraft.core.registries.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.level.BlockEvent;

/**
 * PlayerActionRecorder - Records player actions for Echo Dimension echoes
 * 
 * Listens for:
 * - Block breaking (resources mined)
 * - Entity kills (mob echoes)
 * - Player events (respawn, login)
 */
public class PlayerActionRecorder {

    /**
     * Register event handlers
     */
    public static void register(net.neoforged.bus.api.IEventBus bus) {
        bus.register(PlayerActionRecorder.class);
        EchoDimensionMod.LOGGER.info("PlayerActionRecorder registered");
    }

    /**
     * Record block break (mining) - creates resource echoes
     */
    @SubscribeEvent
    public void onBlockBroken(BlockEvent.BreakEvent event) {
        if (event.getLevel().isClientSide()) return;
        if (event.getPlayer() == null) return;

        // Don't record in Echo Dimension
        if (EchoDimension.isEchoDimension(event.getLevel())) return;

        ServerPlayer player = (ServerPlayer) event.getPlayer();
        ResourceKey<?> blockKey = event.getState().getBlockHolder().key()
                .map(key -> (ResourceKey<?>) key)
                .orElse(null);

        String blockType = blockKey != null
                ? blockKey.location().toString()
                : "minecraft:stone";

        PlayerEchoData data = PlayerEchoData.get(player);
        data.recordResource(blockType, player);
    }

    /**
     * Record entity kill - creates mob echoes
     */
    @SubscribeEvent
    public void onEntityKilled(LivingDeathEvent event) {
        if (event.getLevel().isClientSide()) return;
        if (!(event.getSource().getEntity() instanceof ServerPlayer player)) return;

        // Don't record in Echo Dimension
        if (EchoDimension.isEchoDimension(event.getLevel())) return;

        Entity entity = event.getEntity();
        ResourceKey<?> entityKey = entity.getType().builtInRegistryHolder().key()
                .map(key -> (ResourceKey<?>) key)
                .orElse(null);

        String entityType = entityKey != null
                ? entityKey.location().toString()
                : "minecraft:pig";

        PlayerEchoData data = PlayerEchoData.get(player);
        data.recordKill(entityType, player);
    }

    /**
     * Record entity drops (for loot tracking)
     */
    @SubscribeEvent
    public void onEntityDrop(LivingDropsEvent event) {
        // Optional: track valuable drops
    }

    /**
     * Record player login
     */
    @SubscribeEvent
    public void onPlayerJoin(net.neoforged.neoforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            EchoDimensionMod.LOGGER.info("Player {} joined with {} echoes",
                    player.getName().getString(),
                    PlayerEchoData.get(player).getTotalEchoes());
        }
    }

    /**
     * Record player respawn
     */
    @SubscribeEvent
    public void onPlayerRespawn(net.neoforged.neoforge.event.entity.player.PlayerEvent.PlayerRespawnEvent event) {
        // Player respawned - could affect echo resonance
    }

    /**
     * Record player level up (experience)
     */
    @SubscribeEvent
    public void onPlayerXpChange(net.neoforged.neoforge.event.entity.player.PlayerEvent.XpChangeEvent event) {
        // Optional: track progression
    }
}