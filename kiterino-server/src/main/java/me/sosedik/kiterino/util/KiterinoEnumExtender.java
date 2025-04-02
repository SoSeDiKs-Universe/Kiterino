package me.sosedik.kiterino.util;

import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NullMarked;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static me.sosedik.kiterino.util.KiterinoUnsafeUtil.getField;

@SuppressWarnings("unchecked")
@NullMarked
public abstract class KiterinoEnumExtender<T extends Enum<T>> implements IKiterinoBootstrapEnumInjector {

	protected final Field enumNameField = getField(Enum.class, "name");
	protected final Field enumOrdinalField = getField(Enum.class, "ordinal");
	protected final Class<T> clazz;
	protected final int originalEnumCount;
	protected final List<T> allocated = new ArrayList<>();

	protected KiterinoEnumExtender() {
		this.clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		this.originalEnumCount = clazz.getEnumConstants().length;
	}

	public abstract void injectEnum(T value) throws Exception;

	@Override
	public void injectEnums(Class<?> enumsClass) {
		try {
			for (Field field : enumsClass.getDeclaredFields()) {
				if (field.getType() != clazz) continue;

				int modifiers = field.getModifiers();
				if (!Modifier.isPublic(modifiers)) continue;
				if (!Modifier.isStatic(modifiers)) continue;
				if (!Modifier.isFinal(modifiers)) continue;

				T value = (T) field.get(null);
				injectEnum(value);
				allocated.add(value);
			}
		} catch (Exception e) {
			throw new KiterinoEnumExtenderRuntimeException("Couldn't inject enum in " + enumsClass, e);
		}
	}

	@SuppressWarnings({"java:S3011"})
	@Override
	public T allocateEnum(Key key) {
		try {
			T value = KiterinoUnsafeUtil.allocateInstance(clazz);

			String enumName = toEnumName(key);
			enumNameField.set(value, enumName);

			return value;
		} catch (Exception e) {
			throw new KiterinoEnumExtenderRuntimeException("Couldn't allocate enum: " + key, e);
		}
	}

	@SuppressWarnings("java:S3011")
	protected void injectAllocated() {
		try {
			if (allocated.isEmpty()) return;

			T[] newValues = (T[]) Array.newInstance(clazz, originalEnumCount + allocated.size());
			for (int i = 0; i < allocated.size(); i++) {
				T material = allocated.get(i);
				int ordinal = originalEnumCount + i;
				enumOrdinalField.set(material, ordinal);
				newValues[ordinal] = material;
			}

			System.arraycopy(clazz.getEnumConstants(), 0, newValues, 0, originalEnumCount);
			Field valuesField = clazz.getDeclaredField("$VALUES");
			KiterinoUnsafeUtil.setField(valuesField, newValues);

			Field enumConstants = Class.class.getDeclaredField("enumConstants");
			enumConstants.setAccessible(true);
			enumConstants.set(clazz, null);
			Field enumConstantDirectory = Class.class.getDeclaredField("enumConstantDirectory");
			enumConstantDirectory.setAccessible(true);
			enumConstantDirectory.set(clazz, null);
		} catch (Exception e) {
			throw new KiterinoEnumExtenderRuntimeException("Couldn't extend enum", e);
		}
	}

	private static String toEnumName(Key key) {
		return key.asString().toUpperCase(Locale.US).replace(':', '_');
	}

}
