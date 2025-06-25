package me.sosedik.kiterino.testplugin;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class TestItem extends Item {

	public TestItem(String id) {
		super(
			new Properties()
				.setId(ResourceKey.create(Registries.ITEM, ResourceLocation.parse(id)))
				.food(new FoodProperties.Builder().alwaysEdible().nutrition(3).build())
				.stacksTo(10)
		);
	}

//	@Override
//	public void modify(ItemContextBox contextBox) {
//		contextBox.setNewMaterial(Material.GOLDEN_APPLE);
//		ItemMeta meta = contextBox.getMeta();
//		meta.itemName(Component.text(boop));
//	}

}
