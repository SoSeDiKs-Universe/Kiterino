package me.sosedik.kiterino.testplugin.data;

import me.sosedik.kiterino.util.KiterinoBootstrapMaterialInjector;
import net.kyori.adventure.key.Key;
import org.bukkit.Material;
import org.intellij.lang.annotations.Subst;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class TestMaterials {

	public static final Material TEST_ITEM_1 = inject("item_1");
	public static final Material TEST_ITEM_2 = inject("item_2");
	public static final Material TEST_ITEM_3 = inject("item_3");

	public static final Material TEST_BLOCK_1 = inject("block_1");

	private static Material inject(@Subst("sample_key") String key) {
		return KiterinoBootstrapMaterialInjector.injectMaterial(Key.key("test", key));
	}

}
