package com.raktos.echodimension.event;

import com.raktos.echodimension.data.PlayerEchoData;
import com.raktos.echodimension.dimension.EchoDimension;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.level.BlockEvent;

public class PlayerActionRecorder {
    public static void register(IEventBus bus) {
        bus.addListener(PlayerActionRecorder::onBlockBroken);
        bus.addListener(PlayerActionRecorder::onEntityKilled);
    }

    public static void onBlockBroken(BlockEvent.BreakEvent event) {
        Level level = event.getLevel();
        if (level.isClientSide()) return;

        Player player = event.getPlayer();
        if (player == null) return;
        if (!EchoDimension.isEchoDimension(level)) return;
        if (!(player instanceof ServerPlayer serverPlayer)) return;

        String blockType = event.getState().getBlockHolder()
                .unwrapKey()
                .map(k -> k.location().toString())
                .orElse("minecraft:stone");

        PlayerEchoData.get(serverPlayer).recordResource(blockType, serverPlayer);
    }

    public static void onEntityKilled(LivingDeathEvent event) {
        Level level = event.getEntity().level();
        if (level.isClientSide()) return;

        if (!(event.getSource().getEntity() instanceof ServerPlayer player)) return;
        if (EchoDimension.isEchoDimension(level)) return;

        String entityType = event.getEntity().getType()
                .unwrapKey()
                .map(k -> k.location().toString())
                .orElse("minecraft:pig");

        PlayerEchoData.get(player).recordKill(entityType, player);
    }
}
