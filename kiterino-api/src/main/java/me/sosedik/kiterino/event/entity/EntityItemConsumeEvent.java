package me.sosedik.kiterino.event.entity;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * This event will fire when a non-player entity is finishing consuming an item (food,
 * potion, milk bucket).
 * <br>
 * If the ItemStack is modified the server will use the effects of the new
 * item and not remove the original one from the player's inventory.
 * <br>
 * If the event is cancelled the effect will not be applied and the item will
 * not be removed from the player's inventory.
 * <p>
 * For a player-related event, see {@link PlayerItemConsumeEvent}.
 */
// Kiterino - Add EntityItemConsumeEvent
@NullMarked
public class EntityItemConsumeEvent extends EntityEvent implements ItemConsumeEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final EquipmentSlot hand;
    private ItemStack item;
    private @Nullable ItemStack replacement;

    private boolean cancelled;

    @ApiStatus.Internal
    public EntityItemConsumeEvent(final LivingEntity entity, final ItemStack item, final EquipmentSlot hand) {
        super(entity);
        this.item = item;
        this.hand = hand;
    }

    @Override
    public LivingEntity getEntity() {
        return (LivingEntity) super.getEntity();
    }

    /**
     * Gets the item that is being consumed. Modifying the returned item will
     * have no effect, you must use {@link
     * #setItem(ItemStack)} instead.
     *
     * @return an ItemStack for the item being consumed
     */
    public ItemStack getItem() {
        return this.item.clone();
    }

    /**
     * Set the item being consumed
     *
     * @param item the item being consumed
     */
    public void setItem(@Nullable ItemStack item) {
        if (item == null) {
            this.item = new ItemStack(Material.AIR);
        } else {
            this.item = item;
        }
    }

    /**
     * Get the hand used to consume the item.
     *
     * @return the hand
     */
    public EquipmentSlot getHand() {
        return this.hand;
    }

    /**
     * Return the custom item stack that will replace the consumed item, or {@code null} if no
     * custom replacement has been set (which means the default replacement will be used).
     *
     * @return The custom item stack that will replace the consumed item or {@code null}
     */
    public @Nullable ItemStack getReplacement() {
        return this.replacement;
    }

    /**
     * Set a custom item stack to replace the consumed item. Pass {@code null} to clear any custom
     * stack that has been set and use the default replacement.
     *
     * @param replacement Replacement item to set, {@code null} to clear any custom stack and use default
     */
    public void setReplacement(@Nullable ItemStack replacement) {
        this.replacement = replacement;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

}
