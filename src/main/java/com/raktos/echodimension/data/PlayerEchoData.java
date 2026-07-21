package com.raktos.echodimension.data;

import com.raktos.echodimension.EchoDimensionMod;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * PlayerEchoData - Stores echo recordings per player via NBT persistence
 * 
 * Records player actions in the Overworld that create echoes in the Echo Dimension:
 * - Structures built
 * - Mobs killed
 * - Resources mined
 * - Repairs performed
 */
public class PlayerEchoData {

    private static final Logger LOGGER = LogManager.getLogger();

    // Persistent data key
    public static final String DATA_KEY = "EchoDimensionData";

    // Echo counts
    private int structureCount = 0;
    private int killCount = 0;
    private int resourceCount = 0;
    private int repairCount = 0;

    // Detailed echo tracking
    private final Map<String, Integer> echoTypes = new HashMap<>();
    private final Map<String, Integer> mobEchoes = new HashMap<>();
    private final Map<String, Integer> blockEchoes = new HashMap<>();

    /**
     * Get PlayerEchoData for a server player
     */
    public static PlayerEchoData get(ServerPlayer player) {
        PlayerEchoData playerData = new PlayerEchoData();
        
        CompoundTag data = player.getPersistentData();
        if (!data.contains(DATA_KEY)) {
            data.put(DATA_KEY, new CompoundTag());
        }

        CompoundTag echoTag = data.getCompound(DATA_KEY);
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

        CompoundTag mobTag = new CompoundTag();
        mobEchoes.forEach(mobTag::putInt);
        tag.put("mobEchoes", mobTag);

        CompoundTag blockTag = new CompoundTag();
        blockEchoes.forEach(blockTag::putInt);
        tag.put("blockEchoes", blockTag);
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
            typesTag.getAllKeys().forEach(key -> 
                echoTypes.put(key, typesTag.getInt(key))
            );
        }

        if (tag.contains("mobEchoes")) {
            CompoundTag mobTag = tag.getCompound("mobEchoes");
            mobTag.getAllKeys().forEach(key ->
                mobEchoes.put(key, mobTag.getInt(key))
            );
        }

        if (tag.contains("blockEchoes")) {
            CompoundTag blockTag = tag.getCompound("blockEchoes");
            blockTag.getAllKeys().forEach(key ->
                blockEchoes.put(key, blockTag.getInt(key))
            );
        }
    }

    /**
     * Record a structure echo
     */
    public void recordStructure(String structureType, ServerPlayer player) {
        structureCount++;
        echoTypes.merge(structureType, 1, Integer::sum);
        blockEchoes.merge(structureType, 1, Integer::sum);
        saveToPlayer(player);
    }

    /**
     * Record a kill echo
     */
    public void recordKill(String entityType, ServerPlayer player) {
        killCount++;
        mobEchoes.merge(entityType, 1, Integer::sum);
        saveToPlayer(player);
    }

    /**
     * Record a resource echo
     */
    public void recordResource(String blockType, ServerPlayer player) {
        resourceCount++;
        blockEchoes.merge(blockType, 1, Integer::sum);
        saveToPlayer(player);
    }

    /**
     * Record a repair action
     */
    public void recordRepair(ServerPlayer player) {
        repairCount++;
        saveToPlayer(player);
    }

    /**
     * Save data to player NBT
     */
    private void saveToPlayer(ServerPlayer player) {
        CompoundTag data = player.getPersistentData();
        CompoundTag echoTag = data.contains(DATA_KEY)
                ? data.getCompound(DATA_KEY)
                : new CompoundTag();
        save(echoTag);
        data.put(DATA_KEY, echoTag);
    }

    /**
     * Check if player has any echoes
     */
    public boolean hasAnyEchoes() {
        return structureCount > 0 || killCount > 0 || resourceCount > 0;
    }

    // Getters
    public int getStructureCount() { return structureCount; }
    public int getKillCount() { return killCount; }
    public int getResourceCount() { return resourceCount; }
    public int getRepairCount() { return repairCount; }
    public int getTotalEchoes() { return structureCount + killCount + resourceCount; }

    public Map<String, Integer> getMobEchoes() { return mobEchoes; }
    public Map<String, Integer> getBlockEchoes() { return blockEchoes; }
}