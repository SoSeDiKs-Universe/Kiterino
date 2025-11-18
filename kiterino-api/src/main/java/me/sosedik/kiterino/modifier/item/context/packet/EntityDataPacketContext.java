package me.sosedik.kiterino.modifier.item.context.packet;

import me.sosedik.kiterino.modifier.item.context.ItemModifierContext;
import me.sosedik.kiterino.modifier.item.context.ItemModifierContextType;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.UnknownNullability;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.function.Function;

/**
 * Wraps around an entity data packet
 */
@NullMarked
public class EntityDataPacketContext extends EntityPacketContext {

    static @UnknownNullability Function<Object, @Nullable Entity> entityFetcher;

    private final EntityType entityType;

    /**
     * Constructs wrapper around an entity data packet
     *
     * @param contextType context type
     * @param parentContext parent context
     * @param packet nms packet
     * @param world world
     * @param entityId entity id
     * @param entity entity instance
     */
    public EntityDataPacketContext(ItemModifierContextType contextType, @Nullable ItemModifierContext parentContext, Object packet, @Nullable World world, int entityId, EntityType entityType, Object entity) {
        super(contextType, parentContext, packet, world, entityId, () -> entityFetcher.apply(entity));
        this.entityType = entityType;
    }

    /**
     * Gets the entity type of this entity.
     * Does not require fetching the entity instance.
     *
     * @return entity type
     */
    public EntityType getEntityType() {
        return this.entityType;
    }

}
