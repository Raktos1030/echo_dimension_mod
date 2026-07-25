package net.raktos.echodimension.registry;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.raktos.echodimension.EchoDimension;
import net.raktos.echodimension.item.EchoCompassItem;

public class ModItems {
    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(EchoDimension.MODID);

    public static final DeferredItem<BlockItem> ECHO_STONE_ITEM =
            ITEMS.registerSimpleBlockItem("echo_stone", ModBlocks.ECHO_STONE);

    /** Compas d'echo : clic droit pour voyager entre l'Overworld et l'Echo. */
    public static final DeferredItem<Item> ECHO_COMPASS = ITEMS.registerItem(
            "echo_compass",
            EchoCompassItem::new,
            new Item.Properties().stacksTo(1).rarity(Rarity.RARE)
    );
}