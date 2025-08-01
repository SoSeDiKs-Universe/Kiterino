package me.sosedik.kiterino.testplugin.data;

import net.kyori.adventure.key.Key;
import org.bukkit.Registry;
import org.bukkit.potion.PotionEffectType;
import org.intellij.lang.annotations.Subst;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class TestMobEffects {

	public static final PotionEffectType EFFECT_1 = inject("effect_1");

	private static PotionEffectType inject(@Subst("sample_key") String key) {
		return Registry.EFFECT.getOrThrow(Key.key("test", key));
	}

}
