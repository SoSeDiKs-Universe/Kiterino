package me.sosedik.kiterino.util;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
// Kiterino - Injecting custom Materials
// Kiterino - Injecting custom entities
public class KiterinoEnumExtenderRuntimeException extends RuntimeException {

	public KiterinoEnumExtenderRuntimeException(String message, @Nullable Throwable cause) {
		super(message, cause);
	}

}
