package me.sosedik.kiterino.testplugin;

import net.kyori.adventure.key.Key;
import org.bukkit.entity.EntityType;

import static me.sosedik.kiterino.util.KiterinoBootstrapEntityTypeInjector.injectEntityType;

public class TestEntityTypes {

	public static final EntityType TITIES = injectEntityType(Key.key("test", "tities"));

}
