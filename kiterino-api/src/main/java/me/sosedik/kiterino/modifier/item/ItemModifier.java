package me.sosedik.kiterino.modifier.item;

import me.sosedik.kiterino.modifier.item.context.ItemModifierContext;
import me.sosedik.kiterino.modifier.item.context.ItemModifierContextType;
import me.sosedik.kiterino.modifier.item.context.packet.BaseItemContext;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Locale;

/**
 * Makes modifications to an item
 */
@NullMarked
public abstract class ItemModifier {

    private final NamespacedKey modifierId;

    protected ItemModifier(NamespacedKey modifierId) {
        this.modifierId = modifierId;
    }

    /**
     * Unique ID of this modifier
     *
     * @return modifier id
     */
    public NamespacedKey getModifierId() {
        return modifierId;
    }

    /**
     * Registers this modifier
     */
    public void register() {
        ItemModifiersHandler.itemModifiersHandler().registerModifier(this);
    }

    /**
     * Unregisters this modifier
     */
    public void unregister() {
        ItemModifiersHandler.itemModifiersHandler().unregisterModifier(this);
    }

    /**
     * Whether this modifier should automatically
     * skip air items
     *
     * @return whether to skip air items
     */
    public boolean skipAir() {
        return true;
    }

    /**
     * Whether this modifier should automatically
     * skip the provided context
     *
     * @param context context
     * @return whether to skip context
     */
    public boolean skipContext(ItemModifierContext context) {
        return false;
    }

    /**
     * Modifies an item given the context
     *
     * @param contextBox item data
     * @return modification result
     */
    public abstract ModificationResult modify(ItemContextBox contextBox);

    /**
     * Modify item with viewer's locale, empty context, lore and slot 0.
     * Will return {@code null} if no modifications were made.
     *
     * @param viewer player viewing the item
     * @param item item
     * @return modified item or {@code null}
     */
    public static @Nullable ItemStack modifyItem(Player viewer, ItemStack item) {
        return modifyItem(null, viewer, viewer.locale(), item);
    }

    /**
     * Modify item with viewer's locale, empty context, lore and slot 0.
     * Will return {@code null} if no modifications were made.
     *
     * @param parentContext parent context
     * @param viewer player viewing the item
     * @param item item
     * @return modified item or {@code null}
     */
    public static @Nullable ItemStack modifyItem(@Nullable ItemModifierContext parentContext, Player viewer, ItemStack item) {
        return modifyItem(parentContext, viewer, viewer.locale(), item);
    }

    /**
     * Modify item with empty context, lore and slot 0
     *
     * @param viewer player viewing the item
     * @param locale locale
     * @param item item
     * @return modified item or {@code null}
     */
    public static @Nullable ItemStack modifyItem(@Nullable Player viewer, Locale locale, ItemStack item) {
        return modifyItem(null, viewer, locale, item);
    }

    /**
     * Modify item with empty context, lore and slot 0
     *
     * @param parentContext parent context
     * @param viewer player viewing the item
     * @param locale locale
     * @param item item
     * @return modified item or {@code null}
     */
    public static @Nullable ItemStack modifyItem(@Nullable ItemModifierContext parentContext, @Nullable Player viewer, Locale locale, ItemStack item) {
        var contextBox = new ItemContextBox(viewer, locale, parentContext == null ? ItemModifierContext.EMPTY_LORE : new BaseItemContext(ItemModifierContextType.EMPTY_LORE, parentContext), item.clone());
        return modifyItem(contextBox);
    }

    /**
     * Modify item given the context.
     * Will return {@code null} if no modifications were made.
     *
     * @param contextBox item context box
     * @return modified item or {@code null}
     */
    public static @Nullable ItemStack modifyItem(ItemContextBox contextBox) {
        return ItemModifiersHandler.itemModifiersHandler().modifyItem(contextBox);
    }

}
