package me.sosedik.kiterino.registry.data;

import io.papermc.paper.datacomponent.DataComponentType;
import io.papermc.paper.registry.RegistryBuilder;
import me.sosedik.kiterino.modifier.item.KiterinoItemModifier;
import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Range;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * A data-centric version-specific registry entry for the {@link ItemType} type.
 */
// TODO Vanilla /give tab completion support
@ApiStatus.Experimental
@ApiStatus.NonExtendable
@NullMarked
public interface ItemRegistryEntity {

    /**
     * Provides the item's visual modifier
     *
     * @return the item's visual modifier
     */
    @Nullable
    KiterinoItemModifier modifier();

    /**
     * Gets the item's component data
     *
     * @return component data or null
     */
    boolean hasData(DataComponentType type);

    /**
     * Gets the item's component data
     *
     * @return component data or null
     * @param <T> component data type
     * @deprecated not implemented yet
     */
    @Deprecated
    <T> @Nullable T getData(DataComponentType.Valued<T> type);

    /**
     * Provides the item's compost chance
     *
     * @return the item's compost chance
     */
    @Nullable Float compostChance();

    /**
     * Provides the item's nms implementation
     *
     * @return the item's nms implementation
     */
    @Nullable Object nmsItem();

    /**
     * Constructs item properties with item id already set
     *
     * @return item properties
     */
    Object constructItemProperties();

    /**
     * Checks whether this item has a block with the same id
     *
     * @return whether this item has a block variant
     */
    boolean hasAttachedBlock();

    /**
     * Tries to find the matching block with the same id
     *
     * @return matching block
     * @throws IllegalArgumentException if block does not exist
     */
    Object asBlockOrThrow();

    /**
     * A mutable builder for the {@link ItemRegistryEntity} plugins may change in applicable registry events.
     */
    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface Builder extends ItemRegistryEntity, RegistryBuilder<ItemType> {

        /**
         * Configures the item's visual modifier
         *
         * @param modifier the item's visual modifier
         * @return this builder
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder modifier(@Nullable KiterinoItemModifier modifier);

        /**
         * Sets item's component data
         *
         * @param type component type
         * @param value component value
         * @param <T> component data type
         * @deprecated not implemented yet
         */
        @Contract(value = "_, _ -> this", mutates = "this")
        @Deprecated
        <T> Builder setData(DataComponentType.Valued<T> type, @Nullable T value);

        /**
         * Sets item's component data
         *
         * @param type component type
         * @deprecated not implemented yet
         */
        @Contract(value = "_ -> this", mutates = "this")
        @Deprecated
        Builder setData(DataComponentType.NonValued type);

        /**
         * Configures the item's compost chance
         *
         * @param levelIncreaseChance the item's compost chance
         * @return this builder
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder compostChance(@Range(from = 0L, to = 1L) @Nullable Float levelIncreaseChance);

        /**
         * Configures the item's nms implementation
         *
         * @param nmsItem the item's nms implementation
         * @return this builder
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder nmsItem(@Nullable Object nmsItem);

    }

}
