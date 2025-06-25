package me.sosedik.kiterino.testplugin;

import net.kyori.adventure.key.Key;
import org.bukkit.Material;

import static me.sosedik.kiterino.util.KiterinoBootstrapMaterialInjector.injectMaterial;

public class TestMaterials {

	public static final Material BADASS = injectMaterial(Key.key("test", "badass"));
	public static final Material BADASS2 = injectMaterial(Key.key("test", "badass2"));
	public static final Material BADASS3 = injectMaterial(Key.key("test", "badass3"));
	public static final Material BOOBA = injectMaterial(Key.key("test", "booba"));
	public static final Material BOOBAD = injectMaterial(Key.key("test", "boobad"));
	public static final Material BOOBAS = injectMaterial(Key.key("test", "boobas"));

}
