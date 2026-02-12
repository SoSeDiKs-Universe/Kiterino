package me.sosedik.kiterino.util;

import com.google.common.base.Suppliers;
import io.papermc.paper.adventure.PaperAdventure;
import me.sosedik.kiterino.KiterinoConfig;
import me.sosedik.kiterino.modifier.item.ItemModifiersHandlerImpl;
import me.sosedik.kiterino.modifier.item.KiterinoItemModifierImpl;
import me.sosedik.kiterino.world.block.KiterinoBlock;
import net.kyori.adventure.key.Key;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.jspecify.annotations.NullMarked;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import static me.sosedik.kiterino.util.KiterinoUnsafeUtil.getField;

/**
 * Abomination to workaround Material enum
 * This accessibility hack should never be used, ever
 */
@NullMarked
// Kiterino - Injecting custom Materials
public class KiterinoBootstrapMaterialInjectorImpl extends KiterinoEnumExtender<Material> {

    private final List<KiterinoBlock> blocks = new ArrayList<>();
    private final Field materialItemField = getField(CraftMagicNumbers.class, "MATERIAL_ITEM");
    private final Field itemMaterialField = getField(CraftMagicNumbers.class, "ITEM_MATERIAL");
    private final Field materialBlockField = getField(CraftMagicNumbers.class, "MATERIAL_BLOCK");
    private final Field blockMaterialField = getField(CraftMagicNumbers.class, "BLOCK_MATERIAL");
    private final Field byNameField = getField(Material.class, "BY_NAME");
    private final Field idField = getField(Material.class, "id");
    private final Field ctorField = getField(Material.class, "ctor");
    private final Field dataField = getField(Material.class, "data");
    private final Field legacyField = getField(Material.class, "legacy");
    private final Field keyField = getField(Material.class, "key");
    private final Field itemTypeField = getField(Material.class, "itemType");
    private final Field blockTypeField = getField(Material.class, "blockType");
    private final Field materialRegistryMap = getField(Registry.SimpleRegistry.class, "map");

    @Override
    public void injectEnum(Material value) throws Exception {
        NamespacedKey materialKey = value.getKey();
        Item item = BuiltInRegistries.ITEM.getValueOrThrow(ResourceKey.create(Registries.ITEM, PaperAdventure.asVanilla(materialKey)));

        idField.set(value, 1);
        legacyField.set(value, false);
        ctorField.set(value, org.bukkit.material.MaterialData.class.getConstructor(Material.class, byte.class));
        if (item instanceof BlockItem blockItem && blockItem.getBlock() instanceof KiterinoBlock kiterinoBlock && kiterinoBlock.getBlockDataClasses() != null) {
            dataField.set(value, kiterinoBlock.getBlockDataClasses().first());
        } else {
            dataField.set(value, org.bukkit.material.MaterialData.class);
        }

        itemTypeField.set(value, Suppliers.memoize(() -> Registry.ITEM.get(materialKey)));
        blockTypeField.set(value, Suppliers.memoize(() -> Registry.BLOCK.get(materialKey)));

        ((Map<Material, Item>) materialItemField.get(null)).put(value, item);
        ((Map<Item, Material>) itemMaterialField.get(null)).put(item, value);
        if (item instanceof BlockItem blockItem) {
            ((Map<Material, Block>) materialBlockField.get(null)).put(value, blockItem.getBlock());
            ((Map<Block, Material>) blockMaterialField.get(null)).put(blockItem.getBlock(), value);

            if (blockItem.getBlock() instanceof KiterinoBlock kiterinoBlock)
                blocks.add(kiterinoBlock);
        }

        ((Map<NamespacedKey, Material>) materialRegistryMap.get(Registry.MATERIAL)).put(materialKey, value);
    }

    @SuppressWarnings({"unchecked", "java:S3011"})
    @Override
    public Material allocateEnum(Key key) {
        try {
            Material material = super.allocateEnum(key);
            keyField.set(material, new NamespacedKey(key.namespace(), key.value()));

            String enumName = material.name();
            enumNameField.set(material, enumName);
            ((Map<String, Material>) byNameField.get(null)).put(enumName, material);

            return material;
        } catch (Exception e) {
            throw new KiterinoEnumExtenderRuntimeException("Couldn't extend Material: " + key, e);
        }
    }

    private void finalizeInject() {
        if (allocated.isEmpty()) return;

        injectAllocated();

        blocks.forEach(KiterinoBlock::postInit);

        // Kiterino start - Implement packet item faker for injected items
        var modifier = KiterinoItemModifierImpl.MODIFIER_IMPL;
        if (!KiterinoConfig.itemModifiersOrder.contains(modifier.getModifierId())) {
            ItemModifiersHandlerImpl.modifiers.put(modifier.getModifierId(), modifier);
            KiterinoConfig.itemModifiersOrder.addLast(modifier.getModifierId());
            KiterinoConfig.log(Level.WARNING, "Item modifier with id " + modifier.getModifierId() + " is missing from \"item-modifiers.modification-order\" option, but custom item was injected. Forcing the modifier automatically.");
        } else {
            modifier.register();
        }
        // Kiterino end - Implement packet item faker for injected items
    }

    public static void init() {
        KiterinoBootstrapMaterialInjector.injector = new KiterinoBootstrapMaterialInjectorImpl();
    }

    public static void destruct() {
        ((KiterinoBootstrapMaterialInjectorImpl) KiterinoBootstrapMaterialInjector.injector).finalizeInject();
        KiterinoBootstrapMaterialInjector.injector = null;
    }

}
