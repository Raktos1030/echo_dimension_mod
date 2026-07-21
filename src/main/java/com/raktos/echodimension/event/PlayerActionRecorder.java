package com.raktos.echodimension.event;

import com.raktos.echodimension.EchoDimensionMod;
import com.raktos.echodimension.data.PlayerEchoData;
import com.raktos.echodimension.dimension.EchoDimension;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.level.BlockEvent;

public class PlayerActionRecorder {
    public static void register(IEventBus bus) {
        bus.addListener(PlayerActionRecorder::onBlockBroken);
        bus.addListener(PlayerActionRecorder::onEntityKilled);
    }

    public static void onBlockBroken(BlockEvent.BreakEvent event) {
        if (event.getLevel().isClientSide()) return;
        if (event.getPlayer() == null) return;
        if (EchoDimension.isEchoDimension(event.getLevel())) return;
        if (!(event.getPlayer() instanceof ServerPlayer player)) return;

        String blockType = event.getState().getBlockHolder().unwrapKey()
                .map(k -> k.location().toString()).orElse("minecraft:stone");

        PlayerEchoData.get(player).recordResource(blockType, player);
    }

    public static void onEntityKilled(LivingDeathEvent event) {
        if (event.getLevel().isClientSide()) return;
        if (!(event.getSource().getEntity() instanceof ServerPlayer player)) return;
        if (EchoDimension.isEchoDimension(event.getLevel())) return;

        Entity entity = event.getEntity();
        String entityType = entity.getType().unwrapKey()
                .map(k -> k.location().toString()).orElse("minecraft:pig");

        PlayerEchoData.get(player).recordKill(entityType, player);
    }
}
