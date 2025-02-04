package me.sosedik.kiterino;

import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.List;

@NullMarked
public final class KiterinoWorldConfig {

	private final YamlConfiguration config;
	private final String worldName;
	private final World.Environment environment;

	public KiterinoWorldConfig(String worldName, World.Environment environment) {
		this.config = KiterinoConfig.config;
		this.worldName = worldName;
		this.environment = environment;

		init();
	}

	public void init() {
		log("-------- World Settings For [" + this.worldName + "] --------");
		KiterinoConfig.readConfig(KiterinoWorldConfig.class, this);
	}

	public void log(String s) {
		KiterinoConfig.log(s);
	}

	public @Nullable String getString(String path, String def, String... comments) {
		this.config.addDefault("world-settings.default." + path, def);
		if (comments.length > 0) this.config.setComments("world-settings.default." + path, List.of(comments));
		return this.config.getString("world-settings." + this.worldName + "." + path, this.config.getString("world-settings.default." + path));
	}

	public boolean getBoolean(String path, boolean def, String... comments) {
		this.config.addDefault("world-settings.default." + path, def);
		if (comments.length > 0) this.config.setComments("world-settings.default." + path, List.of(comments));
		return this.config.getBoolean("world-settings." + this.worldName + "." + path, this.config.getBoolean("world-settings.default." + path));
	}

	public double getDouble(String path, double def, String... comments) {
		this.config.addDefault("world-settings.default." + path, def);
		if (comments.length > 0) this.config.setComments("world-settings.default." + path, List.of(comments));
		return this.config.getDouble("world-settings." + this.worldName + "." + path, this.config.getDouble("world-settings.default." + path));
	}

	public int getInt(String path, int def, String... comments) {
		this.config.addDefault("world-settings.default." + path, def);
		if (comments.length > 0) this.config.setComments("world-settings.default." + path, List.of(comments));
		return this.config.getInt("world-settings." + this.worldName + "." + path, this.config.getInt("world-settings.default." + path));
	}

	// Kiterino start - Bat options
	public boolean batsIgnoreInvisiblePlayers = false; // Kiterino - Bats ignore invisible (by API) players
	private void batsSettings() {
		batsIgnoreInvisiblePlayers = getBoolean("gameplay-mechanics.bat.ignore-invisible-players", batsIgnoreInvisiblePlayers, "Make resting bats ignore players that are marked invisible by API");
	}
	// Kiterino end - Bat options

	// Kiterino start - Ice options
	public boolean iceAlwaysMeltInNether = false; // Kiterino - Always melt ice in Nether
	public boolean meltPackedIceInNether = false; // Kiterino - Melt packed ice in Nether
	private void iceBlockSettings() {
		iceAlwaysMeltInNether = getBoolean("blocks.ice.always-melt-in-nether", iceAlwaysMeltInNether, "Make ice melt in ultrawarm dimensions (e.g. Nether) regardless of melting requirements");
		meltPackedIceInNether = getBoolean("blocks.packed_ice.melt-in-nether", meltPackedIceInNether, "Ticks packed ice in ultrawarm dimensions (e.g. Nether) to allow melting it");
	}
	// Kiterino end - Ice options

}
