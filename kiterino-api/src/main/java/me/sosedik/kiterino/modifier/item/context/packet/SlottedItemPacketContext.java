package me.sosedik.kiterino.modifier.item.context.packet;

import me.sosedik.kiterino.modifier.item.context.ItemModifierContext;
import me.sosedik.kiterino.modifier.item.context.ItemModifierContextType;
import me.sosedik.kiterino.modifier.item.context.PacketItemModifierContext;
import me.sosedik.kiterino.modifier.item.context.SlottedItemModifierContext;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Wrapper around set slot and window items packets
 */
@NullMarked
public class SlottedItemPacketContext extends BasePacketContext implements SlottedItemModifierContext {

    private final int slot;

    /**
     * Wrapper around set slot and window items packets
     *
     * @param contextType context type
     * @param parentContext parent context
     * @param packet packet
     * @param slot slot
     */
    public SlottedItemPacketContext(ItemModifierContextType contextType, @Nullable ItemModifierContext parentContext, Object packet, int slot) {
        super(contextType, parentContext, packet);
        this.slot = slot;
    }

    public int getSlot() {
        return this.slot;
    }

}
