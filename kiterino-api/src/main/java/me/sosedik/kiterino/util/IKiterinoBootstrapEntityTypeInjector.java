package me.sosedik.kiterino.util;

import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NullMarked;

import java.util.function.Consumer;
import java.util.function.Function;

@NullMarked
public interface IKiterinoBootstrapEntityTypeInjector extends IKiterinoBootstrapEnumInjector {

	void addEntityTypes(Class<?> entityTypesClass, Class<?> nmsEntityTypesClass, Function<Key, Object> entityTypeDataProvider, Consumer<Key> attributeProvider);

}
