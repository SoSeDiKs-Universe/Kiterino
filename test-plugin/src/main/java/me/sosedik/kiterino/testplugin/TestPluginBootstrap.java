package me.sosedik.kiterino.testplugin;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import me.sosedik.kiterino.testplugin.data.TestMaterials;
import me.sosedik.kiterino.testplugin.data.TestEntities;
import me.sosedik.kiterino.testplugin.data.TestEntityTypes;
import me.sosedik.kiterino.testplugin.impl.entity.CraftSampleCustomEntityAPI;
import me.sosedik.kiterino.testplugin.impl.entity.SampleCustomEntityAPI;
import me.sosedik.kiterino.testplugin.impl.item.TestItemImpl;
import me.sosedik.kiterino.util.KiterinoBootstrapEntityTypeInjectorImpl;
import net.kyori.adventure.text.Component;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.ai.attributes.DefaultAttributes;
import org.bukkit.craftbukkit.entity.CraftEntityTypes;
import org.jspecify.annotations.NullMarked;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.event.RegistryEvents;
import me.sosedik.kiterino.testplugin.impl.block.TestBlockImpl;
import net.kyori.adventure.key.Key;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.potion.PotionEffectType;

import static org.bukkit.craftbukkit.entity.CraftEntityTypes.createLiving;

@NullMarked
public class TestPluginBootstrap implements PluginBootstrap {

    @Override
    public void bootstrap(BootstrapContext context) {
        // Injecting custom entities
        context.injectEntityTypes(TestEntityTypes.class, TestEntities.class,
            key -> switch (key.value()) {
                case "entity_1" -> new CraftEntityTypes.EntityTypeData<>(TestEntityTypes.ENTITY_1, SampleCustomEntityAPI.class, CraftSampleCustomEntityAPI::new, createLiving(TestEntities.ENTITY_1));
                default -> throw new IllegalArgumentException();
            },
            key -> {
                switch (key.value()) {
                    case "entity_1" -> {
                        KiterinoBootstrapEntityTypeInjectorImpl.ENTITY_TYPE_REPLACEMENTS.put(TestEntities.ENTITY_1, EntityTypes.SHEEP);
                        DefaultAttributes.register(TestEntities.ENTITY_1, net.minecraft.world.entity.animal.pig.Pig.createAttributes().build());
                    }
                    default -> throw new IllegalArgumentException();
                }
            }
        );

        // Injecting custom mob/potion effects
        context.getLifecycleManager().registerEventHandler(RegistryEvents.MOB_EFFECT.compose(), event -> {
            event.registry().register(TypedKey.create(RegistryKey.MOB_EFFECT, Key.key("test", "effect_1")), b -> b
                .category(PotionEffectType.Category.BENEFICIAL)
                .color(Color.WHITE.value())
            );
        });

        // Injecting blocks (also requires block items)
        context.getLifecycleManager().registerEventHandler(RegistryEvents.BLOCK.compose(), event -> {
            event.registry().register(TypedKey.create(RegistryKey.BLOCK, Key.key("test:block_1")), b -> b
                .nmsBlock(new TestBlockImpl((BlockBehaviour.Properties) b.constructBlockProperties()))
            );
        });

        // Injecting items
        context.getLifecycleManager().registerEventHandler(RegistryEvents.ITEM.compose(), event -> {
            event.registry().register(TypedKey.create(RegistryKey.ITEM, Key.key("test:block_1")), b -> b
                .nmsItem(new BlockItem(
                    (Block) b.asBlockOrThrow(),
                    ((Item.Properties) b.constructItemProperties())
                        .stacksTo(99)
                ))
                .modifier(box -> {
                    box.setType(Material.ACACIA_LOG);
                    box.addLore(Component.text(System.currentTimeMillis()));
                })
            );

            event.registry().register(TypedKey.create(RegistryKey.ITEM, Key.key("test:item_1")), b -> b
                .nmsItem(new TestItemImpl(b.constructItemProperties()))
                .modifier(box -> box.setType(Material.STICK))
            );
            event.registry().register(TypedKey.create(RegistryKey.ITEM, Key.key("test:item_2")), b -> b
                .nmsItem(new Item(
                    ((Item.Properties) b.constructItemProperties())
                        .stacksTo(32)
                        .rarity(Rarity.EPIC)
                        .food(new FoodProperties(2, 5F, true))
                        .craftRemainder(BuiltInRegistries.ITEM.getValue(Identifier.parse("test:item_1")))
                ))
//                .setData(DataComponentTypes.MAX_STACK_SIZE, 32)
//                .setData(DataComponentTypes.FOOD, FoodProperties.food().nutrition(5).canAlwaysEat(true).build())
//                .setData(DataComponentTypes.USE_REMAINDER, UseRemainder.useRemainder(ItemStack.of(Material.matchMaterial("test:item_1"))))
                .modifier(box -> box.setType(Material.EMERALD))
            );
        });
        context.getLifecycleManager().registerEventHandler(RegistryEvents.ITEM.compose(), event -> {
            event.registry().register(TypedKey.create(RegistryKey.ITEM, Key.key("test:item_3")), b -> b
                .nmsItem(new Item(
                    ((Item.Properties) b.constructItemProperties())
                        .durability(10)
                        .repairable(ItemTags.COALS)
                        .food(new FoodProperties(0, 5F, true))
                        .craftRemainder(BuiltInRegistries.ITEM.getValue(Identifier.parse("test:item_1")))
                ))
//                .setData(DataComponentTypes.MAX_STACK_SIZE, 32)
//                .setData(DataComponentTypes.FOOD, FoodProperties.food().nutrition(5).canAlwaysEat(true).build())
//                .setData(DataComponentTypes.USE_REMAINDER, UseRemainder.useRemainder(ItemStack.of(Material.matchMaterial("test:item_1"))))
                .modifier(box -> box.setType(Material.DIAMOND))
            );
        });

        // Has to be called after injecting all items
        context.getLifecycleManager().registerEventHandler(RegistryEvents.ITEM.compose(), event -> {
            context.injectMaterials(TestMaterials.class);
        });
    }

}
