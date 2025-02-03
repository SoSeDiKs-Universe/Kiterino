package me.sosedik.kiterino.registry.data;

import io.papermc.paper.datacomponent.DataComponentType;
import io.papermc.paper.datacomponent.PaperDataComponentType;
import io.papermc.paper.registry.PaperRegistryBuilder;
import io.papermc.paper.registry.data.util.Conversions;
import me.sosedik.kiterino.modifier.item.KiterinoItemModifier;
import me.sosedik.kiterino.modifier.item.KiterinoItemModifierImpl;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.UseCooldown;
import net.minecraft.world.level.block.ComposterBlock;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

// Kiterino - Data-driven items
@NullMarked
public class KiterinoItemRegistryEntity implements ItemRegistryEntity {

    protected Map<DataComponentType, Object> components = new HashMap<>();
    protected @Nullable Float compostChance;
    protected @Nullable Item nmsItem;
    protected @Nullable KiterinoItemModifier modifier; // Kiterino - Implement packet item faker for injected items

    protected final Conversions conversions;
    public ResourceLocation itemKey;

    public KiterinoItemRegistryEntity(
        final Conversions conversions,
        final @Nullable Item internal
    ) {
        this.conversions = conversions;
        if (internal == null) {
            return;
        }

        parseValues(internal);
    }

    @Override
    public boolean hasData(DataComponentType type) {
        return this.components.containsKey(type);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> @Nullable T getData(DataComponentType.Valued<T> type) {
        return (T) this.components.get(type);
    }

    @Override
    public @Nullable Float compostChance() {
        return this.compostChance;
    }

    @Override
    public @Nullable Object nmsItem() {
        return this.nmsItem;
    }

    @Override
    public Object constructItemProperties() {
        return new Item.Properties()
                .setId(ResourceKey.create(Registries.ITEM, this.itemKey))
                .component(DataComponents.USE_COOLDOWN, new UseCooldown(0F, Optional.of(this.itemKey))); // Kiterino - Implement packet item faker for injected items
    }

    // Kiterino start - Data-driven blocks
    @Override
    public boolean hasAttachedBlock() {
        return BuiltInRegistries.BLOCK.containsKey(ResourceKey.create(Registries.BLOCK, this.itemKey));
    }

    @Override
    public Object asBlockOrThrow() {
        return BuiltInRegistries.BLOCK.getValueOrThrow(ResourceKey.create(Registries.BLOCK, this.itemKey));
    }
    // Kiterino end - Data-driven blocks

    // Kiterino start - Implement packet item faker for injected items
    @Override
    public @Nullable KiterinoItemModifier modifier() {
        return this.modifier;
    }
    // Kiterino end - Implement packet item faker for injected items

    protected void parseValues(Item item) {
        // Does it make sense to parse?
        // DataComponentMap components = item.components();
    }

    public static final class KiterinoBuilder extends KiterinoItemRegistryEntity implements Builder,
            PaperRegistryBuilder<Item, ItemType> {

        public KiterinoBuilder(final Conversions conversions, final @Nullable Item internal) {
            super(conversions, internal);
        }

        @Override
        public <T> Builder setData(DataComponentType.Valued<T> type, @Nullable T value) {
            this.components.put(type, value);
            return this;
        }

        @Override
        public Builder setData(DataComponentType.NonValued type) {
            this.components.put(type, null);
            return this;
        }

        @Override
        public Builder compostChance(@Nullable Float levelIncreaseChance) {
            this.compostChance = levelIncreaseChance;
            return this;
        }

        @Override
        public Builder nmsItem(@Nullable Object nmsItem) {
            if (!(nmsItem instanceof Item item)) throw new IllegalArgumentException("NMS item must extend Item");
            this.nmsItem = item;
            parseValues(item);
            return this;
        }

        // Kiterino start - Implement packet item faker for injected items
        @Override
        public Builder modifier(@Nullable KiterinoItemModifier modifier) {
            this.modifier = modifier;
            return this;
        }

        @SuppressWarnings("unchecked")
        private KiterinoItemModifier buildModifier(Item nmsItem) {
            return box -> {
                if (this.modifier != null) this.modifier.modify(box);
                ItemStack item = box.getItem();
                // TODO this is dumb
                nmsItem.components().keySet().forEach(dataType -> {
                    if (dataType == DataComponents.ITEM_MODEL) return;

                    DataComponentType type = PaperDataComponentType.minecraftToBukkit(dataType);
                    if (item.isDataOverridden(type)) return;

                    if (type instanceof DataComponentType.Valued<?> valued) {
                        item.setData((DataComponentType.Valued<Object>) valued, PaperDataComponentType.convertDataComponentValue(nmsItem.components(), (PaperDataComponentType.ValuedImpl) valued));
                    } else if (type instanceof DataComponentType.NonValued nonValued) {
                        item.setData(nonValued);
                    }
                });
            };
        }
        // Kiterino end - Implement packet item faker for injected items

        @SuppressWarnings("unchecked")
        @Override
        public Item build() {
            Item item = this.nmsItem;
            if (item == null) {
                Item.Properties properties = (Item.Properties) constructItemProperties();
                this.components.forEach((type, value) -> {
                    if (type instanceof PaperDataComponentType.ValuedImpl<?, ?> valued) {
                        setData(properties, (PaperDataComponentType<Object, Object>) valued, value);
                    } else {
                        setData(properties, (PaperDataComponentType.NonValuedImpl<Object, Object>) type, null);
                    }
                });
                item = new Item(properties);
            }
            if (this.compostChance != null) ComposterBlock.add(this.compostChance, item);
            KiterinoItemModifierImpl.MODIFIER_IMPL.add(new org.bukkit.NamespacedKey(this.itemKey.getNamespace(), this.itemKey.getPath()), buildModifier(item)); // Kiterino - Implement packet item faker for injected items
            return item;
        }

        private void setData(Item.Properties properties, PaperDataComponentType<Object, Object> type, @Nullable Object value) {
            properties.component(type.getHandle(), type.getAdapter().toVanilla(value));
        }

    }

}
