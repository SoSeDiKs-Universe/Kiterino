package me.sosedik.kiterino.testplugin;

import me.sosedik.kiterino.testplugin.data.TestMaterials;
import me.sosedik.kiterino.testplugin.data.TestMobEffects;
import me.sosedik.kiterino.testplugin.data.TestEntityTypes;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class TestPlugin extends JavaPlugin implements Listener {

    @Override
    public void onLoad() {
        getLogger().info("MobEffect Test1: " + TestMobEffects.EFFECT_1);
        getLogger().info("Item Test1: " + TestMaterials.TEST_ITEM_1);
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

}
