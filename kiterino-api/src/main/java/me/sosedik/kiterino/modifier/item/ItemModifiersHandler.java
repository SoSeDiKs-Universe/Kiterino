package me.sosedik.kiterino.modifier.item;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@ApiStatus.Internal
@NullMarked
abstract class ItemModifiersHandler {

    static @Nullable ItemModifiersHandler itemModifiersHandler;

    /**
     * Registers item modifier
     *
     * @param itemModifier item modifier
     */
    public abstract void registerModifier(ItemModifier itemModifier);

    /**
     * Unregisters item modifier
     *
     * @param itemModifier item modifier
     */
    public abstract void unregisterModifier(ItemModifier itemModifier);

    /**
     * Applies modifiers to the item.
     * Will return {@code null} if no changes were made.
     *
     * @param contextBox context box
     * @return modified item or {@code null} if no changes
     */
    public abstract @Nullable ItemStack modifyItem(ItemContextBox contextBox);

    /**
     * Gets the item modifiers applier
     *
     * @return item modifiers handler
     */
    public static ItemModifiersHandler itemModifiersHandler() {
        assert itemModifiersHandler != null;
        return itemModifiersHandler;
    }

}
