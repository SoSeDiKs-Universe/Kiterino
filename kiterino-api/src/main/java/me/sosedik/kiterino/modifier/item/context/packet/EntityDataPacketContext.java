package me.sosedik.kiterino.modifier.item.context.packet;

import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.function.Function;

/**
 * Wraps around an entity data packet
 */
@NullMarked
public class EntityDataPacketContext extends EntityPacketContext {

    static Function<Object, @Nullable Entity> entityFetcher;

    private final EntityType entityType;

    /**
     * Constructs wrapper around an entity data packet
     *
     * @param packet nms packet
     * @param world world
     * @param entityId entity id
     * @param entity entity instance
     */
    public EntityDataPacketContext(Object packet, World world, int entityId, EntityType entityType, Object entity) {
        super(packet, world, entityId, () -> entityFetcher.apply(entity));
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
