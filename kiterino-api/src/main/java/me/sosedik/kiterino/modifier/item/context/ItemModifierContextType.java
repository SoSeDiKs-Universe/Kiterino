package me.sosedik.kiterino.modifier.item.context;

import me.sosedik.kiterino.modifier.item.context.packet.AdvancementPacketContext;
import me.sosedik.kiterino.modifier.item.context.packet.EntityDataPacketContext;
import me.sosedik.kiterino.modifier.item.context.packet.EntityEquipmentPacketContext;
import me.sosedik.kiterino.modifier.item.context.packet.MerchantOfferPacketContext;
import me.sosedik.kiterino.modifier.item.context.packet.RecipeBookPacketContext;
import me.sosedik.kiterino.modifier.item.context.packet.UnknownEntityDataContext;
import org.jspecify.annotations.NullMarked;

/**
 * Context type
 */
@NullMarked
public class ItemModifierContextType {

	/**
	 * Setting singular item in an inventory
	 */
	public static final ItemModifierContextType SET_SLOT = context(SlottedItemModifierContext.class).withName().withLore().build();

	/**
	 * Setting multiple items in an inventory
	 */
	public static final ItemModifierContextType WINDOW_ITEMS = context(SlottedItemModifierContext.class).withName().withLore().build();

	/**
	 * Equipment slots of other entities
	 */
	public static final ItemModifierContextType ENTITY_EQUIPMENT = context(EntityEquipmentPacketContext.class).build();

	/**
	 * Item inside the recipe book
	 */
	public static final ItemModifierContextType RECIPE_BOOK = context(RecipeBookPacketContext.class).withName().withLore().build();

	/**
	 * Ghost item as a response for the recipe book
	 */
	public static final ItemModifierContextType RECIPE_GHOST = context(RecipeBookPacketContext.class).withName().withLore().build();

	/**
	 * Advancement's item (also used in toasts due to lack of distinction)
	 */
	public static final ItemModifierContextType ADVANCEMENT = context(AdvancementPacketContext.class).build();

	/**
	 * Merchant offer (trade screen)
	 */
	public static final ItemModifierContextType MERCHANT_OFFER = context(MerchantOfferPacketContext.class).withName().withLore().build();

	/**
	 * Entity data packet
	 */
	public static final ItemModifierContextType ENTITY_DATA = context(EntityDataPacketContext.class).build();

	/**
	 * Unknown entity data packet
	 */
	public static final ItemModifierContextType UNKNOWN_ENTITY_DATA = context(UnknownEntityDataContext.class).build();

	/**
	 * Empty modification context with visible name and lore
	 */
	public static final ItemModifierContextType EMPTY_LORE = context(ItemModifierContext.EMPTY.getClass()).withName().withLore().build();

	/**
	 * Empty modification context with visible name and without visible lore
	 */
	public static final ItemModifierContextType EMPTY_NO_LORE = context(ItemModifierContext.EMPTY.getClass()).withName().build();

	// Allows parsing items inside hover event. Required to not kick the client when an injected item is shown.
	// By default, only parses hovers in system chat messages, but can be enabled globally via "item-modifiers.apply-modifiers-on-all-hover" option.
	/**
	 * Show item hover event in text components
	 */
	public static final ItemModifierContextType CHAT_ITEM = context(ItemModifierContext.EMPTY.getClass()).withName().withLore().build();

	private final Class<? extends ItemModifierContext> contextClass;
	private final boolean visibleName;
	private final boolean visibleLore;

	/**
	 * Constructs a context type
	 *
	 * @param contextClass context class
	 * @param visibleName whether the name is visible in this context
	 * @param visibleLore whether the lore is visible in this context
	 */
	public ItemModifierContextType(Class<? extends ItemModifierContext> contextClass, boolean visibleName, boolean visibleLore) {
		this.contextClass = contextClass;
		this.visibleName = visibleName;
		this.visibleLore = visibleLore;
	}

	/**
	 * Gets the context class
	 *
	 * @return context class
	 */
	public Class<? extends ItemModifierContext> getContextClass() {
		return this.contextClass;
	}

	/**
	 * Whether the name is visible in this context
	 *
	 * @return whether the name is visible
	 */
	public boolean hasVisibleName() {
		return this.visibleName;
	}

	/**
	 * Whether the lore is visible in this context
	 *
	 * @return whether the lore is visible
	 */
	public boolean hasVisibleLore() {
		return this.visibleLore;
	}

	/**
	 * Constructs a builder for item modifier context type
	 *
	 * @param contextClass context class
	 * @return builder
	 */
	public static Builder context(Class<? extends ItemModifierContext> contextClass) {
		return new Builder(contextClass);
	}

	/**
	 * Item modifier context type builder
	 */
	public static class Builder {

		private final Class<? extends ItemModifierContext> contextClass;
		private boolean visibleName;
		private boolean visibleLore;

		Builder(Class<? extends ItemModifierContext> contextClass) {
			this.contextClass = contextClass;
		}

		/**
		 * Marks that this context had a visible name
		 *
		 * @return this builder
		 */
		public Builder withName() {
			this.visibleName = true;
			return this;
		}

		/**
		 * Marks that this context had a visible lore
		 *
		 * @return this builder
		 */
		public Builder withLore() {
			this.visibleLore = true;
			return this;
		}

		/**
		 * Builds a context type
		 *
		 * @return context type
		 */
		public ItemModifierContextType build() {
			return new ItemModifierContextType(this.contextClass, this.visibleName, this.visibleLore);
		}

	}

}
