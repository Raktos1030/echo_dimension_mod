package com.raktos.echodimension.event;

import com.raktos.echodimension.data.PlayerEchoData;
import com.raktos.echodimension.dimension.EchoDimension;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.level.BlockEvent;

public class PlayerActionRecorder {
    public static void register(IEventBus bus) {
        bus.addListener(PlayerActionRecorder::onBlockBroken);
        bus.addListener(PlayerActionRecorder::onEntityKilled);
    }

    public static void onBlockBroken(BlockEvent.BreakEvent event) {
        // FIX NeoForge 1.21: BlockEvent uses LevelAccessor, not Level
        LevelAccessor level = event.getLevel();
        if (level.isClientSide()) return;

        Player player = event.getPlayer();
        if (player == null) return;
        if (!EchoDimension.isEchoDimension((Level) level)) return;
        if (!(player instanceof ServerPlayer serverPlayer)) return;

        // FIX NeoForge 1.21: Entity.getType().getKey() returns ResourceKey, use .location()
        String blockType = BuiltInRegistries.BLOCK.getKey(event.getState().getBlock())
                .location()
                .toString();

        PlayerEchoData.get(serverPlayer).recordResource(blockType, serverPlayer);
    }

    public static void onEntityKilled(LivingDeathEvent event) {
        Level level = event.getEntity().level();
        if (level.isClientSide()) return;

        if (!(event.getSource().getEntity() instanceof ServerPlayer player)) return;
        if (EchoDimension.isEchoDimension(level)) return;

        // FIX NeoForge 1.21: EntityType.getKey() returns ResourceKey, use .location()
        String entityType = BuiltInRegistries.ENTITY_TYPE.getKey(event.getEntity().getType())
                .location()
                .toString();

        PlayerEchoData.get(player).recordKill(entityType, player);
    }
}