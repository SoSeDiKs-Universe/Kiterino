package me.sosedik.kiterino.util;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

@NullMarked
final class KiterinoUnsafeUtil {

    private KiterinoUnsafeUtil() {
        throw new IllegalStateException("Utility class");
    }

    private static Unsafe unsafe;

    static {
        try {
            Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
            unsafeField.setAccessible(true);
            unsafe = (Unsafe) unsafeField.get(null);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    static Field getField(Class<?> clazz, String fieldName) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException e) {
            throw new KiterinoDataInjectorRuntimeException("Couldn't get field", e);
        }
    }

    static void setField(Field field, @Nullable Object value) {
        Object fieldBase = unsafe.staticFieldBase(field);
        long fieldOffset = unsafe.staticFieldOffset(field);

        unsafe.putObject(fieldBase, fieldOffset, value);
    }

    static <T> T allocateInstance(Class<T> tClass) {
        try {
            return (T) unsafe.allocateInstance(tClass);
        } catch (Exception e) {
            throw new KiterinoDataInjectorRuntimeException("Couldn't allocate new instance", e);
        }
    }

}
