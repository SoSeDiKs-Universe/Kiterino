package me.sosedik.kiterino.event.entity;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

/**
 * Called when an entity starts using an item
 */
@NullMarked
// Kiterino - Add EntityStartUsingItemEvent
public class EntityStartUsingItemEvent extends EntityEvent implements Cancellable {

	private static final HandlerList handlers = new HandlerList();

	private final ItemStack item;
	private int useDuration;
	private boolean cancelled;

	public EntityStartUsingItemEvent(LivingEntity entity, ItemStack item, int useDuration) {
		super(entity);
		this.item = item;
		this.useDuration = useDuration;
	}

	@Override
	public LivingEntity getEntity() {
		return (LivingEntity) this.entity;
	}

	/**
	 * Gets the item that's being used
	 *
	 * @return the item that's being used
	 */
	public ItemStack getItem() {
		return this.item;
	}

	/**
	 * Gets for how long in ticks the item should be used until it's ready
	 *
	 * @return item use duration
	 */
	public int getUseDuration() {
		return this.useDuration;
	}

	/**
	 * Sets for how long in ticks the item should be used until it's ready
	 *
	 * @param useDuration item use duration in ticks
	 */
	public void setUseDuration(int useDuration) {
		this.useDuration = useDuration;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @return if cancelled, the item will stop being in use
	 */
	@Override
	public boolean isCancelled() {
		return this.cancelled;
	}

	/**
	 * Set whether to cancel item use. If canceled,
	 * the item will stop being in use.
	 *
	 * @param cancel whether you wish to cancel this event
	 */
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
