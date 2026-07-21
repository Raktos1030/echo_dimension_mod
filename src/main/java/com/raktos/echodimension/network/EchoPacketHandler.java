package com.raktos.echodimension.network;

import com.raktos.echodimension.EchoDimensionMod;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.network.PacketDistributor;

public class EchoPacketHandler {
    public static final ResourceLocation CHANNEL_ID =
            ResourceLocation.fromNamespaceAndPath(EchoDimensionMod.MOD_ID, "echo_channel");
    public static final int PROTOCOL_VERSION = 1;

    public static void register(IEventBus bus) {
        EchoDimensionMod.LOGGER.info("EchoPacketHandler registered");
    }

    public static void syncEchoData(ServerPlayer player, int structures, int kills, int resources) {
        player.server.execute(() -> {
            EchoDimensionMod.LOGGER.debug("Syncing echo data: structures={}, kills={}, resources={}",
                    structures, kills, resources);
        });
    }

    public record SyncEchoDataPayload(int structures, int kills, int resources) implements CustomPacketPayload {
        public static final ResourceLocation ID =
                ResourceLocation.fromNamespaceAndPath(EchoDimensionMod.MOD_ID, "sync_echo_data");

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
    }
}
