package me.sosedik.kiterino.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;
import org.bukkit.inventory.EquipmentSlot;
import org.jspecify.annotations.NullMarked;

/**
 * Called when the server requests the player to swing their hand
 */
// Kiterino - Add PlayerArmSwingFromServerEvent
@NullMarked
public class PlayerArmSwingFromServerEvent extends PlayerAnimationEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final EquipmentSlot equipmentSlot;
    private boolean cancelled;

    /***
     * Constructs player arm swing from server event
     *
     * @param player player
     * @param equipmentSlot hand
     */
    public PlayerArmSwingFromServerEvent(Player player, EquipmentSlot equipmentSlot) {
        super(player, equipmentSlot == EquipmentSlot.HAND ? PlayerAnimationType.ARM_SWING : PlayerAnimationType.OFF_ARM_SWING);
        this.equipmentSlot = equipmentSlot;
        this.cancelled = false;
    }

    /**
     * Returns the hand of the arm swing
     *
     * @return the hand
     */
    public EquipmentSlot getHand() {
        return this.equipmentSlot;
    }

    /**
     * @return if cancelled, the hand will not be swung
     */
    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    /**
     * Set whether to cancel item use. If canceled,
     * the hand will not be swung.
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
