package me.sosedik.kiterino.modifier.item.context.packet;

import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.EquipmentSlot;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Wrapper around an entity equipment packet
 */
@NullMarked
public class EntityEquipmentPacketContext extends EntityPacketContext {

	private final EquipmentSlot slot;

	/**
	 * Constructs entity equipment packet wrapper
	 *
	 * @param packet nms packet
	 * @param world entity's world
	 * @param entityId internal entity id
	 * @param entity entity
	 * @param slot equipment slot
	 */
	public EntityEquipmentPacketContext(Object packet, World world, int entityId, @Nullable Entity entity, EquipmentSlot slot) {
		super(packet, world, entityId, entity);
		this.slot = slot;
	}

	/**
	 * Gets the equipment slot
	 *
	 * @return the equipment slot
	 */
	public EquipmentSlot getSlot() {
		return slot;
	}

}
