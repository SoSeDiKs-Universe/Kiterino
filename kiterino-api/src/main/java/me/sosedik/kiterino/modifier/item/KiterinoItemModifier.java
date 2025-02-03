package me.sosedik.kiterino.modifier.item;

import org.jspecify.annotations.NullMarked;

/**
 * Handles visual modification for injected items
 */
@FunctionalInterface
@NullMarked
public interface KiterinoItemModifier {

	/**
	 * Modifies an item before sending it to the client.
	 * <p><b>Implementations must override the item type to match one of the vanilla ones.
	 *
	 * @param contextBox item data
	 */
	void modify(ItemContextBox contextBox);

}
