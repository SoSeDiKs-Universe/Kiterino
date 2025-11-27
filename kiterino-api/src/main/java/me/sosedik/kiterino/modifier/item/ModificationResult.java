package me.sosedik.kiterino.modifier.item;

/**
 * Represents the result of modifying an item
 */
public enum ModificationResult {

    /**
     * Modified item
     */
    OK,
    /**
     * Skipped item
     */
    PASS,
    /**
     * Completely replaced item.
     * This will skip any further modifiers.
     * <p>
     * Generally meant to be used by the highest priority modifiers.
     * {@link ItemModifier#modifyItem(ItemContextBox)} may be called
     * to go through the modifiers for the replaced item.
     */
    RETURN

}
