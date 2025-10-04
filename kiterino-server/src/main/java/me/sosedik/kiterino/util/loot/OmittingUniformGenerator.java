package me.sosedik.kiterino.util.loot;

import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.providers.number.LootNumberProviderType;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import org.jspecify.annotations.NullMarked;

import java.util.Set;

// Kiterino - Prevent clashed stew effects
@NullMarked
public record OmittingUniformGenerator(UniformGenerator source, Set<Float> omitted) implements NumberProvider {

	public OmittingUniformGenerator(UniformGenerator source, Float... omitted) {
		this(source, Set.of(omitted));
	}

	@Override
	public float getFloat(LootContext lootContext) {
		float result;

		do {
			result = this.source.getFloat(lootContext);
		} while (this.omitted.contains(result));

		return result;
	}

	@Override
	public int getInt(LootContext lootContext) {
		int result;

		do {
			result = this.source.getInt(lootContext);
		} while (this.omitted.contains((float) result));

		return result;
	}

	@Override
	public LootNumberProviderType getType() {
		return this.source.getType();
	}

}
