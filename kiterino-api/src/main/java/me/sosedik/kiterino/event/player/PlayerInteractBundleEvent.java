package me.sosedik.kiterino.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

/**
 * Represents an event of player interacting with a bundle item
 */
// Kiterino - Add Bundle API
@NullMarked
public abstract class PlayerInteractBundleEvent extends PlayerEvent {

    private final ItemStack bundle;

    protected PlayerInteractBundleEvent(Player who, ItemStack bundle) {
        super(who);
        this.bundle = bundle;
    }

    /**
     * Returns the bundle involved in this event
     *
     * @return bundle item
     */
    public ItemStack getBundle() {
        return this.bundle;
    }

}
