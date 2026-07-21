package com.raktos.echodimension.item;

import com.raktos.echodimension.EchoDimensionMod;
import com.raktos.echodimension.data.PlayerEchoData;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.registries.DeferredHolder;

/**
 * Dimension Access Item - Shows echo count and allows portal activation
 * Required to use the Echo Portal
 */
public class DimensionAccessItem extends Item {

    public static final DeferredHolder<Item, DimensionAccessItem> ECHO_PORTAL_ITEM =
            EchoDimensionMod.ITEMS.register("echo_portal_item", DimensionAccessItem::new);

    public DimensionAccessItem() {
        super(new Item.Properties()
                .stacksTo(1)
                .durability(100)
        );
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        // Portal interaction is handled by EchoPortalBlock
        return InteractionResult.PASS;
    }

    @Override
    public InteractionResult use(Level level, Player player, net.minecraft.world.InteractionHand hand) {
        if (level.isClientSide || player == null) {
            return InteractionResult.PASS;
        }

        ServerLevel serverLevel = (ServerLevel) level;
        ServerPlayer serverPlayer = (ServerPlayer) player;

        // Get player's echo data
        PlayerEchoData echoData = PlayerEchoData.get(serverPlayer);

        // Display echo status to player
        player.displayClientMessage(
                Component.literal("=== Echo Dimension Status ==="), false
        );
        player.displayClientMessage(
                Component.literal("Structures: " + echoData.getStructureCount()), false
        );
        player.displayClientMessage(
                Component.literal("Kills: " + echoData.getKillCount()), false
        );
        player.displayClientMessage(
                Component.literal("Resources: " + echoData.getResourceCount()), false
        );
        player.displayClientMessage(
                Component.literal("Total Echoes: " + echoData.getTotalEchoes()), false
        );

        // Damage the item slightly
        ItemStack stack = player.getItemInHand(hand);
        stack.hurtAndBreak(1, serverPlayer, net.minecraft.world.entity.EquipmentSlot.MAINHAND);

        return InteractionResult.SUCCESS;
    }

    @Override
    public Component getName(ItemStack stack) {
        return Component.literal("Echo Portal Key");
    }

    @Override
    public Component getDescription() {
        return Component.literal("A mystical key that resonates with the Echo Dimension");
    }
}