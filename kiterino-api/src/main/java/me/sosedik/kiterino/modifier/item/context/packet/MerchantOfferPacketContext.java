package me.sosedik.kiterino.modifier.item.context.packet;

import me.sosedik.kiterino.modifier.item.context.ItemModifierContext;
import me.sosedik.kiterino.modifier.item.context.ItemModifierContextType;
import me.sosedik.kiterino.modifier.item.context.PacketItemModifierContext;
import org.bukkit.inventory.MerchantRecipe;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Wraps around a merchant offer packet
 */
@NullMarked
public class MerchantOfferPacketContext extends BasePacketContext implements PacketItemModifierContext {

	private final MerchantRecipe merchantRecipe;
	private final Slot slot;

	/**
	 * Wraps around a merchant offer packet
	 *
	 * @param contextType context type
	 * @param parentContext parent context
	 * @param packet nms packet
	 * @param merchantRecipe nms merchant offer
	 * @param slot item slot
	 */
	public MerchantOfferPacketContext(ItemModifierContextType contextType, @Nullable ItemModifierContext parentContext, Object packet, MerchantRecipe merchantRecipe, Slot slot) {
		super(contextType, parentContext, packet);
		this.merchantRecipe = merchantRecipe;
		this.slot = slot;
	}

	public MerchantRecipe getMerchantRecipe() {
		return this.merchantRecipe;
	}

	public Slot getSlot() {
		return this.slot;
	}

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
