package me.sosedik.kiterino.modifier.item.context.packet;

import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.jspecify.annotations.NullMarked;

import java.util.function.Supplier;

/**
 * Wrapper for entity data packets that have entity id, but can't fetch the bukkit entity
 */
@NullMarked
public class UnknownEntityDataContext extends EntityPacketContext {

	/**
	 * Constructs wrapper around a packet that contains an unknown entity
	 *
	 * @param packet   nms packet
	 * @param world    world instance
	 * @param entityId internal entity id
	 */
	public UnknownEntityDataContext(Object packet, World world, int entityId) {
		super(packet, world, entityId, (Supplier<Entity>) null);
	}

}
