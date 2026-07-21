package com.raktos.echodimension.block;

import com.raktos.echodimension.EchoDimensionMod;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import java.util.UUID;

public class EchoPortalBlockEntity extends BlockEntity {
    private UUID activatingPlayer;
    private long activationTime;
    private boolean isActive;
    private int echoCount = 0;

    public EchoPortalBlockEntity(BlockPos pos, BlockState state) {
        super(EchoDimensionMod.BLOCK_ENTITY_TYPES.get().getValue(EchoDimensionMod.location("echo_portal_block")), pos, state);
    }

    public void activate(UUID playerId) {
        this.activatingPlayer = playerId;
        this.activationTime = System.currentTimeMillis();
        this.isActive = true;
        this.setChanged();
    }

    public void addEcho() {
        this.echoCount++;
        this.setChanged();
    }

    public Component getStatus() {
        return Component.literal(isActive ? "Active - " + echoCount + " echoes" : "Inactive");
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        if (activatingPlayer != null) tag.putUUID("activatingPlayer", activatingPlayer);
        tag.putLong("activationTime", activationTime);
        tag.putInt("echoCount", echoCount);
        tag.putBoolean("isActive", isActive);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains("activatingPlayer")) activatingPlayer = tag.getUUID("activatingPlayer");
        activationTime = tag.getLong("activationTime");
        echoCount = tag.getInt("echoCount");
        isActive = tag.getBoolean("isActive");
    }
}
