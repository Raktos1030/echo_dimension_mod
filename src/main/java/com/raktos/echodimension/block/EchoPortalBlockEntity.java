package com.raktos.echodimension.block;

import com.raktos.echodimension.EchoDimensionMod;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class EchoPortalBlockEntity extends BlockEntity {
    public static final BlockEntityType<EchoPortalBlockEntity> TYPE =
            EchoDimensionMod.BLOCK_ENTITY_TYPES.register("echo_portal_block",
                    () -> BlockEntityType.Builder.of(EchoPortalBlockEntity::new,
                            EchoPortalBlock.BLOCK.get()).build(null));

    @Nullable
    private java.util.UUID activatingPlayer;
    private long activationTime;
    private boolean isActive;
    private int echoCount;

    public EchoPortalBlockEntity(BlockPos pos, BlockState state) {
        super(TYPE, pos, state);
    }

    public void activate(@Nullable java.util.UUID playerId) {
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

    public boolean isActive() { return isActive; }
    public int getEchoCount() { return echoCount; }

    @Nullable
    public java.util.UUID getActivatingPlayer() { return activatingPlayer; }

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
