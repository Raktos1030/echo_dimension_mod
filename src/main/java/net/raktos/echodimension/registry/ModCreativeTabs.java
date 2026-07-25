package net.raktos.echodimension.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.raktos.echodimension.EchoDimension;

import java.util.function.Supplier;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, EchoDimension.MODID);

    public static final Supplier<CreativeModeTab> ECHO_TAB = TABS.register("echo_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.echo_dimension"))
                    .icon(() -> new ItemStack(ModItems.ECHO_COMPASS.get()))
                    .displayItems((params, output) -> {
                        output.accept(ModItems.ECHO_COMPASS.get());
                        output.accept(ModItems.ECHO_STONE_ITEM.get());
                    })
                    .build());
}