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

    // Kiterino start - Gameplay mechanics
    public boolean fireResistanceResetsFireTicks = false; // Kiterino - Reset fire ticks when having fire resistance
    public boolean doorOpenAIAffectsGates = false; // Kiterino - Allow AI opening fence gates
    private void gameplayMechanics() {
        fireResistanceResetsFireTicks = getBoolean("gameplay-mechanics.fire-resistance-resets-fire-ticks", fireResistanceResetsFireTicks, "Having fire resistance potion effect will reset fire ticks");
        doorOpenAIAffectsGates = getBoolean("gameplay-mechanics.door-open-ai-affects-gates", doorOpenAIAffectsGates, "AI for opening doors allows opening gates as well");
    }
    // Kiterino end - Gameplay mechanics

    // Kiterino start - Sleeping options
    public boolean dayDreaming = false;
    public boolean sleepWithNoBed = false;
    public boolean noAutoWakeUp = false;
    public boolean noWeatherSkip = false;
    private void sleepingOptions() {
        dayDreaming = getBoolean("gameplay-mechanics.sleep.daydreaming", dayDreaming, "Allow sleeping during day without weather");
        sleepWithNoBed = getBoolean("gameplay-mechanics.sleep.sleep-with-no-bed", sleepWithNoBed, "Don't wake up players automatically, even if the bed does not exist");
        noAutoWakeUp = getBoolean("gameplay-mechanics.sleep.no-auto-wake-up", noAutoWakeUp, "Disable automatic wake up after sleeping");
        noWeatherSkip = getBoolean("gameplay-mechanics.sleep.no-weather-skip", noWeatherSkip, "Disable weather skipping after sleeping");
    }
    // Kiterino end - Sleeping options

    // Kiterino start - Bat options
    public boolean batsIgnoreInvisiblePlayers = false; // Kiterino - Bats ignore invisible (by API) players
    private void batsSettings() {
        batsIgnoreInvisiblePlayers = getBoolean("entity.bat.ignore-invisible-players", batsIgnoreInvisiblePlayers, "Make resting bats ignore players that are marked invisible by API");
    }
    // Kiterino end - Bat options

    // Kiterino start - Silverfish options
    public boolean moreAnnoyingSilverfish = false;
    private void silverfishOptions() {
        moreAnnoyingSilverfish = getBoolean("entity.silverfish.more-annoying", moreAnnoyingSilverfish, "Makes silverfish produce sounds more often"); // Kiterino - Make silverfishes more annoying
    }
    // Kiterino end - Silverfish options

    // Kiterino start - Ghast options
    public boolean ghastsScreamOutsideNether = false;
    private void ghastOptions() {
        ghastsScreamOutsideNether = getBoolean("entity.ghast.scream-outside-nether", ghastsScreamOutsideNether, "Makes ghasts use scream sound when outside ultrawarm dimensions");
    }
    // Kiterino end - Ghast options

    // Kiterino start - Creeper options
    public boolean creepersStalkPlayers = false; // Kiterino - Creepers stalk players
    private void creeperOptions() {
        creepersStalkPlayers = getBoolean("entity.creeper.stalk-players", creepersStalkPlayers, "Creepers require the target to see them in order to explode");
    }
    // Kiterino end - Creeper options

    // Kiterino start - Projectile options
    public float arrowWaterInertia = 0.6F;
    public boolean applyImpalingOnWetMobs = false;
    private void projectileOptions() {
        arrowWaterInertia = (float) getDouble("entity.projectile.arrow-water-inertia", arrowWaterInertia, "The inertia applied to arrows in water");
        applyImpalingOnWetMobs = getBoolean("entity.projectile.apply-impaling-on-wet-mobs", applyImpalingOnWetMobs, "Trident's Impaling enchantment works on wet mobs");
    }
    // Kiterino end - Projectile options

    // Kiterino start - Ice options
    public boolean iceAlwaysMeltInNether = false; // Kiterino - Always melt ice in Nether
    public boolean meltPackedIceInNether = false; // Kiterino - Melt packed ice in Nether
    private void iceBlockSettings() {
        iceAlwaysMeltInNether = getBoolean("blocks.ice.always-melt-in-nether", iceAlwaysMeltInNether, "Make ice melt in ultrawarm dimensions (e.g. Nether) regardless of melting requirements");
        meltPackedIceInNether = getBoolean("blocks.packed_ice.melt-in-nether", meltPackedIceInNether, "Ticks packed ice in ultrawarm dimensions (e.g. Nether) to allow melting it");
    }
    // Kiterino end - Ice options

    // Kiterino start - Allow grass spread upon coarse dirt
    public boolean grassSpreadOnCoarseDirt = false;
    private void grassSpreadOnCoarseDirt() {
        grassSpreadOnCoarseDirt = getBoolean("blocks.grass_block.spread-on-coarse-dirt", grassSpreadOnCoarseDirt, "Make grass block spread onto coarse dirt, turning it into dirt");
    }
    // Kiterino end - Allow grass spread upon coarse dirt

    // Kiterino start - Campfire options
    public boolean campfireAffectedByGravity = false;
    public double campfireBurnOutInRainChance = 0;
    private void campfireOptions() {
        campfireAffectedByGravity = getBoolean("blocks.campfire.affected-by-gravity", campfireAffectedByGravity, "Make campfires affected by gravity (like sand/gravel)");
        campfireBurnOutInRainChance = getDouble("blocks.campfire.burn-out-in-rain-chance", campfireBurnOutInRainChance, "Chance for lit campfires to burnout during rain");
    }
    // Kiterino end - Campfire options

}
