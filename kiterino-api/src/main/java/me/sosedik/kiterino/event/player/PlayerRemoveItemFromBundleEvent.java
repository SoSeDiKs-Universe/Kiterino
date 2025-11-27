package me.sosedik.kiterino.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

/**
 * Called when the player tries to put an item into a bundle
 */
// Kiterino - Add Bundle API
@NullMarked
public class PlayerRemoveItemFromBundleEvent extends PlayerInteractBundleEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final ItemStack item;
    private final int slot;
    private final boolean puttingOnCursor;
    private boolean cancelled;

    public PlayerRemoveItemFromBundleEvent(Player who, ItemStack bundle, ItemStack item, int slot, boolean puttingOnCursor) {
        super(who, bundle);
        this.item = item;
        this.slot = slot;
        this.puttingOnCursor = puttingOnCursor;
    }

    /**
     * Gets the item that the player tries to remove from the bundle
     *
     * @return the item involved in the event
     */
    public ItemStack getItem() {
        return this.item;
    }

    /**
     * Gets the slot the item or bundle is placed in
     *
     * @return resulting item slot
     */
    public int getSlot() {
        return this.slot;
    }

    /**
     * Checks whether the item's being removed into the cursor
     * or a slot under cursor
     *
     * @return whether the item is being remove into the cursor
     */
    public boolean isPuttingOnCursor() {
        return this.puttingOnCursor;
    }

    /**
     * @return if cancelled, the item won't be removed from the bundle
     */
    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    /**
     * Set whether to deny removing the item from the bundle
     *
     * @param cancel true if you wish to cancel this event
     */
    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
