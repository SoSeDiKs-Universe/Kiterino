package me.sosedik.kiterino.modifier.item.context.packet;

import me.sosedik.kiterino.modifier.item.context.ItemModifierContext;
import me.sosedik.kiterino.modifier.item.context.ItemModifierContextType;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.EquipmentSlot;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.function.Supplier;

/**
 * Wrapper around an entity equipment packet
 */
@NullMarked
public class EntityEquipmentPacketContext extends EntityPacketContext {

    private final EquipmentSlot slot;

    /**
     * Constructs entity equipment packet wrapper
     *
     * @param contextType context type
     * @param parentContext parent context
     * @param packet nms packet
     * @param world entity's world
     * @param entityId internal entity id
     * @param entityFetcher entity fetcher
     * @param slot equipment slot
     */
    public EntityEquipmentPacketContext(ItemModifierContextType contextType, @Nullable ItemModifierContext parentContext, Object packet, @Nullable World world, int entityId, @Nullable Supplier<@Nullable Entity> entityFetcher, EquipmentSlot slot) {
        super(contextType, parentContext, packet, world, entityId, entityFetcher);
        this.slot = slot;
    }

    /**
     * Gets the equipment slot
     *
     * @return the equipment slot
     */
    public EquipmentSlot getSlot() {
        return slot;
    }

}
