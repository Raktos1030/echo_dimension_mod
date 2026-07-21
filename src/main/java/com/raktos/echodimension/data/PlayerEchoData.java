package com.raktos.echodimension.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class PlayerEchoData {
    private static final Logger LOGGER = LogManager.getLogger();
    public static final String DATA_KEY = "EchoDimensionData";

    private int structureCount;
    private int killCount;
    private int resourceCount;
    private int repairCount;
    private final Map<String, Integer> mobEchoes = new HashMap<>();
    private final Map<String, Integer> blockEchoes = new HashMap<>();

    public static void register(IEventBus bus) {
        bus.addListener(PlayerEchoData::onPlayerLoggedIn);
    }

    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            PlayerEchoData data = get(player);
            LOGGER.info("Player {} joined with {} echoes",
                    player.getName().getString(), data.getTotalEchoes());
        }
    }

    public static PlayerEchoData get(ServerPlayer player) {
        PlayerEchoData playerData = new PlayerEchoData();
        CompoundTag data = player.getPersistentData();
        if (data.contains(DATA_KEY)) {
            playerData.load(data.getCompound(DATA_KEY));
        }
        return playerData;
    }

    public void saveToPlayer(ServerPlayer player) {
        CompoundTag data = player.getPersistentData();
        CompoundTag echoTag = new CompoundTag();
        save(echoTag);
        data.put(DATA_KEY, echoTag);
    }

    public void save(CompoundTag tag) {
        tag.putInt("structures", structureCount);
        tag.putInt("kills", killCount);
        tag.putInt("resources", resourceCount);
        tag.putInt("repairs", repairCount);
    }

    public void load(CompoundTag tag) {
        structureCount = tag.getInt("structures");
        killCount = tag.getInt("kills");
        resourceCount = tag.getInt("resources");
        repairCount = tag.getInt("repairs");
    }

    public void recordStructure(String type, ServerPlayer player) {
        structureCount++;
        blockEchoes.merge(type, 1, Integer::sum);
        saveToPlayer(player);
    }

    public void recordKill(String type, ServerPlayer player) {
        killCount++;
        mobEchoes.merge(type, 1, Integer::sum);
        saveToPlayer(player);
    }

    public void recordResource(String type, ServerPlayer player) {
        resourceCount++;
        blockEchoes.merge(type, 1, Integer::sum);
        saveToPlayer(player);
    }

    public void recordRepair(ServerPlayer player) {
        repairCount++;
        saveToPlayer(player);
    }

    public int getStructureCount() { return structureCount; }
    public int getKillCount() { return killCount; }
    public int getResourceCount() { return resourceCount; }
    public int getRepairCount() { return repairCount; }
    public int getTotalEchoes() { return structureCount + killCount + resourceCount; }
    public boolean hasAnyEchoes() { return structureCount > 0 || killCount > 0 || resourceCount > 0; }
    public Map<String, Integer> getMobEchoes() { return new HashMap<>(mobEchoes); }
    public Map<String, Integer> getBlockEchoes() { return new HashMap<>(blockEchoes); }
}
