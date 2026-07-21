package com.raktos.echodimension.item;

import com.raktos.echodimension.data.PlayerEchoData;
import com.raktos.echodimension.EchoDimensionMod;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class DimensionAccessItem extends Item {
    // FIX NeoForge 1.21: use Holder<Item> instead of Item
    public static final Holder<Item> ECHO_PORTAL_ITEM = EchoDimensionMod.ITEMS.register("echo_portal_item",
            () -> new DimensionAccessItem(
                    new Properties().stacksTo(1).durability(100)
            ));

    public DimensionAccessItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (level.isClientSide()) return InteractionResultHolder.pass(player.getItemInHand(hand));
        if (!(player instanceof ServerPlayer serverPlayer)) return InteractionResultHolder.pass(player.getItemInHand(hand));

        PlayerEchoData echoData = PlayerEchoData.get(serverPlayer);
        player.displayClientMessage(Component.literal("=== Echo Dimension Status ==="), false);
        player.displayClientMessage(Component.literal("Structures: " + echoData.getStructureCount()), false);
        player.displayClientMessage(Component.literal("Kills: " + echoData.getKillCount()), false);
        player.displayClientMessage(Component.literal("Resources: " + echoData.getResourceCount()), false);
        player.displayClientMessage(Component.literal("Total Echoes: " + echoData.getTotalEchoes()), false);
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }
}