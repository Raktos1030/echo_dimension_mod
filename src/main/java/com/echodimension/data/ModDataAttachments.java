package com.echodimension.data;

import com.echodimension.core.EchoDimensionMod;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.neoforged.bus.api.IEventBus;

import javax.annotation.Nullable;
import java.util.*;

/**
 * Gestionnaire des données de capture des actions du joueur.
 * Stocke les actions du joueur pour les reproduire dans la Dimension Écho.
 * TODO Phase 4: Implémenter le système complet de capture.
 */
public class ModDataAttachments {
    
    /**
     * Représente une action capturée du joueur.
     * TODO Phase 4: Étendre avec plus de types d'actions.
     */
    public static class CapturedAction {
        private final ActionType type;
        private final ResourceLocation blockType;
        private final int x, y, z;
        private final long timestamp;
        private final UUID playerId;
        
        public enum ActionType {
            BLOCK_BREAK,
            BLOCK_PLACE,
            ENTITY_KILL,
            STRUCTURE_BUILD
        }
        
        public CapturedAction(ActionType type, ResourceLocation blockType, int x, int y, int z, long timestamp, UUID playerId) {
            this.type = type;
            this.blockType = blockType;
            this.x = x;
            this.y = y;
            this.z = z;
            this.timestamp = timestamp;
            this.playerId = playerId;
        }
        
        public CompoundTag serialize() {
            CompoundTag tag = new CompoundTag();
            tag.putString("type", type.name());
            tag.putString("blockType", blockType.toString());
            tag.putInt("x", x);
            tag.putInt("y", y);
            tag.putInt("z", z);
            tag.putLong("timestamp", timestamp);
            tag.putString("playerId", playerId.toString());
            return tag;
        }
        
        public static CapturedAction deserialize(CompoundTag tag) {
            return new CapturedAction(
                ActionType.valueOf(tag.getString("type")),
                new ResourceLocation(tag.getString("blockType")),
                tag.getInt("x"),
                tag.getInt("y"),
                tag.getInt("z"),
                tag.getLong("timestamp"),
                UUID.fromString(tag.getString("playerId"))
            );
        }
    }
    
    /**
     * Données sauvegardées contenant toutes les actions capturées.
     * TODO Phase 4: Implémenter la gestion des données.
     */
    public static class EchoWorldData extends SavedData {
        
        private final Map<UUID, List<CapturedAction>> playerActions = new HashMap<>();
        private final Map<String, Long> echoEntities = new HashMap<>(); // entityId -> timestamp
        
        public EchoWorldData() {
            super();
        }
        
        public EchoWorldData(CompoundTag tag) {
            // TODO Phase 4: Charger les données depuis le NBT
            if (tag.contains("playerActions")) {
                ListTag actionsList = tag.getList("playerActions", Tag.TAG_COMPOUND);
                for (int i = 0; i < actionsList.size(); i++) {
                    CompoundTag actionTag = actionsList.getCompound(i);
                    UUID playerId = UUID.fromString(actionTag.getString("playerId"));
                    ListTag playerActions = actionTag.getList("actions", Tag.TAG_COMPOUND);
                    
                    List<CapturedAction> actions = new ArrayList<>();
                    for (int j = 0; j < playerActions.size(); j++) {
                        actions.add(CapturedAction.deserialize(playerActions.getCompound(j)));
                    }
                    this.playerActions.put(playerId, actions);
                }
            }
        }
        
        @Override
        public CompoundTag save(CompoundTag tag) {
            // TODO Phase 4: Sauvegarder les données en NBT
            return tag;
        }
        
        /**
         * Ajoute une action capturée.
         * TODO Phase 4: Implémenter l'ajout d'actions.
         */
        public void addAction(CapturedAction action) {
            playerActions.computeIfAbsent(action.playerId, k -> new ArrayList<>()).add(action);
            setDirty();
        }
        
        /**
         * Récupère les actions d'un joueur.
         * TODO Phase 4: Implémenter la récupération.
         */
        public List<CapturedAction> getPlayerActions(UUID playerId) {
            return playerActions.getOrDefault(playerId, Collections.emptyList());
        }
        
        /**
         * Efface toutes les données d'un joueur.
         * TODO Phase 4: Implémenter l'effacement.
         */
        public void clearPlayerActions(UUID playerId) {
            playerActions.remove(playerId);
            setDirty();
        }
    }
    
    public static void register(IEventBus eventBus) {
        // TODO Phase 4: Enregistrer la gestion des données via DataAttachmentRegistry
        EchoDimensionMod.LOGGER.info("Registering data attachments for Echo Dimension...");
    }
    
    /**
     * Récupère ou crée les données de la Dimension Écho pour un niveau.
     * TODO Phase 4: Implémenter la récupération des données.
     */
    @Nullable
    public static EchoWorldData getEchoWorldData(ServerLevel level) {
        // TODO Phase 4: return level.getDataStorage().computeIfAbsent(...)
        return null;
    }
}