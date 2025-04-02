package me.sosedik.kiterino.util;

import net.kyori.adventure.key.Key;
import org.bukkit.Material;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Used for injecting custom Materials.
 * <br>
 * <b>Can be used only during the bootstrap phase.
 */
@NullMarked
public class KiterinoBootstrapMaterialInjector {

	private KiterinoBootstrapMaterialInjector() {
		throw new IllegalStateException("Utility class");
	}

	static @Nullable IKiterinoBootstrapEnumInjector injector;

	/**
	 * Inject materials in the class using the plugin's name as namespace.
	 * Keys will be fetched from Material field names.
	 * <br>
	 * <b>Can be used only during the bootstrap phase.
	 *
	 * @param materialsClass class with public static final Material instances
	 * @throws IllegalStateException if not in bootstrap phase
	 */
	public static void injectMaterials(Class<?> materialsClass) {
		if (injector == null) throw new IllegalStateException("Can't inject Materials outside bootstrap phase!");
		injector.injectEnums(materialsClass);
	}

	/**
	 * Injects a custom material.
	 * If the key is null, it will be calculated from Plugin's provided namespace
	 * and Material field's name as a key.
	 * <br>
	 * <b>Can be used only during the bootstrap phase.
	 *
	 * @param key material key
	 * @return injected Material
	 * @throws IllegalStateException if not in bootstrap phase
	 */
	public static Material injectMaterial(Key key) {
		if (injector == null) throw new IllegalStateException("Can't inject Materials outside bootstrap phase!");
		return (Material) injector.allocateEnum(key);
	}

}
