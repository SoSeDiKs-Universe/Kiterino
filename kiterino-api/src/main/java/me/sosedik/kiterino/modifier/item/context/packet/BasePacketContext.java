package me.sosedik.kiterino.modifier.item.context.packet;

import me.sosedik.kiterino.modifier.item.context.ItemModifierContext;
import me.sosedik.kiterino.modifier.item.context.ItemModifierContextType;
import me.sosedik.kiterino.modifier.item.context.PacketItemModifierContext;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class BasePacketContext extends BaseItemContext implements PacketItemModifierContext {

	private final Object packet;

	/**
	 * Base packet context
	 *
	 * @param contextType   context type
	 * @param parentContext parent context
	 * @param packet packet
	 */
	public BasePacketContext(ItemModifierContextType contextType, @Nullable ItemModifierContext parentContext, Object packet) {
		super(contextType, parentContext);
		this.packet = packet;
	}

	@Override
	public Object getPacket() {
		return this.packet;
	}

}
