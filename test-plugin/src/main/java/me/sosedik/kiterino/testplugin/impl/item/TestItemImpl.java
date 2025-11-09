package me.sosedik.kiterino.testplugin.impl.item;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class TestItemImpl extends Item {

    public TestItemImpl(Object properties) {
        super(
            ((Properties) properties)
                .food(new FoodProperties.Builder().alwaysEdible().nutrition(3).build())
                .stacksTo(10)
        );
    }

}
