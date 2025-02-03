package me.sosedik.kiterino.modifier.item.context.packet;

import net.minecraft.world.entity.Entity;

public class ItemPacketContextInitializer {

	private ItemPacketContextInitializer() {
		throw new IllegalStateException("Utility class");
	}

	public static void init() {
		if (EntityDataPacketContext.entityFetcher == null) EntityDataPacketContext.entityFetcher = (entity) -> {
			if (entity instanceof org.bukkit.entity.Entity bukkitEntity) return bukkitEntity;
			if (entity instanceof Entity nmsEntity) return nmsEntity.getBukkitEntity();

			return null;
		};
	}

}
