package me.sosedik.kiterino.modifier.item.context;

import me.sosedik.kiterino.modifier.item.context.packet.BaseItemContext;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Context within which the item is modified
 */
@NullMarked
public interface ItemModifierContext {

    /**
     * Gets the context type
     *
     * @return context type
     */
    ItemModifierContextType getContextType();

    /**
     * Gets the parent context
     *
     * @return parent context
     */
    default @Nullable ItemModifierContext getParentContext() {
        return null;
    }

    default ItemModifierContext getRootContext() {
        ItemModifierContext context = this;
        ItemModifierContext parentContext;
        while ((parentContext = context.getParentContext()) != null)
            context = parentContext;
        return context;
    }

    /**
     * Empty item modifier context with lore
     */
    ItemModifierContext EMPTY_LORE = new BaseItemContext(ItemModifierContextType.EMPTY_LORE, null);

    /**
     * Empty item modifier context without lore
     */
    ItemModifierContext EMPTY_NO_LORE = new BaseItemContext(ItemModifierContextType.EMPTY_NO_LORE, null);

}
