package me.sosedik.kiterino.modifier.item.context;

import org.jspecify.annotations.NullMarked;

/**
 * Item modifier context that has an NMS packet
 */
@NullMarked
public interface PacketItemModifierContext extends ItemModifierContext {

    /**
     * The item packet that was in the base of this context
     *
     * @return the item packet, if present
     */
    Object getPacket();

}
