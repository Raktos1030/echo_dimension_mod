package com.raktos.echodimension.block;

import com.raktos.echodimension.EchoDimensionMod;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.UUID;

/**
 * Echo Portal Block Entity - Stores portal state and player tracking
 */
public class EchoPortalBlockEntity extends BlockEntity {

    private UUID activatingPlayer;
    private long activationTime;
    private int echoCount; // Number of echoes at this portal
    private boolean isActive;

    public EchoPortalBlockEntity(BlockPos pos, BlockState state) {
        super(EchoPortalBlockEntity.BLOCK_ENTITY, pos, state);
    }

    public static final net.neoforged.neoforge.registries.DeferredHolder<BlockEntityType<?>, BlockEntityType<EchoPortalBlockEntity>> BLOCK_ENTITY = 
            net.neoforged.neoforge.registries.DeferredRegister.create(
                    net.minecraft.core.registries.BuiltInRegistries.BLOCK_ENTITY_TYPE,
                    EchoDimensionMod.MOD_ID
            ).register(
                    "echo_portal_block_entity",
                    () -> BlockEntityType.Builder.of(EchoPortalBlockEntity::new, 
                            com.raktos.echodimension.block.EchoPortalBlock.BLOCK.get()).build(null)
            );

    /**
     * Activate the portal with a player
     */
    public void activate(UUID playerId) {
        this.activatingPlayer = playerId;
        this.activationTime = System.currentTimeMillis();
        this.isActive = true;
        this.setChanged();
    }

    /**
     * Get portal status for display
     */
    public Component getStatus() {
        if (!isActive) {
            return Component.literal("§7Inactive Portal");
        }
        return Component.literal("§dActive Portal §7- §e" + echoCount + " echoes recorded");
    }

    /**
     * Increment the echo count
     */
    public void addEcho() {
        this.echoCount++;
        this.setChanged();
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        if (activatingPlayer != null) {
            tag.putUUID("activatingPlayer", activatingPlayer);
        }
        tag.putLong("activationTime", activationTime);
        tag.putInt("echoCount", echoCount);
        tag.putBoolean("isActive", isActive);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains("activatingPlayer")) {
            activatingPlayer = tag.getUUID("activatingPlayer");
        }
        activationTime = tag.getLong("activationTime");
        echoCount = tag.getInt("echoCount");
        isActive = tag.getBoolean("isActive");
    }
}
