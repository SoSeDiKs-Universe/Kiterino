package me.sosedik.kiterino.testplugin;

import me.sosedik.kiterino.event.entity.EntityLoadsProjectileEvent;
import me.sosedik.kiterino.testplugin.data.TestMaterials;
import me.sosedik.kiterino.testplugin.data.TestMobEffects;
import me.sosedik.kiterino.testplugin.data.TestEntityTypes;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
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

    @EventHandler(priority = EventPriority.LOW)
    public void onBlockInteract(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.OFF_HAND) return;
        if (event.getClickedBlock() == null) return;

        Player player = event.getPlayer();
        if (player.hasActiveItem()) return;

        if (player.getInventory().getItemInMainHand().getType() == Material.BOW)
            player.startUsingItem(EquipmentSlot.HAND);
        else if (player.getInventory().getItemInOffHand().getType() == Material.BOW)
            player.startUsingItem(EquipmentSlot.OFF_HAND);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onAirInteract(PlayerInteractEvent event) {
        if (event.useItemInHand() == Event.Result.ALLOW) return;
        if (event.getClickedBlock() != null) return;
        if (event.getHand() == null) return;

        Player player = event.getPlayer();
        if (player.getInventory().getItemInMainHand().getType() != Material.BOW) return;

        event.setUseItemInHand(Event.Result.ALLOW);
        player.startUsingItem(event.getHand());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onLoad(EntityLoadsProjectileEvent event) {
        if (!event.getProjectile().isEmpty()) return;

        event.setProjectile(new ItemStack(Material.ARROW));
    }

    @EventHandler
    public void onBow(EntityShootBowEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        player.sendMessage("Shoot!");
    }

}
