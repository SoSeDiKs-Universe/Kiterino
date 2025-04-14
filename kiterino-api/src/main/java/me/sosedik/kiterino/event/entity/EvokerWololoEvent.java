package me.sosedik.kiterino.event.entity;

import org.bukkit.entity.Evoker;
import org.bukkit.entity.Sheep;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.jspecify.annotations.NullMarked;

/**
 * Called when {@link Evoker} finishes wololo spell
 */
@NullMarked
// Kiterino - Add EvokerWololoEvent
public class EvokerWololoEvent extends EntityEvent {

	private static final HandlerList handlers = new HandlerList();

	private final Sheep wololoTarget;

	public EvokerWololoEvent(Evoker entity, Sheep wololoTarget) {
		super(entity);
		this.wololoTarget = wololoTarget;
	}

	@Override
	public Evoker getEntity() {
		return (Evoker) super.getEntity();
	}

	/**
	 * Gets the wololo spell target
	 *
	 * @return wololo target
	 */
	public Sheep getWololoTarget() {
		return this.wololoTarget;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

}
