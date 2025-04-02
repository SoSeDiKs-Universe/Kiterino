package me.sosedik.kiterino.util;

import net.kyori.adventure.key.Key;
import org.bukkit.entity.EntityType;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Used for injecting custom EntityTypes.
 * <br>
 * <b>Can be used only during the bootstrap phase.
 */
@NullMarked
public class KiterinoBootstrapEntityTypeInjector {

	private KiterinoBootstrapEntityTypeInjector() {
		throw new IllegalStateException("Utility class");
	}

	static @Nullable IKiterinoBootstrapEntityTypeInjector injector;

	/**
	 * Inject entity types in the class using the plugin's name as namespace.
	 * Keys will be fetched from EntityType field names.
	 * <br>
	 * <b>Can be used only during the bootstrap phase.
	 *
	 * @param entityTypesClass class with public static final EntityType instances
	 * @param nmsEntityTypesClass class with public static final NMS EntityType instances
	 * @param entityTypeDataProvider provides EntityTypeData for the entity
	 * @param attributeProvider provides AttributeSupplier for the entity
	 * @throws IllegalStateException if not in bootstrap phase
	 */
	public static void injectEntityTypes(Class<?> entityTypesClass, Class<?> nmsEntityTypesClass, Function<Key, Object> entityTypeDataProvider, Consumer<Key> attributeProvider) {
		if (injector == null) throw new IllegalStateException("Can't inject EntityType outside bootstrap phase!");
		injector.addEntityTypes(entityTypesClass, nmsEntityTypesClass, entityTypeDataProvider, attributeProvider);
	}

	/**
	 * Injects a custom entity type.
	 * If the key is null, it will be calculated from Plugin's provided namespace
	 * and EntityType field's name as a key.
	 * <br>
	 * <b>Can be used only during the bootstrap phase.
	 *
	 * @param key entity type key
	 * @return injected EntityType
	 * @throws IllegalStateException if not in bootstrap phase
	 */
	public static EntityType injectEntityType(Key key) {
		if (injector == null) throw new IllegalStateException("Can't inject EntityType outside bootstrap phase!");
		return (EntityType) injector.allocateEnum(key);
	}

}
