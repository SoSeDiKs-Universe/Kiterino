package me.sosedik.kiterino.modifier.item.context.packet;

import me.sosedik.kiterino.modifier.item.context.ItemModifierContext;
import me.sosedik.kiterino.modifier.item.context.ItemModifierContextType;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.function.Supplier;

/**
 * Wrapper for entity data packets that have entity id, but can't fetch the bukkit entity
 */
@NullMarked
public class UnknownEntityDataContext extends EntityPacketContext {

    /**
     * Constructs wrapper around a packet that contains an unknown entity
     *
     * @param contextType context type
     * @param parentContext parent context
     * @param packet   nms packet
     * @param world    world instance
     * @param entityId internal entity id
     */
    public UnknownEntityDataContext(ItemModifierContextType contextType, @Nullable ItemModifierContext parentContext, Object packet, @Nullable World world, int entityId) {
        super(contextType, parentContext, packet, world, entityId, (Supplier<Entity>) null);
    }

}
