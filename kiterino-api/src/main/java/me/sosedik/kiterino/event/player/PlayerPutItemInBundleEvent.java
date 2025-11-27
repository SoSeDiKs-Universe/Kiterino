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
public class PlayerPutItemInBundleEvent extends PlayerInteractBundleEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final ItemStack item;
    private final int slot;
    private final boolean itemOnCursor;
    private boolean cancelled;

    public PlayerPutItemInBundleEvent(Player who, ItemStack bundle, ItemStack item, int slot, boolean itemOnCursor) {
        super(who, bundle);
        this.item = item;
        this.slot = slot;
        this.itemOnCursor = itemOnCursor;
    }

    /**
     * Gets the item that the player tries to put into the bundle
     *
     * @return the item involved in the event
     */
    public ItemStack getItem() {
        return this.item;
    }

    /**
     * Gets the slot the item or bundle is placed in
     *
     * @return item's or bundle's slot
     */
    public int getSlot() {
        return this.slot;
    }

    /**
     * Checks whether the item's placed from the cursor or the slot under cursor
     *
     * @return whether the item is put on top of the bundle
     */
    public boolean isItemOnCursor() {
        return this.itemOnCursor;
    }

    /**
     * @return if cancelled, the item won't try to be put into the bundle
     */
    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    /**
     * Set whether to deny trying to put the item into the bundle
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
