package com.raktos.echodimension.event;

import com.raktos.echodimension.data.PlayerEchoData;
import com.raktos.echodimension.dimension.EchoDimension;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
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
        LevelAccessor level = event.getLevel();
        if (level.isClientSide()) return;

        Player player = event.getPlayer();
        if (player == null) return;
        
        if (!(level instanceof Level)) return;
        Level castLevel = (Level) level;
        if (!EchoDimension.isEchoDimension(castLevel)) return;
        if (!(player instanceof ServerPlayer serverPlayer)) return;

        // ResourceLocation IS the location, just use toString()
        String blockType = BuiltInRegistries.BLOCK.getKey(event.getState().getBlock()).location().toString();

        PlayerEchoData.get(serverPlayer).recordResource(blockType, serverPlayer);
    }

    public static void onEntityKilled(LivingDeathEvent event) {
        Level level = event.getEntity().level();
        if (level.isClientSide()) return;

        Entity sourceEntity = event.getSource().getEntity();
        if (!(sourceEntity instanceof ServerPlayer player)) return;
        if (EchoDimension.isEchoDimension(level)) return;

        // EntityType.getKey() returns ResourceKey, use .location() to get ResourceLocation
        String entityType = BuiltInRegistries.ENTITY_TYPE.getKey(event.getEntity().getType()).location().toString();

        PlayerEchoData.get(player).recordKill(entityType, player);
    }
}
