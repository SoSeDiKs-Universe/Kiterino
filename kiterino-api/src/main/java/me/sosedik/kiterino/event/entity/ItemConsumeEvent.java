package me.sosedik.kiterino.event.entity;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Represents item consume event
 */
@NullMarked
public interface ItemConsumeEvent extends Cancellable {

    /**
     * Gets the entity involved in event
     *
     * @return entity
     */
    LivingEntity getEntity();

    /**
     * Gets the item that is being consumed. Modifying the returned item will
     * have no effect, you must use {@link
     * #setItem(ItemStack)} instead.
     *
     * @return an ItemStack for the item being consumed
     */
    ItemStack getItem();

    /**
     * Set the item being consumed
     *
     * @param item the item being consumed
     */
    void setItem(@Nullable ItemStack item);

    /**
     * Get the hand used to consume the item.
     *
     * @return the hand
     */
    EquipmentSlot getHand();

    /**
     * Return the custom item stack that will replace the consumed item, or {@code null} if no
     * custom replacement has been set (which means the default replacement will be used).
     *
     * @return The custom item stack that will replace the consumed item or {@code null}
     */
    @Nullable ItemStack getReplacement();

    /**
     * Set a custom item stack to replace the consumed item. Pass {@code null} to clear any custom
     * stack that has been set and use the default replacement.
     *
     * @param replacement Replacement item to set, {@code null} to clear any custom stack and use default
     */
    void setReplacement(@Nullable ItemStack replacement);

    /**
     * Calls the event and tests if cancelled.
     *
     * @return {@code false} if event was cancelled, if cancellable. otherwise {@code true}.
     */
    default boolean callEvent() {
        return ((Event) this).callEvent();
    }

}
