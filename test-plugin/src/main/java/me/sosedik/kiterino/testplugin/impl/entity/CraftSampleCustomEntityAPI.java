package me.sosedik.kiterino.testplugin.impl.entity;

import net.minecraft.world.entity.animal.sheep.Sheep;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftSheep;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class CraftSampleCustomEntityAPI extends CraftSheep implements SampleCustomEntityAPI {

	public CraftSampleCustomEntityAPI(CraftServer server, Sheep entity) {
		super(server, entity);
	}

}
