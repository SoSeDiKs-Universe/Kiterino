package me.sosedik.kiterino.event.entity;

import org.bukkit.Bukkit;
import org.bukkit.entity.Ageable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.jspecify.annotations.NullMarked;

/**
 * Called when en entity turns into a baby or an adult.
 * <p>May be async during chunk generation.
 */
// Kiterino - Add EntityChangeAgeEvent
@NullMarked
public class MaybeAsyncEntityChangeAgeEvent extends EntityEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final boolean toAdult;

    public MaybeAsyncEntityChangeAgeEvent(Ageable entity) {
        super(entity, !Bukkit.isPrimaryThread());
        this.toAdult = entity.isAdult();
    }

    @Override
    public Ageable getEntity() {
        return (Ageable) super.getEntity();
    }

    /**
     * Checks whether this entity has turned into a baby
     *
     * @return whether this entity has turned into a baby
     */
    public boolean isBaby() {
        return !isAdult();
    }

    /**
     * Checks whether this entity has turned into an adult
     *
     * @return whether this entity has turned into an adult
     */
    public boolean isAdult() {
        return this.toAdult;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

}
