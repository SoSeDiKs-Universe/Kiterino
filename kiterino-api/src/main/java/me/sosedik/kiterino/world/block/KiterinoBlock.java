package me.sosedik.kiterino.world.block;

import it.unimi.dsi.fastutil.Pair;
import org.bukkit.block.BlockState;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Represents Kiterino NMS block.
 * <br>
 * Extend this class with your NMS block to use it with
 * Kiterino registry methods.
 */
@NullMarked
public interface KiterinoBlock {

	/**
	 * Serializes custom block to the client.
	 * <br>
	 * If the resulting block state is null, you MUST
	 * replace the block in some other way, e.g., via
	 * packet listener plugins.
	 * <br>
	 * Note: this will be called for each serialization, so
	 * caching the block state is preferable.
	 *
	 * @param currentState current NMS block state
	 * @return serialized block
	 */
	@Nullable BlockState serializeBlockToClient(Object currentState);

	/**
	 * Gets the block data classes for this block.
	 * Left is a bukkit interface, right is a craft implementation.
	 *
	 * @return block data classes
	 */
	default @Nullable Pair<Class<?>, Class<?>> getBlockDataClasses() {
		return null;
	}

}
