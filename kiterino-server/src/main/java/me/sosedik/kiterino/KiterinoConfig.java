package me.sosedik.kiterino;

import com.google.common.base.Throwables;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

@SuppressWarnings("unused")
@NullMarked
public final class KiterinoConfig {

	private KiterinoConfig() {
		throw new IllegalStateException("Utility class");
	}

	private static final String HEADER = """
            This is the main configuration file for Kiterino
            
            GitHub: https://github.com/SoSeDiKs-Universe/Kiterino
            """;

	private static File configFile;
	public static YamlConfiguration config;
	private static int version;
	static boolean verbose;

	public static void init(File configFile) {
		KiterinoConfig.configFile = configFile;
		config = new YamlConfiguration();
		try {
			config.load(configFile);
		} catch (IOException ignored) {
		} catch (InvalidConfigurationException ex) {
			Bukkit.getLogger().log(Level.SEVERE, "Could not load kiterino.yml, please correct your syntax errors", ex);
			throw Throwables.propagate(ex);
		}
		config.options().header(HEADER);
		config.options().copyDefaults(true);
		verbose = getBoolean(config, "verbose", false);

		version = getInt(config, "config-version", 1);
		set(config, "config-version", 1);

		readConfig(KiterinoConfig.class, null);
	}

	static void readConfig(Class<?> clazz, @Nullable Object instance) {
		readConfig(configFile, config, clazz, instance);
	}

	public static void readConfig(File configFile, YamlConfiguration config, Class<?> clazz, @Nullable Object instance) {
		for (Method method : clazz.getDeclaredMethods()) {
			if (!Modifier.isPrivate(method.getModifiers())) continue;
			if (method.getParameterTypes().length != 0) continue;
			if (method.getReturnType() != Void.TYPE) continue;

			try {
				method.setAccessible(true);
				method.invoke(instance);
			} catch (InvocationTargetException ex) {
				throw Throwables.propagate(ex.getCause());
			} catch (Exception ex) {
				Bukkit.getLogger().log(Level.SEVERE, "Error invoking " + method, ex);
			}
		}

		try {
			config.save(configFile);
		} catch (IOException ex) {
			Bukkit.getLogger().log(Level.SEVERE, "Could not save " + configFile, ex);
		}
	}

	public static void log(String s) {
		if (verbose) {
			log(Level.INFO, s);
		}
	}

	public static void log(Level level, String s) {
		Bukkit.getLogger().log(level, s);
	}

	public static void set(YamlConfiguration config, String path, @Nullable Object val, String... comments) {
		config.addDefault(path, val);
		config.set(path, val);
		if (comments.length > 0) config.setComments(path, List.of(comments));
	}

	public static boolean getBoolean(YamlConfiguration config, String path, boolean def, String... comments) {
		config.addDefault(path, def);
		if (comments.length > 0) config.setComments(path, List.of(comments));
		return config.getBoolean(path, def);
	}

	public static int getInt(YamlConfiguration config, String path, int def, String... comments) {
		config.addDefault(path, def);
		if (comments.length > 0) config.setComments(path, List.of(comments));
		return config.getInt(path, def);
	}

	public static <T> List<T> getList(YamlConfiguration config, String path, @Nullable List<T> def, String... comments) {
		config.addDefault(path, def);
		if (comments.length > 0) config.setComments(path, List.of(comments));
		return (List<T>) config.getList(path, def);
	}

	public static int getVersion() {
		return version;
	}

	// Kiterino start - Item Modifiers API
	public static boolean itemModifiersLogMissingIds;
	public static final List<NamespacedKey> itemModifiersOrder = new ArrayList<>();
	private static void itemModifiers() {
		itemModifiersLogMissingIds = getBoolean(config, "item-modifiers.log-missing-ids", true, "Whether to log the id if the item modifier's id is missing in modification order");
		getList(config, "item-modifiers.modification-order", List.of(), "List of item modifier ids, determines modification order").forEach(id -> itemModifiersOrder.add(NamespacedKey.fromString((String) id)));
	}
	// Kiterino end - Item Modifiers API

	// Kiterino start - Parse items in show_item hover event
	public static boolean parseItemHoversEverywhere;
	private static void parseItemHoversEverywhere() {
		parseItemHoversEverywhere = getBoolean(config, "item-modifiers.apply-modifiers-on-all-hover", false, "Apply item modifiers on all show_item hover events in text components.", "By default, they are applied only to system chat messages.", "Only enable if your client is getting kicked due to injected items.");
	}
	// Kiterino end - Parse items in show_item hover event

	// Kiterino start - Prevent creative from overriding items
	public static boolean preventCreativeItemOverride;
	private static void preventCreativeItemsOverride() {
		preventCreativeItemOverride = getBoolean(config, "items.prevent-creative-override", true, "Prevent creative from overriding items");
	}
	// Kiterino end - Prevent creative from overriding items

	// Kiterino start - Allow server-side translatables on items
	public static boolean denyServerTranslatablesOnItems;
	private static void allowServerTranslatablesOnItems() {
		denyServerTranslatablesOnItems = !getBoolean(config, "items.allow-server-translatables", true, "Apply server-side translatables to items before sending to the client");
	}
	// Kiterino end - Allow server-side translatables on items

	// Kiterino start - Less limited recipe matcher
	public static boolean allowDamagedItemsInRecipeBook;
	public static boolean allowEnchantedItemsInRecipeBook;
	public static boolean allowRenamedItemsInRecipeBook;
	private static void lessLimitedCraftingBook() {
		allowDamagedItemsInRecipeBook = getBoolean(config, "recipe-matcher.allow-damaged-items", false, "Allow using damaged items when searching for items");
		allowEnchantedItemsInRecipeBook = getBoolean(config, "recipe-matcher.allow-enchanted-items", false, "Allow using items with enchantments when searching for items");
		allowRenamedItemsInRecipeBook = getBoolean(config, "recipe-matcher.allow-renamed-items", false, "Allow using items with custom names (i.e., renamed in anvil) when searching for items");
	}
	// Kiterino end - Less limited recipe matcher

}
