package me.sosedik.kiterino.modifier.item.context;

import me.sosedik.kiterino.inventory.InventorySlotHelper;
import org.jspecify.annotations.NullMarked;

/**
 * Item modifier context that has a container slot
 */
@NullMarked
public interface SlottedItemModifierContext extends ItemModifierContext {

	/**
	 * Gets the raw container slot the item's in
	 *
	 * @return slot
	 * @see InventorySlotHelper
	 */
	int slot();

}
