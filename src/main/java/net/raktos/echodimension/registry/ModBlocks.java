package net.raktos.echodimension.registry;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.raktos.echodimension.EchoDimension;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(EchoDimension.MODID);

    /** Pierre d'echo : bloc de base de la dimension. Emet une faible lueur. */
    public static final DeferredBlock<Block> ECHO_STONE = BLOCKS.registerSimpleBlock(
            "echo_stone",
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_CYAN)
                    .strength(2.0f, 8.0f)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.DEEPSLATE)
                    .lightLevel(state -> 3)
    );
}