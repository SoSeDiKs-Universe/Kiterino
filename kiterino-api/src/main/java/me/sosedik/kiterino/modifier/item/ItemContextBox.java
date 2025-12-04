package me.sosedik.kiterino.modifier.item;

import io.papermc.paper.datacomponent.DataComponentType;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemLore;
import io.papermc.paper.datacomponent.item.TooltipDisplay;
import me.sosedik.kiterino.modifier.item.context.ItemModifierContext;
import net.kyori.adventure.text.ComponentLike;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Locale;

/**
 * Contains data required for modifying items
 */
@NullMarked
public class ItemContextBox {

    private final @Nullable Player viewer;
    private final Locale locale;
    private final ItemModifierContext context;
    private final Material type;
    private ItemStack item;

    /**
     * Creates a new context box with viewer's locale
     *
     * @param viewer player viewing the item
     * @param context modification context
     * @param item item
     */
    public ItemContextBox(@Nullable Player viewer, ItemModifierContext context, ItemStack item) {
        this(viewer, viewer == null ? Locale.US : viewer.locale(), context, item);
    }

    /**
     * Creates a new context box
     *
     * @param viewer player viewing the item
     * @param locale locale for modifications
     * @param context modification context
     * @param item item
     */
    public ItemContextBox(@Nullable Player viewer, Locale locale, ItemModifierContext context, ItemStack item) {
        this.viewer = viewer;
        this.locale = locale;
        this.type = item.getType();
        this.item = item;
        this.context = context;
    }

    /**
     * Gets the player receiving this item
     *
     * @return player viewer
     */
    public @Nullable Player getViewer() {
        return this.viewer;
    }

    /**
     * Locale that should be used for modification
     *
     * @return locale
     */
    public Locale getLocale() {
        return this.locale;
    }

    /**
     * Gets the modification context
     *
     * @return modification context
     */
    public ItemModifierContext getContext() {
        return this.context;
    }

    /**
     * Gets the original item's type
     *
     * @return item
     */
    public Material getInitialType() {
        return this.type;
    }

    /**
     * Sets the item's new type
     */
    public void setType(Material type) {
        this.item = this.item.withType(type);
    }

    /**
     * Gets the modifying item
     *
     * @return item
     */
    public ItemStack getItem() {
        return this.item;
    }

    /**
     * Completely replaces the item
     *
     * @param item item
     */
    public void setItem(ItemStack item) {
        this.item = item;
    }

    /**
     * Helper for adding a line to the item's lore
     *
     * @param line component
     */
    public void addLore(ComponentLike line) {
        ItemLore lore;
        if (this.item.hasData(DataComponentTypes.LORE)) {
            ItemLore currentLore = this.item.getData(DataComponentTypes.LORE);
            assert currentLore != null;
            lore = ItemLore.lore().lines(currentLore.lines()).addLine(line).build();
        } else {
            lore = ItemLore.lore().addLine(line).build();
        }
        this.item.setData(DataComponentTypes.LORE, lore);
    }

    /**
     * Helper for adding a line to the item's lore
     *
     * @param index line index
     * @param line component
     */
    public void addLore(int index, ComponentLike line) {
        ItemLore lore;
        if (this.item.hasData(DataComponentTypes.LORE)) {
            ItemLore currentLore = this.item.getData(DataComponentTypes.LORE);
            assert currentLore != null;
            lore = ItemLore.lore().lines(currentLore.lines()).addLine(index, line).build();
        } else {
            lore = ItemLore.lore().addLine(line).build();
        }
        this.item.setData(DataComponentTypes.LORE, lore);
    }

    /**
     * Helper for adding lines to the item's lore
     *
     * @param lines components
     */
    public void addLore(ComponentLike... lines) {
        addLore(List.of(lines));
    }

    /**
     * Helper for adding lines to the item's lore
     *
     * @param lines components
     */
    public void addLore(List<? extends ComponentLike> lines) {
        ItemLore lore;
        if (this.item.hasData(DataComponentTypes.LORE)) {
            ItemLore currentLore = this.item.getData(DataComponentTypes.LORE);
            assert currentLore != null;
            lore = ItemLore.lore().lines(currentLore.lines()).addLines(lines).build();
        } else {
            lore = ItemLore.lore().addLines(lines).build();
        }
        this.item.setData(DataComponentTypes.LORE, lore);
    }

    /**
     * Gets the size of the lore
     *
     * @return lore size
     */
    public int getLoreSize() {
        ItemLore currentLore = this.item.getData(DataComponentTypes.LORE);
        return currentLore == null ? 0 : currentLore.lines().size();
    }

    /**
     * Adds hidden components from tooltip display
     *
     * @param components component types
     */
    public void addHiddenComponents(DataComponentType... components) {
        TooltipDisplay tooltipDisplay = this.item.getData(DataComponentTypes.TOOLTIP_DISPLAY);
        if (tooltipDisplay == null) {
            this.item.setData(DataComponentTypes.TOOLTIP_DISPLAY, TooltipDisplay.tooltipDisplay().addHiddenComponents(components).build());
        } else if (!tooltipDisplay.hideTooltip()) {
            this.item.setData(DataComponentTypes.TOOLTIP_DISPLAY, TooltipDisplay.tooltipDisplay().hiddenComponents(tooltipDisplay.hiddenComponents()).addHiddenComponents(components).build());
        }
    }

}
