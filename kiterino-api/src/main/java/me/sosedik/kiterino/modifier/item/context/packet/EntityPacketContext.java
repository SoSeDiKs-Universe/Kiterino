package me.sosedik.kiterino.modifier.item.context.packet;

import me.sosedik.kiterino.modifier.item.context.ItemModifierContext;
import me.sosedik.kiterino.modifier.item.context.ItemModifierContextType;
import me.sosedik.kiterino.modifier.item.context.PacketItemModifierContext;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.function.Supplier;

/**
 * Wrapper around packet containing an entity
 */
@NullMarked
public abstract class EntityPacketContext extends BasePacketContext {

    private final @Nullable World world;
    private final int entityId;
    protected @Nullable Entity entity;
    private @Nullable Supplier<@Nullable Entity> entityFetcher;
    protected boolean fetchedEntity;

    /**
     * Constructs wrapper around a packet that contains an entity
     *
     * @param contextType context type
     * @param parentContext parent context
     * @param packet nms packet
     * @param world world instance
     * @param entityId internal entity id
     * @param entity entity
     */
    protected EntityPacketContext(ItemModifierContextType contextType, @Nullable ItemModifierContext parentContext, Object packet, @Nullable World world, int entityId, @Nullable Entity entity) {
        super(contextType, parentContext, packet);
        this.world = world;
        this.entityId = entityId;
        this.entity = entity;
        this.fetchedEntity = true;
    }

    /**
     * Constructs wrapper around a packet that contains an entity
     *
     * @param contextType context type
     * @param parentContext parent context
     * @param packet nms packet
     * @param world world instance
     * @param entityId internal entity id
     * @param entityFetcher entity supplier
     */
    protected EntityPacketContext(ItemModifierContextType contextType, @Nullable ItemModifierContext parentContext, Object packet, @Nullable World world, int entityId, @Nullable Supplier<@Nullable Entity> entityFetcher) {
        super(contextType, parentContext, packet);
        this.world = world;
        this.entityId = entityId;
        this.entityFetcher = entityFetcher;
        this.fetchedEntity = entityFetcher == null;
    }

    /**
     * Gets the internal entity id
     *
     * @return the entity id
     */
    public int getEntityId() {
        return this.entityId;
    }

    /**
     * Get the world the entity's in
     *
     * @return world
     */
    public @Nullable World getWorld() {
        return this.world;
    }

    /**
     * Fetches the Bukkit entity by its internal id
     *
     * @return bukkit entity
     */
    public @Nullable Entity getEntity() {
        if (this.fetchedEntity) return this.entity;
        this.fetchedEntity = true;
        if (entityFetcher != null) this.entity = entityFetcher.get();
        return this.entity;
    }

    /**
     * Checks whether the entity was fetched
     *
     * @return whether the entity was fetched
     */
    public boolean isFetched() {
        return this.fetchedEntity;
    }

}
