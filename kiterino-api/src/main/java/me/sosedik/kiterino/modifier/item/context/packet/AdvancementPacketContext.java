package me.sosedik.kiterino.modifier.item.context.packet;

import me.sosedik.kiterino.modifier.item.context.PacketItemModifierContext;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.Advancement;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.function.Supplier;

/**
 * Wraps around an advancement packet
 */
@NullMarked
public class AdvancementPacketContext implements PacketItemModifierContext {

    private final Object packet;
    private final NamespacedKey advancementKey;
    private final Supplier<Advancement> advancementSupplier;
    private @Nullable Advancement advancement;

    /**
     * Constructs advancement packet context
     *
     * @param packet packet
     * @param advancementKey advancement key
     * @param advancementSupplier advancement supplier
     */
    public AdvancementPacketContext(Object packet, NamespacedKey advancementKey, Supplier<Advancement> advancementSupplier) {
        this.packet = packet;
        this.advancementKey = advancementKey;
        this.advancementSupplier = advancementSupplier;
    }

    @Override
    public Object packet() {
        return this.packet;
    }

    /**
     * Gets the advancement key
     *
     * @return the advancement key
     */
    public NamespacedKey getAdvancementKey() {
        return this.advancementKey;
    }

    /**
     * Fetches the bukkit advancement wrapper
     *
     * @return advancement
     */
    public Advancement getAdvancement() {
        if (this.advancement == null) this.advancement = advancementSupplier.get();
        return this.advancement;
    }

}
