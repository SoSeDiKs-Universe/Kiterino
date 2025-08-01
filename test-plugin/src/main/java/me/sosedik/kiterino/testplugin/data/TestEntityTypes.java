package me.sosedik.kiterino.testplugin.data;

import me.sosedik.kiterino.util.KiterinoBootstrapEntityTypeInjector;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.EntityType;
import org.intellij.lang.annotations.Subst;
import org.jspecify.annotations.NullMarked;

/**
 * Keys should match the ones in {@link TestEntities}
 */
@NullMarked
public final class TestEntityTypes {

	public static final EntityType ENTITY_1 = inject("entity_1");

	private static EntityType inject(@Subst("sample_key") String key) {
		return KiterinoBootstrapEntityTypeInjector.injectEntityType(Key.key("test", key));
	}

}
