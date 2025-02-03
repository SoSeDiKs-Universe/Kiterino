package me.sosedik.kiterino.util;

import net.kyori.adventure.key.Key;
import org.bukkit.Material;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

@ApiStatus.Internal
@NullMarked
interface IKiterinoBootstrapMaterialInjector {

	void injectMaterials(Class<?> materialsClass);

	Material allocateMaterial(Key key);

}
