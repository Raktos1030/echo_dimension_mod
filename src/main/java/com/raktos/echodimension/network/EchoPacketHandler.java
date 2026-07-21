package com.raktos.echodimension.network;

import com.raktos.echodimension.EchoDimensionMod;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ServerboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.IEventBus;

public class EchoPacketHandler {
    public static final ResourceLocation CHANNEL_ID = new ResourceLocation(EchoDimensionMod.MOD_ID, "echo_channel");
    public static final int PROTOCOL_VERSION = 1;

    public static void register(IEventBus bus) {
        EchoDimensionMod.LOGGER.info("EchoPacketHandler registered");
    }

    public static void sendToClient(ServerPlayer player, CustomPacketPayload payload) {
        player.connection.send(payload);
    }

    public static void syncEchoData(ServerPlayer player, int structures, int kills, int resources) {
        sendToClient(player, new SyncEchoDataPayload(structures, kills, resources));
    }

    public record SyncEchoDataPayload(int structures, int kills, int resources) implements CustomPacketPayload {
        public static final ResourceLocation ID = new ResourceLocation(EchoDimensionMod.MOD_ID, "sync_echo_data");

        @Override
        public ResourceLocation id() { return ID; }

        public void write(FriendlyByteBuf buffer) {
            buffer.writeVarInt(structures);
            buffer.writeVarInt(kills);
            buffer.writeVarInt(resources);
        }

        public static SyncEchoDataPayload read(FriendlyByteBuf buffer) {
            return new SyncEchoDataPayload(buffer.readVarInt(), buffer.readVarInt(), buffer.readVarInt());
        }

        public void handle(net.neoforged.neoforge.network.handling.PlayPayloadContext context) {
            context.workHandler().execute(() -> {
                EchoDimensionMod.LOGGER.debug("Echo data sync: s={} k={} r={}", structures, kills, resources);
            });
        }
    }
}
