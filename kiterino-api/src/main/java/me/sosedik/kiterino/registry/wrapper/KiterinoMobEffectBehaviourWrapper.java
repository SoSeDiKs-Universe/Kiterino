package me.sosedik.kiterino.registry.wrapper;

import org.bukkit.damage.DamageSource;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.Range;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Wrapper for mob effect behaviors
 */
@NullMarked
public interface KiterinoMobEffectBehaviourWrapper {

    /**
     * Gets the effect's blend in duration ticks
     *
     * @return effect's blend in duration ticks
     */
    default int getBlendInDurationTicks() {
        return 0;
    }

    /**
     * Gets the effect's blend out duration ticks
     *
     * @return effect's blend out duration ticks
     */
    default int getBlendOutDurationTicks() {
        return 0;
    }

    /**
     * Gets the effect's blend advance duration ticks
     *
     * @return effect's blend advance duration ticks
     */
    default int getBlendOutAdvanceTicks() {
        return 0;
    }

    /**
     * Called when the effect is ticked.
     * If {@code false} is returned, the effect will be removed from the entity.
     *
     * @param entity entity that has the effect
     * @param amplifier effect amplifier
     * @return whether the effect was ticked
     */
    default boolean applyEffectTick(LivingEntity entity, int amplifier) {
        return true;
    }

    /**
     * Called when the effect is applied if this effect is {@link #isInstantaneous()}
     *
     * @param source effect source (e.g., thrown potion)
     * @param attacker attacker entity (e.g., thrown potion's owner)
     * @param target an entity the effect is applied to
     * @param amplifier effect amplifier
     * @param proximity intensity relative to maximum effect; 0.0: not affected; 1.0: fully hit by potion effects
     */
    default void applyInstantaneousEffect(@Nullable Entity source, @Nullable Entity attacker, LivingEntity target, int amplifier, @Range(from = 0L, to = 1L) double proximity) {
        this.applyEffectTick(target, amplifier);
    }

    /**
     * Checks whether the effect's tick method ({@link #applyEffectTick(LivingEntity, int)}) should be fired this tick
     *
     * @param duration effect duration
     * @param amplifier effect amplifier
     * @return whether to call tick method
     */
    default boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return false;
    }

    /**
     * Called when the effect was added or updated to the entity
     *
     * @param entity entity
     * @param amplifier effect amplifier
     */
    default void onEffectStarted(LivingEntity entity, int amplifier) {}

    /**
     * Called when the effect is being freshly added to the entity
     *
     * @param entity entity
     * @param amplifier effect amplifier
     */
    default void onEffectAdded(LivingEntity entity, int amplifier) {}

    /**
     * Called when the effect is expired
     *
     * @param entity entity
     * @param amplifier effect amplifier
     */
    default void onEffectExpired(LivingEntity entity, int amplifier) {}

    /**
     * Called when the effect is removed
     *
     * @param entity entity
     * @param amplifier effect amplifier
     */
    default void onEffectRemoved(LivingEntity entity, int amplifier) {}

    /**
     * Called when the entity having this effect dies
     *
     * @param entity entity
     * @param amplifier effect amplifier
     */
    default void onMobKilled(LivingEntity entity, int amplifier) {}

    /**
     * Called when the entity having this effect is hurt
     *
     * @param entity entity
     * @param amplifier effect amplifier
     * @param source damage source
     * @param amount damage amount
     */
    default void onMobHurt(LivingEntity entity, int amplifier, DamageSource source, float amount) {}

    /**
     * Checks whether this effect is instantaneous
     *
     * @return whether this effect is instantaneous
     */
    default boolean isInstantaneous() {
        return false;
    }

}
