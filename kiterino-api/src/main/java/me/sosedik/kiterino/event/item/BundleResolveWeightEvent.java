package me.sosedik.kiterino.event.item;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Range;
import org.jspecify.annotations.NullMarked;

/**
 * Called when bundle calculates weight for an item
 *
 * @deprecated not implemented yet
 */
// Kiterino - Add Bundle API
@Deprecated
@NullMarked
public class BundleResolveWeightEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final ItemStack bundle;
    private final ItemStack item;
    private int itemWeight;

    public BundleResolveWeightEvent(ItemStack bundle, ItemStack item, int itemWeight) {
        this.bundle = bundle;
        this.item = item;
        this.itemWeight = itemWeight;
    }

    /**
     * Gets the bundle item involved in the event
     *
     * @return bundle item
     */
    public ItemStack getBundle() {
        return this.bundle;
    }

    /**
     * Gets the item that is being put into the bundle.
     * There might be multiple items in the stack.
     *
     * @return item
     */
    public ItemStack getItem() {
        return this.item;
    }

    /**
     * Gets the occupied weight of the individual item in the stack within this calculation.
     * The overall weight will be multiplied by the amount of items in the stack, and the
     * individual item's weight can't be less than 1.
     *
     * @return the weight of the item
     */
    public int getItemWeight() {
        return this.itemWeight;
    }

    /**
     * Sets the occupied weight of individual item in the stack within this calculation.
     * The overall weight will be multiplied by the amount of items in the stack, and the
     * individual item's weight can't be less than 1.
     * Setting the weight above 64 will make the item never fit into the bundle.
     *
     * @param itemWeight the weight of the item
     */
    public void setItemWeight(@Range(from = 1, to = Integer.MAX_VALUE) int itemWeight) {
        this.itemWeight = Math.clamp(itemWeight, 1, 65);
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
