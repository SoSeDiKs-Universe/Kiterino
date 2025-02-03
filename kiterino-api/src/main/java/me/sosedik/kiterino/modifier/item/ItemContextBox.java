package me.sosedik.kiterino.modifier.item;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemLore;
import me.sosedik.kiterino.modifier.item.context.ItemModifierContext;
import me.sosedik.kiterino.modifier.item.context.ItemModifierContextType;
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
	private final ItemModifierContextType contextType;
	private final ItemModifierContext context;
	private final Material type;
	private ItemStack item;

	/**
	 * Creates a new context box with viewer's locale
	 *
	 * @param viewer player viewing the item
	 * @param contextType context type
	 * @param context modification context
	 * @param item item
	 */
	public ItemContextBox(Player viewer, ItemModifierContextType contextType, ItemModifierContext context, ItemStack item) {
		this(viewer, viewer.locale(), contextType, context, item);
	}

	/**
	 * Creates a new context box
	 *
	 * @param viewer player viewing the item
	 * @param locale locale for modifications
	 * @param contextType context type
	 * @param context modification context
	 * @param item item
	 */
	public ItemContextBox(@Nullable Player viewer, Locale locale, ItemModifierContextType contextType, ItemModifierContext context, ItemStack item) {
		this.viewer = viewer;
		this.locale = locale;
		this.contextType = contextType;
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
	 * Gets the context type
	 *
	 * @return context type
	 */
	public ItemModifierContextType getContextType() {
		return this.contextType;
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

}
