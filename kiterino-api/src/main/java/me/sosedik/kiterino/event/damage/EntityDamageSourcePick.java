package me.sosedik.kiterino.event.damage;

import org.bukkit.damage.DamageSource;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Called when {@link DamageSource} for entity damage is constructed
 */
@NullMarked
public class EntityDamageSourcePick extends Event {

    private static final HandlerList handlers = new HandlerList();

    private @Nullable Entity directEntity;
    private @Nullable Entity causingEntity;

    public EntityDamageSourcePick(@Nullable Entity directEntity, @Nullable Entity causingEntity) {
        this.directEntity = directEntity;
        this.causingEntity = causingEntity;
    }

    /**
     * Gets the direct entity
     *
     * @return the direct entity
     */
    public @Nullable Entity getDirectEntity() {
        return directEntity;
    }

    /**
     * Gets the causing entity
     *
     * @return the causing entity
     */
    public @Nullable Entity getCausingEntity() {
        return causingEntity;
    }

    /**
     * Sets the direct entity
     *
     * @param directEntity the direct entity
     */
    public void setDirectEntity(@Nullable Entity directEntity) {
        this.directEntity = directEntity;
    }

    /**
     * Sets the causing entity
     *
     * @param causingEntity the causing entity
     */
    public void setCausingEntity(@Nullable Entity causingEntity) {
        this.causingEntity = causingEntity;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
