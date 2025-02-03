package me.sosedik.kiterino.util;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class KiterinoDataInjectorRuntimeException extends RuntimeException {

	public KiterinoDataInjectorRuntimeException(String message) {
		super(message);
	}

	public KiterinoDataInjectorRuntimeException(String message, @Nullable Throwable cause) {
		super(message, cause);
	}

}
