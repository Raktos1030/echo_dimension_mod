package com.echodimension.entities.echo;

import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

/**
 * Entité "Écho" - un fantôme qui représente un mob que le joueur a tué.
 * TODO Phase 4: Implémenter l'entité complète.
 */
public class EchoPhantom extends Monster {
    
    private static final EntityDataAccessor<Integer> DATA_ORIGINAL_MOB = SynchedEntityData.defineId(
        EchoPhantom.class, net.minecraft.network.syncher.EntityDataSerializers.INT
    );
    private static final EntityDataAccessor<String> DATA_ECHO_NAME = SynchedEntityData.defineId(
        EchoPhantom.class, net.minecraft.network.syncher.EntityDataSerializers.STRING
    );
    
    @Nullable
    private BlockPos originalDeathPos;
    private long echoTimestamp;
    
    public EchoPhantom(EntityType<? extends EchoPhantom> type, Level level) {
        super(type, level);
    }
    
    public static AttributeSupplier.Builder createAttributes() {
        // TODO Phase 4: Configurer les attributs de l'écho (peut être plus fort que l'original)
        return LivingEntity.createLivingAttributes()
            .add(Attributes.ATTACK_DAMAGE, 3.0)
            .add(Attributes.MAX_HEALTH, 20.0)
            .add(Attributes.FOLLOW_RANGE, 32.0);
    }
    
    @Override
    protected void registerGoals() {
        super.registerGoals();
        // TODO Phase 4: Ajouter des goals (suivre le joueur, attaque)
        // this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }
    
    /**
     * Configure cet écho avec les informations du mob original.
     * TODO Phase 4: Appelé lors de la création de l'écho.
     */
    public void configure(int originalMobType, String customName, BlockPos deathPos, long timestamp) {
        this.entityData.set(DATA_ORIGINAL_MOB, originalMobType);
        this.entityData.set(DATA_ECHO_NAME, customName);
        this.originalDeathPos = deathPos;
        this.echoTimestamp = timestamp;
    }
    
    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_ORIGINAL_MOB, 0);
        builder.define(DATA_ECHO_NAME, "Unknown Echo");
    }
    
    @Override
    public boolean hurt(DamageSource source, float amount) {
        // TODO Phase 4: Implémenter la logique de dégâts
        return super.hurt(source, amount);
    }
    
    /**
     * Crée un écho d'un mob à une position spécifique.
     * TODO Phase 4: Factory method pour créer des échos.
     */
    public static EchoPhantom createFromOriginal(EntityType<?> originalType, ServerLevel level, 
                                                  BlockPos spawnPos, long timestamp) {
        EchoPhantom echo = new EchoPhantom(ModEntities.ECHO_PHANTOM.get(), level);
        echo.moveTo(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ());
        echo.configure(
            EntityType.getId(originalType),
            originalType.getDescription().getString(),
            spawnPos,
            timestamp
        );
        return echo;
    }
}