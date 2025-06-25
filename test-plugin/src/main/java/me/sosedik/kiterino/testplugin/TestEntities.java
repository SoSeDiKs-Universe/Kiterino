package me.sosedik.kiterino.testplugin;

import net.kyori.adventure.key.Key;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.animal.sheep.Sheep;

public class TestEntities {

	public static final EntityType<Sheep> TITIES = EntityType.register(Key.key("test", "tities"), EntityType.Builder.of(Sheep::new, MobCategory.CREATURE).sized(0.1F, 0.1F).passengerAttachments(0.86875F).clientTrackingRange(10));

}
