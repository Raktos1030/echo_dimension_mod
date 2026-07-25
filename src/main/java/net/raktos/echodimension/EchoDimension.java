package net.raktos.echodimension;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.raktos.echodimension.registry.ModBlocks;
import net.raktos.echodimension.registry.ModCreativeTabs;
import net.raktos.echodimension.registry.ModItems;

/**
 * Classe principale du mod Echo Dimension.
 * Phase 1 : bloc Echo Stone, item Echo Compass (teleporteur), dimension Echo (datapack).
 * Phase 2 : portails lis (DataComponent), creation automatique du portail oppose.
 */
@Mod(EchoDimension.MODID)
public class EchoDimension {
    public static final String MODID = "echo_dimension";

    public EchoDimension(IEventBus modEventBus) {
        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModCreativeTabs.TABS.register(modEventBus);
    }
}