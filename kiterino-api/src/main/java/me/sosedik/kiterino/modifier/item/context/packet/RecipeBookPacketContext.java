package me.sosedik.kiterino.modifier.item.context.packet;

import me.sosedik.kiterino.modifier.item.context.PacketItemModifierContext;
import org.jspecify.annotations.NullMarked;

/**
 * Recipe book context
 *
 * @param packet nms packet
 * @param displayType item's display position
 */
@NullMarked
public record RecipeBookPacketContext(Object packet,
                                      DisplayType displayType) implements PacketItemModifierContext {

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
