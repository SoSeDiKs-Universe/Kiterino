package me.sosedik.kiterino.util;

import com.google.common.base.Suppliers;
import io.papermc.paper.adventure.PaperAdventure;
import me.sosedik.kiterino.KiterinoConfig;
import me.sosedik.kiterino.modifier.item.ItemModifiersHandlerImpl;
import me.sosedik.kiterino.modifier.item.KiterinoItemModifierImpl;
import me.sosedik.kiterino.world.block.KiterinoBlock;
import net.kyori.adventure.key.Key;
import net.minecraft.core.component.DataComponents;
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
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;

import static me.sosedik.kiterino.util.KiterinoUnsafeUtil.getField;

/**
 * Abomination to workaround Material enum
 * This accessibility hack should never be used, ever
 */
@NullMarked
// Kiterino - Injecting custom Materials
public class KiterinoBootstrapMaterialInjectorImpl implements IKiterinoBootstrapMaterialInjector {

	private final int originalMaterialCount = Material.values().length;
	private final List<Material> allocated = new ArrayList<>();
	private final Field materialItemField = getField(CraftMagicNumbers.class, "MATERIAL_ITEM");
	private final Field itemMaterialField = getField(CraftMagicNumbers.class, "ITEM_MATERIAL");
	private final Field materialBlockField = getField(CraftMagicNumbers.class, "MATERIAL_BLOCK");
	private final Field blockMaterialField = getField(CraftMagicNumbers.class, "BLOCK_MATERIAL");
	private final Field nameField = getField(Enum.class, "name");
	private final Field ordinalField = getField(Enum.class, "ordinal");
	private final Field byNameField = getField(Material.class, "BY_NAME");
	private final Field maxStackField = getField(Material.class, "maxStack");
	private final Field durabilityField = getField(Material.class, "durability");
	private final Field idField = getField(Material.class, "id");
	private final Field ctorField = getField(Material.class, "ctor");
	private final Field dataField = getField(Material.class, "data");
	private final Field legacyField = getField(Material.class, "legacy");
	private final Field keyField = getField(Material.class, "key");
	private final Field itemTypeField = getField(Material.class, "itemType");
	private final Field blockTypeField = getField(Material.class, "blockType");
	private final Field injectedField = getField(Material.class, "injected");

	@SuppressWarnings("java:S3011")
	@Override
	public void injectMaterials(Class<?> materialsClass) {
		try {
			for (Field field : materialsClass.getDeclaredFields()) {
				if (field.getType() != Material.class) continue;

				int modifiers = field.getModifiers();
				if (!Modifier.isPublic(modifiers)) continue;
				if (!Modifier.isStatic(modifiers)) continue;
				if (!Modifier.isFinal(modifiers)) continue;

				Material material = (Material) field.get(null);
				NamespacedKey materialKey = material.getKey();
				Item item = BuiltInRegistries.ITEM.getValueOrThrow(ResourceKey.create(Registries.ITEM, PaperAdventure.asVanilla(materialKey)));

				maxStackField.set(material, item.getDefaultMaxStackSize());
				durabilityField.set(material, (short) (int) item.components().getOrDefault(DataComponents.MAX_DAMAGE, 0));

				idField.set(material, 1);
				legacyField.set(material, false);
				if (item instanceof BlockItem blockItem && blockItem.getBlock() instanceof KiterinoBlock kiterinoBlock && kiterinoBlock.getBlockDataClasses() != null) {
					Class<?> dataClass = kiterinoBlock.getBlockDataClasses().first();
					ctorField.set(material, dataClass.getConstructor(Material.class, byte.class));
					dataField.set(material, dataClass);
				} else {
					ctorField.set(material, org.bukkit.material.MaterialData.class.getConstructor(Material.class, byte.class));
					dataField.set(material, org.bukkit.material.MaterialData.class);
				}

				itemTypeField.set(material, Suppliers.memoize(() -> Registry.ITEM.get(materialKey)));
				blockTypeField.set(material, Suppliers.memoize(() -> Registry.BLOCK.get(materialKey)));

				((Map<Material, Item>) materialItemField.get(null)).put(material, item);
				((Map<Item, Material>) itemMaterialField.get(null)).put(item, material);
				if (item instanceof BlockItem blockItem) {
					((Map<Material, Block>) materialBlockField.get(null)).put(material, blockItem.getBlock());
					((Map<Block, Material>) blockMaterialField.get(null)).put(blockItem.getBlock(), material);
				}

				allocated.add(material);
			}
		} catch (Exception e) {
			throw new KiterinoMaterialExtenderRuntimeException("Couldn't extend Material in " + materialsClass, e);
		}
	}

	@SuppressWarnings({"unchecked", "java:S3011"})
	@Override
	public Material allocateMaterial(Key key) {
		try {
			Material material = KiterinoUnsafeUtil.allocateInstance(Material.class);
			injectedField.set(material, true);
			keyField.set(material, new NamespacedKey(key.namespace(), key.value()));

			String enumName = toMaterialName(key);
			nameField.set(material, enumName);
			((Map<String, Material>) byNameField.get(null)).put(enumName, material);

			return material;
		} catch (Exception e) {
			throw new KiterinoMaterialExtenderRuntimeException("Couldn't extend Material: " + key, e);
		}
	}

	@SuppressWarnings("java:S3011")
	private void finalizeInject() {
		try {
			if (allocated.isEmpty()) return;

			Material[] newValues = new Material[originalMaterialCount + allocated.size()];
			for (int i = 0; i < allocated.size(); i++) {
				Material material = allocated.get(i);
				int ordinal = originalMaterialCount + i;
				ordinalField.set(material, ordinal);
				newValues[ordinal] = material;
			}

			System.arraycopy(Material.values(), 0, newValues, 0, originalMaterialCount);
			Field valuesField = Material.class.getDeclaredField("$VALUES");
			KiterinoUnsafeUtil.setField(valuesField, newValues);

			Field enumConstants = Class.class.getDeclaredField("enumConstants");
			enumConstants.setAccessible(true);
			enumConstants.set(Material.class, null);
			Field enumConstantDirectory = Class.class.getDeclaredField("enumConstantDirectory");
			enumConstantDirectory.setAccessible(true);
			enumConstantDirectory.set(Material.class, null);

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
		} catch (Exception e) {
			throw new KiterinoMaterialExtenderRuntimeException("Couldn't extend Material", e);
		}
	}

	private static String toMaterialName(Key key) {
		return key.asString().toUpperCase(Locale.ROOT).replace(':', '_');
	}

	public static void init() {
		KiterinoBootstrapMaterialInjector.injector = new KiterinoBootstrapMaterialInjectorImpl();
	}

	public static void destruct() {
		((KiterinoBootstrapMaterialInjectorImpl) KiterinoBootstrapMaterialInjector.injector).finalizeInject();
		KiterinoBootstrapMaterialInjector.injector = null;
	}

}
