package me.sosedik.kiterino.modifier.item.context.packet;

import me.sosedik.kiterino.modifier.item.context.PacketItemModifierContext;
import me.sosedik.kiterino.modifier.item.context.SlottedItemModifierContext;
import org.jspecify.annotations.NullMarked;

/**
 * Wrapper around set slot and window items packets
 *
 * @param packet packet
 * @param slot slot
 */
@NullMarked
public record SlottedItemPacketContext(Object packet, int slot) implements PacketItemModifierContext, SlottedItemModifierContext {

}
