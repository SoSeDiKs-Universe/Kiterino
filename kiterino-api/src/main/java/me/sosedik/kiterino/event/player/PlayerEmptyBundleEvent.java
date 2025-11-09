package me.sosedik.kiterino.event.player;

import com.google.common.base.Preconditions;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

/**
 * Called when the player tries to empty the bundle's contents
 */
// Kiterino - Add Bundle API
@NullMarked
public class PlayerEmptyBundleEvent extends PlayerInteractBundleEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final EquipmentSlot hand;
    private boolean cancelled;

    public PlayerEmptyBundleEvent(Player who, ItemStack bundle, EquipmentSlot hand) {
        super(who, bundle);
        Preconditions.checkArgument(hand.isHand(), "EquipmentSlot must be a hand");
        this.hand = hand;
    }

    /**
     * Gets the hand in which the bundle is
     *
     * @return the hand in which the bundle is
     */
    public EquipmentSlot getHand() {
        return this.hand;
    }

    /**
     * @return if cancelled, the bundle won't drop its contents
     */
    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    /**
     * Set whether to deny emptying bundle's contents
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
