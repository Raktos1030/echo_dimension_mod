package net.raktos.echodimension.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.raktos.echodimension.EchoDimension;
import net.raktos.echodimension.block.EchoPortalBlock;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(EchoDimension.MODID);

    public static final DeferredBlock<Block> ECHO_STONE = BLOCKS.registerSimpleBlock(
            "echo_stone",
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_CYAN)
                    .strength(2.0f, 8.0f)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.DEEPSLATE)
                    .lightLevel(state -> 3)
    );

    public static final DeferredBlock<Block> ECHO_PORTAL = BLOCKS.register(
            "echo_portal",
            registryName -> new EchoPortalBlock(BlockBehaviour.Properties.of()
                    .setId(ResourceKey.create(Registries.BLOCK, registryName))
                    .noCollision()
                    .noLootTable()
                    .strength(-1.0F)
                    .sound(SoundType.SCULK)
                    .lightLevel(state -> 11)
            )
    );
}