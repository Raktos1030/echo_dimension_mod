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
import net.minecraft.resources.Identifier;

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

        public static final DeferredBlock<Block> ECHO_PORTAL = BLOCKS.register(
            "echo_portal",
            () -> new EchoPortalBlock(BlockBehaviour.Properties.of()
                    .noCollision()
                    .noLootTable()
                    .strength(-1.0F) // incassable a la main, comme le portail du Nether
                    .sound(SoundType.SCULK)
                    .lightLevel(state -> 11)
                    .setId(ResourceKey.create(Registries.BLOCK,
                            Identifier.fromNamespaceAndPath(EchoDimension.MODID, "echo_portal"))))
    );
}