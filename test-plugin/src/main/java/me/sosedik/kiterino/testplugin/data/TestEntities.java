package me.sosedik.kiterino.testplugin.data;

import net.kyori.adventure.key.Key;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.animal.sheep.Sheep;
import org.intellij.lang.annotations.Subst;
import org.jspecify.annotations.NullMarked;

/**
 * Keys should match the ones in {@link TestEntityTypes}
 */
@NullMarked
public final class TestEntities {

	public static final EntityType<Sheep> ENTITY_1 = register("entity_1", EntityType.Builder.of(Sheep::new, MobCategory.CREATURE).sized(0.1F, 0.1F).passengerAttachments(0.86875F).clientTrackingRange(10));

	private static <T extends Entity> EntityType<T> register(@Subst("sample_key") String key, EntityType.Builder<T> builder) {
		return EntityType.register(Key.key("test", key), builder);
	}

}
