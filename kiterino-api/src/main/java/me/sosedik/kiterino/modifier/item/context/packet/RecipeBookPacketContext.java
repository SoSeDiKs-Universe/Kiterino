package me.sosedik.kiterino.modifier.item.context.packet;

import me.sosedik.kiterino.modifier.item.context.ItemModifierContext;
import me.sosedik.kiterino.modifier.item.context.ItemModifierContextType;
import me.sosedik.kiterino.modifier.item.context.PacketItemModifierContext;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Recipe book context
 */
@NullMarked
public class RecipeBookPacketContext extends BasePacketContext implements PacketItemModifierContext {

	private final DisplayType displayType;

	/**
	 * Recipe book context
	 *
	 * @param contextType context type
	 * @param parentContext parent context
	 * @param packet nms packet
	 * @param displayType item's display position
	 */
	public RecipeBookPacketContext(ItemModifierContextType contextType, @Nullable ItemModifierContext parentContext, Object packet, DisplayType displayType) {
		super(contextType, parentContext, packet);
		this.displayType = displayType;
	}

	public DisplayType getDisplayType() {
		return this.displayType;
	}

    /**
     * Represents item's display position
     */
    public enum DisplayType {
        /**
         * Resulting item
         */
        RESULT,
        /**
         * Ingredient item
         */
        INGREDIENT,
        /**
         * Crafting station
         */
        CRAFTING_STATION,
        /**
         * Fuel item
         */
        FUEL,
        /**
         * Smithing template
         */
        SMITHING_TEMPLATE,
        /**
         * Smithing base
         */
        SMITHING_BASE,
        /**
         * Smithing addition
         */
        SMITHING_ADDITION,
    }

}
