package me.sosedik.kiterino.util;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
// Kiterino - Injecting custom Materials
public class KiterinoMaterialExtenderRuntimeException extends RuntimeException {

	public KiterinoMaterialExtenderRuntimeException(String message, @Nullable Throwable cause) {
		super(message, cause);
	}

}
