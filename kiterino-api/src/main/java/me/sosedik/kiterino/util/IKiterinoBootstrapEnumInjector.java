package me.sosedik.kiterino.util;

import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

@ApiStatus.Internal
@NullMarked
interface IKiterinoBootstrapEnumInjector {

	void injectEnums(Class<?> enumsClass);

	Object allocateEnum(Key key);

}
