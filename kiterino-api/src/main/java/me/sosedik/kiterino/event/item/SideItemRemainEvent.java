package me.sosedik.kiterino.event.item;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Called for:
 * <ul>
 * <li>Recipe leftovers</li>
 * <li>Wet sponge producing water bucket</li>
 * </ul>
 */
// Kiterino - Add SideItemRemainEvent
@NullMarked
public class SideItemRemainEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final Reason reason;
    private final ItemStack item;
    private ItemStack remainder;

    /**
     * Side item remain event
     *
     * @param reason remainder reason
     * @param item item
     * @param remainder remaining item
     */
    public SideItemRemainEvent(Reason reason, ItemStack item, ItemStack remainder) {
        this.reason = reason;
        this.item = item;
        this.remainder = remainder;
    }

    /**
     * Gets the remainder reason
     *
     * @return the remainder reason
     */
    public Reason getReason() {
        return this.reason;
    }

    /**
     * Gets the original item
     *
     * @return original item
     */
    public ItemStack getItem() {
        return this.item;
    }

    /**
     * Gets the remainder
     *
     * @return remaining item
     */
    public ItemStack getRemainder() {
        return this.remainder;
    }

    /**
     * Sets the remainder
     *
     * @param item the remaining item
     */
    public void setRemainder(@Nullable ItemStack item) {
        this.remainder = item == null ? ItemStack.empty() : item;
    }

    /**
     * Remainder reason
     */
    public enum Reason {
        /**
         * Leftover after crafting
         */
        CRAFTING_REMAINDER,
        /**
         * Bucket filled with water after smelting a wet sponge
         */
        WET_SPONGE,
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
