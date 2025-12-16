package me.sosedik.kiterino.event.server;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.NullMarked;

/**
 * Called when server settings (properties) has beed reloaded
 */
// Kiterino - Add ServerPropertiesUpdatedEvent
@NullMarked
public class ServerPropertiesUpdatedEvent extends Event {

	private static final HandlerList HANDLER_LIST = new HandlerList();

	@Override
	public HandlerList getHandlers() {
		return HANDLER_LIST;
	}

	public static HandlerList getHandlerList() {
		return HANDLER_LIST;
	}

}
