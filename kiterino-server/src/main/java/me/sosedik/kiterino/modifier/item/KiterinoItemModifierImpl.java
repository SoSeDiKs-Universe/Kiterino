package me.sosedik.kiterino.modifier.item;

import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

import java.util.HashMap;
import java.util.Map;

/**
 * Handles visual modification for injected items
 */
@ApiStatus.Internal
@NullMarked
// Kiterino - Implement packet item faker for injected items
public final class KiterinoItemModifierImpl extends ItemModifier {

    public static final NamespacedKey MODIFIER_KEY = new NamespacedKey("kiterino", "item_faker");
    public static final KiterinoItemModifierImpl MODIFIER_IMPL = new KiterinoItemModifierImpl();

    private final Map<NamespacedKey, KiterinoItemModifier> modifiers = new HashMap<>();

    private KiterinoItemModifierImpl() {
        super(MODIFIER_KEY);
    }

    public void add(NamespacedKey key, KiterinoItemModifier modifier) {
        this.modifiers.put(key, modifier);
    }

    @Override
    public ModificationResult modify(ItemContextBox contextBox) {
        KiterinoItemModifier modifier = this.modifiers.get(contextBox.getItem().getType().getKey());
        if (modifier != null) {
            modifier.modify(contextBox);
            return ModificationResult.OK;
        }
        return ModificationResult.PASS;
    }

}
