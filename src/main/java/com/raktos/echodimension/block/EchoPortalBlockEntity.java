package com.raktos.echodimension.block;

import com.raktos.echodimension.EchoDimensionMod;
import com.raktos.echodimension.data.PlayerEchoData;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.UUID;

/**
 * Echo Portal Block Entity - Stores portal state and echo count
 */
public class EchoPortalBlockEntity extends BlockEntity {

    private UUID activatingPlayer;
    private long activationTime;
    private boolean isActive;
    private int echoCount = 0;

    public EchoPortalBlockEntity(BlockPos pos, BlockState state) {
        super(EchoDimensionMod.BLOCK_ENTITY_TYPES.get().getValue(EchoDimensionMod.location("echo_portal_block_entity")), pos, state);
    }

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
            return Component.literal("Portal Inactive");
        }
        return Component.literal("Portal Active - " + echoCount + " echoes recorded");
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