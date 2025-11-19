package me.sosedik.kiterino.event.item;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Called for:
 * <ul>
 * <li>Wet sponge producing water bucket</li>
 * </ul>
 */
// Kiterino - Add SideItemRemainEvent
@NullMarked
public class SideItemRemainEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final ItemStack item;
    private ItemStack remainder;

    /**
     * Side item remain event
     *
     * @param item item
     * @param remainder remaining item
     */
    public SideItemRemainEvent(ItemStack item, ItemStack remainder) {
        this.item = item;
        this.remainder = remainder;
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

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
