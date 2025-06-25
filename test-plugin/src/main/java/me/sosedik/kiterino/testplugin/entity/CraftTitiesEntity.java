package me.sosedik.kiterino.testplugin.entity;

import net.minecraft.world.entity.animal.sheep.Sheep;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftSheep;

public class CraftTitiesEntity extends CraftSheep implements TitiesEntity {

	public CraftTitiesEntity(CraftServer server, Sheep entity) {
		super(server, entity);
	}

}
