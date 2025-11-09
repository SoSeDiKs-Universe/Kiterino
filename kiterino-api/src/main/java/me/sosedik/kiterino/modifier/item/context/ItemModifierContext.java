package me.sosedik.kiterino.modifier.item.context;

import org.jspecify.annotations.NullMarked;

/**
 * Context within which the item is modified
 */
@NullMarked
public interface ItemModifierContext {

    /**
     * Empty item modifier context
     */
    ItemModifierContext EMPTY = new ItemModifierContext() {};

}
