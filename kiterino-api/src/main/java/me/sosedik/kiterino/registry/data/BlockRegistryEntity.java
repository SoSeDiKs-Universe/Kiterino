package me.sosedik.kiterino.registry.data;

import io.papermc.paper.registry.RegistryBuilder;
import me.sosedik.kiterino.world.block.KiterinoBlock;
import org.bukkit.block.BlockType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * A data-centric version-specific registry entry for the {@link BlockType} type.
 */
@ApiStatus.Experimental
@ApiStatus.NonExtendable
@NullMarked
public interface BlockRegistryEntity {

    /**
     * Provides the block's nms implementation
     *
     * @return the block's nms implementation
     */
    @Nullable KiterinoBlock nmsBlock();

    /**
     * Constructs block properties with block id already set
     *
     * @return block properties
     */
    Object constructBlockProperties();

    /**
     * A mutable builder for the {@link BlockRegistryEntity} plugins may change in applicable registry events.
     */
    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface Builder extends BlockRegistryEntity, RegistryBuilder<BlockType> {

        /**
         * Configures the block's nms implementation
         *
         * @param nmsBlock the block's nms implementation
         * @return this builder
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder nmsBlock(KiterinoBlock nmsBlock);

    }

}
