package com.raktos.echodimension.network;

import com.raktos.echodimension.EchoDimensionMod;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.network.PacketDistributor;

public class EchoPacketHandler {
    public static final ResourceLocation CHANNEL_ID =
            ResourceLocation.fromNamespaceAndPath(EchoDimensionMod.MOD_ID, "echo_channel");
    public static final int PROTOCOL_VERSION = 1;

    public record SyncEchoDataPayload(int structures, int kills, int resources) implements CustomPacketPayload {
        public static final ResourceLocation ID =
                ResourceLocation.fromNamespaceAndPath(EchoDimensionMod.MOD_ID, "sync_echo_data");

        public static final Id<SyncEchoDataPayload> ID_TYPE = new Id<>(ID);
        public static final Type<SyncEchoDataPayload> TYPE = new Type<>(ID_TYPE);

        public static final StreamCodec<FriendlyByteBuf, SyncEchoDataPayload> STREAM_CODEC =
                StreamCodec.composite(
                        ByteBufCodecs.VAR_INT,
                        SyncEchoDataPayload::structures,
                        ByteBufCodecs.VAR_INT,
                        SyncEchoDataPayload::kills,
                        ByteBufCodecs.VAR_INT,
                        SyncEchoDataPayload::resources,
                        SyncEchoDataPayload::new
                );

        @Override
        public Id<? extends CustomPacketPayload> type() {
            return ID_TYPE;
        }
    }

    public static void register(IEventBus bus) {
        EchoDimensionMod.LOGGER.info("EchoPacketHandler registered");
    }

    public static void syncEchoData(ServerPlayer player, int structures, int kills, int resources) {
        player.server.execute(() -> {
            EchoDimensionMod.LOGGER.debug("Syncing echo data: structures={}, kills={}, resources={}",
                    structures, kills, resources);
        });
    }
}