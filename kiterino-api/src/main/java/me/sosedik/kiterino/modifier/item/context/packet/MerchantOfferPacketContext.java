package me.sosedik.kiterino.modifier.item.context.packet;

import me.sosedik.kiterino.modifier.item.context.PacketItemModifierContext;
import org.bukkit.inventory.MerchantRecipe;
import org.jspecify.annotations.NullMarked;

/**
 * Wraps around a merchant offer packet
 *
 * @param packet nms packet
 * @param merchantRecipe nms merchant offer
 * @param slot item slot
 */
@NullMarked
public record MerchantOfferPacketContext(Object packet,
                                         MerchantRecipe merchantRecipe,
                                         Slot slot) implements PacketItemModifierContext {

    /**
     * Represents trade screen's item slot position
     */
    public enum Slot {

        /**
         * First item cost
         */
        FIRST,
        /**
         * Second item cost
         */
        SECOND,
        /**
         * Trade result
         */
        RESULT

    }

}
