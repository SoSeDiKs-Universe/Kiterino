package me.sosedik.kiterino.modifier.item.context.packet;

import me.sosedik.kiterino.modifier.item.context.ItemModifierContext;
import me.sosedik.kiterino.modifier.item.context.ItemModifierContextType;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class BaseItemContext implements ItemModifierContext {

	private final ItemModifierContextType contextType;
	private final @Nullable ItemModifierContext parentContext;

	/**
	 * Base packet context
	 *
	 * @param contextType context type
	 * @param parentContext parent context
	 */
	public BaseItemContext(ItemModifierContextType contextType, @Nullable ItemModifierContext parentContext) {
		this.contextType = contextType;
		this.parentContext = parentContext;
	}

	@Override
	public ItemModifierContextType getContextType() {
		return this.contextType;
	}

	@Override
	public @Nullable ItemModifierContext getParentContext() {
		return this.parentContext;
	}

}
