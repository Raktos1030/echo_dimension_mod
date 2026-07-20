package com.raktos.echodimension.network;

import com.raktos.echodimension.EchoDimensionMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.game.ServerboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.ServerCommonPacketListenerImpl;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.network.NetworkRegistry;
import net.neoforged.neoforge.network.handlers.IPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Echo Packet Handler - Handles network communication
 * Phase 1: Minimal (singleplayer focus), prepared for multiplayer expansion
 */
public class EchoPacketHandler {

    public static final Logger LOGGER = LogManager.getLogger();

    public static final ResourceLocation CHANNEL = ResourceLocation.fromNamespaceAndPath(
            EchoDimensionMod.MOD_ID, "echo_channel"
    );

    private static final String PROTOCOL_VERSION = "1.0";

    // Registered packets
    private static final Map<String, IPayloadHandler<?>> PACKET_HANDLERS = new HashMap<>();

    /**
     * Register network handlers
     */
    public static void register(IEventBus bus) {
        LOGGER.info("Registering Echo Dimension network handlers...");

        // Register custom payload handlers
        // Note: For Phase 1 (singleplayer), most sync is handled by NBT/saved data
        // This is prepared for when multiplayer is implemented

        LOGGER.info("Echo Dimension network handlers registered (v{})", PROTOCOL_VERSION);
    }

    /**
     * Send packet to client
     */
    public static void sendToClient(ServerPlayer player, EchoPacket packet) {
        // Prepared for multiplayer implementation
        LOGGER.debug("Sending packet to client: {}", packet.getType());
    }

    /**
     * Base class for Echo packets
     */
    public abstract static class EchoPacket {
        private final String type;

        protected EchoPacket(String type) {
            this.type = type;
        }

        public String getType() { return type; }

        public abstract void write(FriendlyByteBuf buffer);
        public abstract void read(FriendlyByteBuf buffer);

        public void write(FriendlyByteBuf buffer, String type) {
            buffer.writeUtf(type);
        }

        protected String readType(FriendlyByteBuf buffer) {
            return buffer.readUtf();
        }
    }

    /**
     * Sync Echo data packet
     */
    public static class SyncEchoDataPacket extends EchoPacket {
        private int structures;
        private int kills;
        private int resources;

        public SyncEchoDataPacket() {
            super("sync_echo_data");
        }

        public SyncEchoDataPacket(int structures, int kills, int resources) {
            super("sync_echo_data");
            this.structures = structures;
            this.kills = kills;
            this.resources = resources;
        }

        @Override
        public void write(FriendlyByteBuf buffer) {
            write(buffer, getType());
            buffer.writeVarInt(structures);
            buffer.writeVarInt(kills);
            buffer.writeVarInt(resources);
        }

        @Override
        public void read(FriendlyByteBuf buffer) {
            structures = buffer.readVarInt();
            kills = buffer.readVarInt();
            resources = buffer.readVarInt();
        }
    }
}
