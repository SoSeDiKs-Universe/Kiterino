package me.sosedik.kiterino.registry.data;

import io.papermc.paper.registry.RegistryBuilder;
import me.sosedik.kiterino.registry.wrapper.KiterinoMobEffectBehaviourWrapper;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;

/**
 * A data-centric version-specific registry entry for the {@link PotionEffectType} type.
 */
// TODO Vanilla /effect tab completion support
@ApiStatus.Experimental
@ApiStatus.NonExtendable
@NullMarked
public interface MobEffectRegistryEntity {

	/**
	 * Provides wrapper around the effect's behaviour
	 *
	 * @return the wrapper around the effect's behaviour
	 */
	KiterinoMobEffectBehaviourWrapper wrapper();

	/**
	 * Provides the effect's category
	 *
	 * @return the effect's category
	 */
	PotionEffectType.Category category();

	/**
	 * Provides the effect's color
	 *
	 * @return the effect's color
	 */
	int color();

	/**
	 * A mutable builder for the {@link MobEffectRegistryEntity} plugins may change in applicable registry events.
	 * <p>
	 * The following values are required for each builder:
	 * <ul>
	 *     <li>{@link #category(PotionEffectType.Category)}</li>
	 *     <li>{@link #color(int)}</li>
	 * </ul>
	 */
	@ApiStatus.Experimental
	@ApiStatus.NonExtendable
	interface Builder extends MobEffectRegistryEntity, RegistryBuilder<PotionEffectType> {

		/**
		 * Configures the effect's wrapper
		 *
		 * @param wrapper the effect's wrapper
		 * @return this builder
		 */
		@Contract(value = "_ -> this", mutates = "this")
		Builder wrapper(KiterinoMobEffectBehaviourWrapper wrapper);

		/**
		 * Configures the effect's category
		 *
		 * @param category the effect's category
		 * @return this builder
		 */
		@Contract(value = "_ -> this", mutates = "this")
		Builder category(PotionEffectType.Category category);

		/**
		 * Configures the effect's color
		 *
		 * @param color the effect's color
		 * @return this builder
		 */
		@Contract(value = "_ -> this", mutates = "this")
		Builder color(int color);

	}

}
