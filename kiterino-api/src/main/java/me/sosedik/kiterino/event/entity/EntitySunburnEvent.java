package me.sosedik.kiterino.event.entity;

import com.google.common.base.Preconditions;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Called when an entity combusts due to sun
 */
// Kiterino - Add EntitySunburnEvent
@NullMarked
public class EntitySunburnEvent extends EntityEvent implements Cancellable {

	private static final HandlerList HANDLER_LIST = new HandlerList();

	private float burnDuration;
	private boolean cancelled;

	@ApiStatus.Internal
	public EntitySunburnEvent(Entity entity, float burnDuration) {
		super(entity);
		this.burnDuration = burnDuration;
	}

	/**
	 * Gets the burn duration in seconds
	 *
	 * @return the burn duration
	 */
	public float getBurnDuration() {
		return this.burnDuration;
	}

	/**
	 * Sets the burn duration in seconds
	 * 
	 * @param burnDuration burn duration
	 * @throws IllegalArgumentException if burn duration is below zero
	 */
	public void setBurnDuration(float burnDuration) {
		Preconditions.checkArgument(burnDuration >= 0);
		this.burnDuration = burnDuration;
	}

	@Override
	public LivingEntity getEntity() {
		return (LivingEntity) super.getEntity();
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
