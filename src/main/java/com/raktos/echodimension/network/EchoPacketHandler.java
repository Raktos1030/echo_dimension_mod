package com.raktos.echodimension.network;

import com.raktos.echodimension.EchoDimensionMod;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

/**
 * Echo Packet Handler - Network communication for Echo Dimension
 * Handles sync of echo data between server and client
 */
public class EchoPacketHandler {

    public static final ResourceLocation CHANNEL_ID = new ResourceLocation(
            EchoDimensionMod.MOD_ID, "echo_channel"
    );

    public static final int PROTOCOL_VERSION = 1;

    /**
     * Register network handlers
     */
    public static void register(IEventBus bus) {
        // Network registry handled by NeoForge dataconnect
        EchoDimensionMod.LOGGER.info("EchoPacketHandler registered");
    }

    /**
     * Send packet to client
     */
    public static void sendToClient(ServerPlayer player, CustomPacketPayload payload) {
        player.connection.send(payload);
    }

    /**
     * Sync Echo data to client
     */
    public static void syncEchoData(ServerPlayer player, int structures, int kills, int resources) {
        sendToClient(player, new SyncEchoDataPayload(structures, kills, resources));
    }

    /**
     * Payload for syncing echo data to client
     */
    public record SyncEchoDataPayload(int structures, int kills, int resources) 
            implements CustomPacketPayload {

        public static final ResourceLocation ID = new ResourceLocation(
                EchoDimensionMod.MOD_ID, "sync_echo_data"
        );

        @Override
        public ResourceLocation id() {
            return ID;
        }

        public void write(FriendlyByteBuf buffer) {
            buffer.writeVarInt(structures);
            buffer.writeVarInt(kills);
            buffer.writeVarInt(resources);
        }

        public static SyncEchoDataPayload read(FriendlyByteBuf buffer) {
            return new SyncEchoDataPayload(
                    buffer.readVarInt(),
                    buffer.readVarInt(),
                    buffer.readVarInt()
            );
        }

        public void handle(PlayPayloadContext context) {
            context.workHandler().execute(() -> {
                // Client-side handling - update HUD or local cache
                EchoDimensionMod.LOGGER.debug("Received echo data sync: structures={}, kills={}, resources={}",
                        structures, kills, resources);
            });
        }
    }

    /**
     * Portal activation notification payload
     */
    public record PortalActivationPayload(boolean enteringEcho, int echoCount)
            implements CustomPacketPayload {

        public static final ResourceLocation ID = new ResourceLocation(
                EchoDimensionMod.MOD_ID, "portal_activation"
        );

        @Override
        public ResourceLocation id() {
            return ID;
        }

        public void write(FriendlyByteBuf buffer) {
            buffer.writeBoolean(enteringEcho);
            buffer.writeVarInt(echoCount);
        }
    }
}