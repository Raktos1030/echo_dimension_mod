package com.raktos.echodimension.data;

import com.raktos.echodimension.EchoDimensionMod;
import com.raktos.echodimension.dimension.EchoDimension;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForgeEventHandler;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Player Echo Data - Stores all recorded echoes for each player
 * Persisted per player, tracks their gameplay history
 */
public class PlayerEchoData {

    public static final Logger LOGGER = LogManager.getLogger();

    // Data registration
    public static final DeferredHolder<net.minecraft.core.Registry<?>, net.minecraft.core.Registry<PlayerDataRegistry>> REGISTRY_KEY =
            net.neoforged.neoforge.registries.DeferredRegister.create(
                    net.minecraft.core.registries.BuiltInRegistries.CUSTOM_REGISTRY,
                    EchoDimensionMod.MOD_ID
            ).register("player_echo_data", () -> new PlayerDataRegistry());

    public static void register(net.neoforged.bus.api.IEventBus bus) {
        // Player data saving/loading is handled by PlayerEvent events
    }

    private int structureCount = 0;
    private int killCount = 0;
    private int resourceCount = 0;
    private int repairCount = 0;

    // Lists of recorded echoes (simplified - stores counts for now)
    private final Map<String, Integer> echoTypes = new HashMap<>();

    /**
     * Get PlayerEchoData for a server player
     */
    public static PlayerEchoData get(ServerPlayer player) {
        CompoundTag data = player.getPersistentData();
        String key = "echodimension_data";

        if (!data.contains(key)) {
            data.put(key, new CompoundTag());
        }

        CompoundTag echoTag = data.getCompound(key);
        PlayerEchoData playerData = new PlayerEchoData();
        playerData.load(echoTag);

        return playerData;
    }

    /**
     * Save echo data to NBT
     */
    public void save(CompoundTag tag) {
        tag.putInt("structures", structureCount);
        tag.putInt("kills", killCount);
        tag.putInt("resources", resourceCount);
        tag.putInt("repairs", repairCount);

        CompoundTag typesTag = new CompoundTag();
        echoTypes.forEach(typesTag::putInt);
        tag.put("echoTypes", typesTag);
    }

    /**
     * Load echo data from NBT
     */
    public void load(CompoundTag tag) {
        structureCount = tag.getInt("structures");
        killCount = tag.getInt("kills");
        resourceCount = tag.getInt("resources");
        repairCount = tag.getInt("repairs");

        if (tag.contains("echoTypes")) {
            CompoundTag typesTag = tag.getCompound("echoTypes");
            typesTag.getAllKeys().forEach(key -> echoTypes.put(key, typesTag.getInt(key)));
        }
    }

    /**
     * Record a structure echo
     */
    public void recordStructure(String structureType, ServerPlayer player) {
        structureCount++;
        echoTypes.merge(structureType, 1, Integer::sum);
        LOGGER.info("Echo recorded: {} by {}", player.getName().getString(), structureType);
        saveToPlayer(player);
    }

    /**
     * Record a kill echo
     */
    public void recordKill(String entityType, ServerPlayer player) {
        killCount++;
        echoTypes.merge("kill_" + entityType, 1, Integer::sum);
        LOGGER.info("Kill echo: {} killed {}", player.getName().getString(), entityType);
        saveToPlayer(player);
    }

    /**
     * Record a resource echo
     */
    public void recordResource(String blockType, ServerPlayer player) {
        resourceCount++;
        echoTypes.merge("resource_" + blockType, 1, Integer::sum);
        LOGGER.info("Resource echo: {} mined {}", player.getName().getString(), blockType);
        saveToPlayer(player);
    }

    /**
     * Record a repair
     */
    public void recordRepair(String echoType, ServerPlayer player) {
        repairCount++;
        LOGGER.info("Echo repaired: {} repaired {}", player.getName().getString(), echoType);
        saveToPlayer(player);
    }

    /**
     * Save data to player NBT
     */
    private void saveToPlayer(ServerPlayer player) {
        CompoundTag data = player.getPersistentData();
        CompoundTag echoTag = data.contains("echodimension_data")
                ? data.getCompound("echodimension_data")
                : new CompoundTag();
        save(echoTag);
        data.put("echodimension_data", echoTag);
    }

    // Getters
    public int getStructureCount() { return structureCount; }
    public int getKillCount() { return killCount; }
    public int getResourceCount() { return resourceCount; }
    public int getRepairCount() { return repairCount; }
    public boolean hasAnyEchoes() { return structureCount + killCount + resourceCount > 0; }
}

/**
 * Player Data Registry (placeholder for data pack loading)
 */
class PlayerDataRegistry extends net.minecraft.core.Registry<PlayerEchoData> {
    public PlayerDataRegistry() {
        super(null);
    }
}
