package com.raktos.echodimension.item;

import com.raktos.echodimension.EchoDimensionMod;
import com.raktos.echodimension.data.PlayerEchoData;
import net.minecraft.advancements.CriteriaTriggers;
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
 * Dimension Access Item - Required to activate Echo Portals
 * Shows echo count when used
 */
public class DimensionAccessItem extends Item {

    public static final DeferredHolder<Item, DimensionAccessItem> ITEM = 
            net.neoforged.neoforge.registries.DeferredHolder.create(
                    net.neoforged.neoforge.registries.DeferredReference.hold(
                            net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(EchoDimensionMod.MOD_ID, "echo_portal_item"),
                            new DimensionAccessItem()
                    )
            );

    public DimensionAccessItem() {
        super(new Item.Properties()
                .stacksTo(1)
                .durability(100)
        );
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();

        if (level.isClientSide || player == null) {
            return InteractionResult.PASS;
        }

        ServerLevel serverLevel = (ServerLevel) level;
        ServerPlayer serverPlayer = (ServerPlayer) player;

        // Get or create player's echo data
        PlayerEchoData echoData = PlayerEchoData.get(serverPlayer);

        // Display echo status
        player.displayClientMessage(
                Component.literal("§5=== Echo Status ==="),
                false
        );
        player.displayClientMessage(
                Component.literal("§7Structures: §e" + echoData.getStructureCount()),
                false
        );
        player.displayClientMessage(
                Component.literal("§7Mobs Killed: §e" + echoData.getKillCount()),
                false
        );
        player.displayClientMessage(
                Component.literal("§7Resources Mined: §e" + echoData.getResourceCount()),
                false
        );
        player.displayClientMessage(
                Component.literal("§7Echos Repaired: §e" + echoData.getRepairCount()),
                false
        );

        // Trigger advancement if player has any echoes
        if (echoData.hasAnyEchoes()) {
            CriteriaTriggers.LOCATION.trigger(serverPlayer, player.blockPosition());
        }

        // Damage the item slightly
        ItemStack stack = context.getItemInHand();
        stack.hurtAndBreak(1, serverPlayer, ServerPlayer.USED_ITEM_STACK);

        return InteractionResult.SUCCESS;
    }

    @Override
    public Component getName(ItemStack stack) {
        return Component.translatable("item.echodimension.echo_portal_item");
    }

    @Override
    public Component getDescription(ItemStack stack) {
        return Component.translatable("item.echodimension.echo_portal_item.desc");
    }
}
