package me.sosedik.kiterino.testplugin;

import me.sosedik.kiterino.testplugin.data.TestMaterials;
import me.sosedik.kiterino.testplugin.data.TestMobEffects;
import me.sosedik.kiterino.testplugin.data.TestEntityTypes;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class TestPlugin extends JavaPlugin implements Listener {

    @Override
    public void onLoad() {
        getLogger().info("MobEffect Test1: " + TestMobEffects.EFFECT_1);
        getLogger().info("Item Test1: " + TestMaterials.TEST_ITEM_1 + " | " + TestMaterials.TEST_ITEM_1.asItemType() + " | " + TestMaterials.TEST_ITEM_1.asItemType().asMaterial());
        getLogger().info("Entity Test1: " + TestEntityTypes.ENTITY_1);
    }

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onEvent(PlayerRespawnEvent event) {
        if (event.getRespawnReason() == PlayerRespawnEvent.RespawnReason.DEATH)
            event.setRespawnLocation(event.getPlayer().getLocation());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEvent(PlayerChatEvent event) {
        Player player = event.getPlayer();
        Pig pig = player.getWorld().spawn(player.getLocation(), Pig.class);
        pig.setRider(player);

        getServer().getScheduler().runTaskLater(this, () -> {
            World first = Bukkit.getWorlds().getFirst();
            World world = player.getWorld().equals(first) ? Bukkit.getWorlds().get(1) : first;
            pig.teleportAsync(world.getSpawnLocation());
        }, 20L);
    }

}
