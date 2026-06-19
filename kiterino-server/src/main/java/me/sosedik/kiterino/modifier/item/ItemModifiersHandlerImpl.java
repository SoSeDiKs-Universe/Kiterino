package me.sosedik.kiterino.modifier.item;

import com.mojang.datafixers.util.Pair;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.BlockItemDataProperties;
import io.papermc.paper.datacomponent.item.BundleContents;
import io.papermc.paper.datacomponent.item.ChargedProjectiles;
import io.papermc.paper.datacomponent.item.Consumable;
import io.papermc.paper.datacomponent.item.ItemContainerContents;
import io.papermc.paper.datacomponent.item.Repairable;
import io.papermc.paper.datacomponent.item.UseCooldown;
import io.papermc.paper.datacomponent.item.UseRemainder;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.set.RegistryKeySet;
import me.sosedik.kiterino.KiterinoConfig;
import me.sosedik.kiterino.inventory.InventorySlotHelper;
import me.sosedik.kiterino.modifier.item.context.ItemModifierContext;
import me.sosedik.kiterino.modifier.item.context.ItemModifierContextType;
import me.sosedik.kiterino.modifier.item.context.packet.AdvancementPacketContext;
import me.sosedik.kiterino.modifier.item.context.packet.BasePacketContext;
import me.sosedik.kiterino.modifier.item.context.packet.EntityDataPacketContext;
import me.sosedik.kiterino.modifier.item.context.packet.EntityEquipmentPacketContext;
import me.sosedik.kiterino.modifier.item.context.packet.ItemPacketContextInitializer;
import me.sosedik.kiterino.modifier.item.context.packet.MerchantOfferPacketContext;
import me.sosedik.kiterino.modifier.item.context.packet.RecipeBookPacketContext;
import me.sosedik.kiterino.modifier.item.context.packet.SlottedItemPacketContext;
import me.sosedik.kiterino.modifier.item.context.packet.UnknownEntityDataContext;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponentExactPredicate;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBundlePacket;
import net.minecraft.network.protocol.game.ClientboundContainerSetContentPacket;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.network.protocol.game.ClientboundDisguisedChatPacket;
import net.minecraft.network.protocol.game.ClientboundLevelParticlesPacket;
import net.minecraft.network.protocol.game.ClientboundMerchantOffersPacket;
import net.minecraft.network.protocol.game.ClientboundPlaceGhostRecipePacket;
import net.minecraft.network.protocol.game.ClientboundRecipeBookAddPacket;
import net.minecraft.network.protocol.game.ClientboundSetCursorItemPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.protocol.game.ClientboundSetEquipmentPacket;
import net.minecraft.network.protocol.game.ClientboundSetPlayerInventoryPacket;
import net.minecraft.network.protocol.game.ClientboundSystemChatPacket;
import net.minecraft.network.protocol.game.ClientboundUpdateAdvancementsPacket;
import net.minecraft.network.protocol.game.ClientboundUpdateRecipesPacket;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.OminousItemSpawner;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.EyeOfEnder;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.entity.projectile.hurtingprojectile.Fireball;
import net.minecraft.world.entity.projectile.hurtingprojectile.windcharge.AbstractWindCharge;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipePropertySet;
import net.minecraft.world.item.crafting.SelectableRecipe;
import net.minecraft.world.item.crafting.StonecutterRecipe;
import net.minecraft.world.item.crafting.display.FurnaceRecipeDisplay;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.RecipeDisplayEntry;
import net.minecraft.world.item.crafting.display.ShapedCraftingRecipeDisplay;
import net.minecraft.world.item.crafting.display.ShapelessCraftingRecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.item.crafting.display.SmithingRecipeDisplay;
import net.minecraft.world.item.crafting.display.StonecutterRecipeDisplay;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.block.BlockType;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.CraftEquipmentSlot;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftEntityType;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.inventory.CraftRecipe;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemType;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;

@NullMarked
public class ItemModifiersHandlerImpl extends ItemModifiersHandler {

    public static final Map<NamespacedKey, ItemModifier> modifiers = new HashMap<>();
    public static final RegistryOps<Tag> SERIALIZATION_CONTEXT = MinecraftServer.getServer().registryAccess().createSerializationContext(NbtOps.INSTANCE);

    @Override
    public void registerModifier(ItemModifier itemModifier) {
        boolean log = true;
        NamespacedKey modifierId = itemModifier.getModifierId();
        if (KiterinoConfig.itemModifiersLogMissingIds && !KiterinoConfig.itemModifiersOrder.contains(modifierId) && !modifiers.containsKey(modifierId)) {
            KiterinoConfig.log(Level.INFO, "Skipping item modifier with id: " + modifierId);
            log = false;
        }

        synchronized (modifiers) {
            if (modifiers.put(modifierId, itemModifier) == null && log)
                KiterinoConfig.log(Level.INFO, "Registering a new item modifier with id: " + modifierId);
        }
    }

    @Override
    public void unregisterModifier(ItemModifier itemModifier) {
        synchronized (modifiers) {
            modifiers.remove(itemModifier.getModifierId());
        }
    }

    @Override
    public org.bukkit.inventory.@Nullable ItemStack modifyItem(ItemContextBox contextBox) {
        boolean modified = false;

        // Used for dynamic empty lines
        int currentLoreSize = contextBox.getLoreSize();
        int priorLoreSize = currentLoreSize;
        NamespacedKey currentId = null;
        NamespacedKey priorId = null;

        modifier:
        for (NamespacedKey modifierId : KiterinoConfig.itemModifiersOrder) {
            ItemModifier modifier = modifiers.get(modifierId);
            if (modifier == null) {
                if ("empty_line".equals(modifierId.namespace())) {
                    if (priorId == null) continue;

                    String[] keys = modifierId.value().split("-");
                    if (keys.length != 4) continue;
                    if (!keys[0].equals(priorId.namespace())) continue;
                    if (!keys[1].equals(priorId.value())) continue;
                    if (!keys[2].equals(currentId.namespace())) continue;
                    if (!keys[3].equals(currentId.value())) continue;

                    contextBox.addLore(priorLoreSize, net.kyori.adventure.text.Component.empty());
                    currentLoreSize++;
                }
                continue;
            }
            if (modifier.skipAir() && contextBox.getItem().getType() == Material.AIR) continue;

            ItemModifierContext context = contextBox.getContext();
            do {
                if (modifier.skipContext(context)) continue modifier;
            } while ((context = context.getParentContext()) != null);

            ModificationResult result = modifier.modify(contextBox);
            if (result == ModificationResult.PASS) continue;
            if (result == ModificationResult.RETURN)
                return contextBox.getItem();

            modified = true;

            priorLoreSize = currentLoreSize;
            currentLoreSize = contextBox.getLoreSize();
            if (priorLoreSize != currentLoreSize) {
                priorId = currentId;
                currentId = modifierId;
            }
        }

        // Special nbt cases
        org.bukkit.inventory.ItemStack item = contextBox.getItem();
        if (!item.isEmpty()) {
            // Kiterino start - Implement packet item faker for injected items
            Player viewer = contextBox.getViewer();
            // Cooldown for custom items
            if (viewer != null && viewer.hasCooldown(contextBox.getInitialType())) {
                boolean applyCooldown = true;
                if (item.hasData(DataComponentTypes.USE_COOLDOWN)) {
                    UseCooldown useCooldown = item.getData(DataComponentTypes.USE_COOLDOWN);
                    assert useCooldown != null;
                    applyCooldown = contextBox.getInitialType().key().equals(useCooldown.cooldownGroup());
                }
                if (applyCooldown) {
                    UseCooldown useCooldown = UseCooldown.useCooldown(viewer.getCooldown(contextBox.getInitialType()) / 20F).cooldownGroup(contextBox.getInitialType().getKey()).build();
                    item.setData(DataComponentTypes.USE_COOLDOWN, useCooldown);
                    modified = true;
                }
            }
            // Kiterino end - Implement packet item faker for injected items
            // Bundles
            if (item.hasData(DataComponentTypes.BUNDLE_CONTENTS)) {
                BundleContents bundleContents = item.getData(DataComponentTypes.BUNDLE_CONTENTS);
                assert bundleContents != null;
                List<org.bukkit.inventory.ItemStack> items = new ArrayList<>(bundleContents.contents());
                for (int i = 0; i < items.size(); i++) {
                    org.bukkit.inventory.ItemStack bundleItem = items.get(i);
                    org.bukkit.inventory.ItemStack newItem = ItemModifier.modifyItem(new ItemContextBox(contextBox.getViewer(), contextBox.getLocale(), ItemModifierContext.EMPTY_NO_LORE, bundleItem.clone()));
                    if (newItem != null) {
                        items.set(i, newItem);
                        modified = true;
                    }
                }
                if (modified) {
                    item.setData(DataComponentTypes.BUNDLE_CONTENTS, BundleContents.bundleContents(items));
                }
            }
            // Containers
            if (item.hasData(DataComponentTypes.CONTAINER)) {
                ItemContainerContents containerContents = item.getData(DataComponentTypes.CONTAINER);
                assert containerContents != null;
                List<org.bukkit.inventory.@Nullable ItemStack> oldItems = containerContents.contents();
                List<org.bukkit.inventory.ItemStack> newItems = new ArrayList<>(oldItems.size());
                for (int i = 0; i < newItems.size(); i++) {
                    org.bukkit.inventory.ItemStack containerItem = newItems.get(i);
                    if (containerItem == null) containerItem = org.bukkit.inventory.ItemStack.empty();
                    org.bukkit.inventory.ItemStack newItem = ItemModifier.modifyItem(new ItemContextBox(contextBox.getViewer(), contextBox.getLocale(), ItemModifierContext.EMPTY_NO_LORE, containerItem.clone()));
                    if (newItem == null) {
                        newItems.set(i, containerItem);
                    } else {
                        newItems.set(i, newItem);
                        modified = true;
                    }
                }
                if (modified) {
                    item.setData(DataComponentTypes.CONTAINER, ItemContainerContents.containerContents(newItems));
                }
            }
            // Block storages
            if (false && item.getType().isBlock() && item.hasData(DataComponentTypes.BLOCK_DATA)) { // TODO not implemented yet
                BlockItemDataProperties blockItemDataProperties = item.getData(DataComponentTypes.BLOCK_DATA);
                BlockType blockType = item.getType().asBlockType();
                assert blockItemDataProperties != null;
                assert blockType != null;
                BlockData blockData = blockItemDataProperties.createBlockData(blockType);
                BlockState blockState = blockData.createBlockState();
                if (blockState instanceof org.bukkit.block.Container container) {
                    var inventory = container.getInventory();
                    for (int i = 0; i < inventory.getSize(); i++) {
                        org.bukkit.inventory.ItemStack containerItem = inventory.getItem(i);
                        if (containerItem == null) continue;

                        org.bukkit.inventory.ItemStack newItem = ItemModifier.modifyItem(new ItemContextBox(contextBox.getViewer(), contextBox.getLocale(), ItemModifierContext.EMPTY_NO_LORE, containerItem.clone()));
                        if (newItem != null) {
                            inventory.setItem(i, containerItem);
                            modified = true;
                        }
                    }
                    if (modified) {
                        item.setData(DataComponentTypes.BLOCK_DATA, BlockItemDataProperties.blockItemStateProperties().build()); // TODO not implemented yet
                    }
                }
            }
            if (item.getItemMeta() instanceof org.bukkit.inventory.meta.BlockStateMeta meta && meta.hasBlockState()) {
                org.bukkit.block.BlockState blockState = meta.getBlockState();
                if (blockState instanceof org.bukkit.block.Container container) {
                    var inventory = container.getInventory();
                    for (int i = 0; i < inventory.getSize(); i++) {
                        org.bukkit.inventory.ItemStack containerItem = inventory.getItem(i);
                        if (containerItem == null) continue;

                        org.bukkit.inventory.ItemStack newItem = ItemModifier.modifyItem(new ItemContextBox(contextBox.getViewer(), contextBox.getLocale(), ItemModifierContext.EMPTY_NO_LORE, containerItem.clone()));
                        if (newItem != null) {
                            inventory.setItem(i, containerItem);
                            modified = true;
                        }
                    }
                    if (modified) {
                        meta.setBlockState(blockState);
                        item.setItemMeta(meta);
                    }
                }
            }
            // Use remainder
            if (item.hasData(DataComponentTypes.USE_REMAINDER)) {
                UseRemainder useRemainder = item.getData(DataComponentTypes.USE_REMAINDER);
                assert useRemainder != null;
                org.bukkit.inventory.ItemStack newItem = ItemModifier.modifyItem(new ItemContextBox(contextBox.getViewer(), contextBox.getLocale(), ItemModifierContext.EMPTY_NO_LORE, useRemainder.transformInto()));
                if (newItem != null) {
                    modified = true;
                    item.setData(DataComponentTypes.USE_REMAINDER, UseRemainder.useRemainder(newItem));
                }
            }
            // Crossbow projectile
            if (item.hasData(DataComponentTypes.CHARGED_PROJECTILES)) {
                ChargedProjectiles data = item.getData(DataComponentTypes.CHARGED_PROJECTILES);
                assert data != null;
                List<org.bukkit.inventory.ItemStack> newProjectiles = new ArrayList<>(data.projectiles());
                for (int i = 0; i < newProjectiles.size(); i++) {
                    org.bukkit.inventory.ItemStack newProjectile = ItemModifier.modifyItem(new ItemContextBox(contextBox.getViewer(), contextBox.getLocale(), ItemModifierContext.EMPTY_NO_LORE, newProjectiles.get(i).clone()));
                    if (newProjectile == null) continue;

                    modified = true;
                    newProjectiles.set(i, newProjectile);
                }
                if (modified) {
                    item.setData(DataComponentTypes.CHARGED_PROJECTILES, ChargedProjectiles.chargedProjectiles(newProjectiles));
                }
            }
            // Repairable ingredient
            if (item.hasData(DataComponentTypes.REPAIRABLE)) {
                Repairable repairable = item.getData(DataComponentTypes.REPAIRABLE);
                assert repairable != null;

                // TODO find a better way
                RegistryKeySet<ItemType> types = repairable.types();
                for (TypedKey<ItemType> typedKey : types.values()) {
                    if ("minecraft".equals(typedKey.key().namespace())) continue;

                    item.resetData(DataComponentTypes.REPAIRABLE);
                    modified = true;
                    break;
                }
            }
            // Consumable custom potion effects
            if (item.hasData(DataComponentTypes.CONSUMABLE)) {
                Consumable consumable = item.getData(DataComponentTypes.CONSUMABLE);
                assert consumable != null;

                if (!consumable.consumeEffects().isEmpty()) {
                    // Yea... a feature! :)
                    item.setData(DataComponentTypes.CONSUMABLE, Consumable.consumable()
                        .consumeSeconds(consumable.consumeSeconds())
                        .sound(consumable.sound())
                        .animation(consumable.animation())
                        .hasConsumeParticles(consumable.hasConsumeParticles())
                        .build());
                    modified = true;
                }
            }
            // Kiterino start - Allow stackable damageable items
            if ((item.hasData(DataComponentTypes.MAX_DAMAGE) || item.hasData(DataComponentTypes.DAMAGE)) && item.hasData(DataComponentTypes.MAX_STACK_SIZE)) {
                Integer damage = item.getData(DataComponentTypes.DAMAGE);
                if (damage == null || damage <= 0) {
                    item.unsetData(DataComponentTypes.DAMAGE);
                    item.unsetData(DataComponentTypes.MAX_DAMAGE);
                } else {
                    item.unsetData(DataComponentTypes.MAX_STACK_SIZE);
                }
                modified = true;
            }
            // Kiterino end - Allow stackable damageable items
        }

        return modified ? contextBox.getItem() : null;
    }

    public static void init() {
        ItemModifier itemFaker = modifiers.get(KiterinoItemModifierImpl.MODIFIER_KEY); // Kiterino - Implement packet item faker for injected items
        modifiers.clear(); // In case of reload. Not supported, but eh
        // Kiterino start - Implement packet item faker for injected items
        if (itemFaker != null) {
            modifiers.put(itemFaker.getModifierId(), itemFaker);
        }
        // Kiterino end - Implement packet item faker for injected items
        if (ItemModifiersHandler.itemModifiersHandler == null) ItemModifiersHandler.itemModifiersHandler = new ItemModifiersHandlerImpl();
        ItemPacketContextInitializer.init();
    }

    public static Packet<?> processPacket(@Nullable CraftPlayer player, Packet<?> initialPacket) {
        if (modifiers.isEmpty()) return initialPacket;
        if (KiterinoConfig.itemModifiersOrder.isEmpty()) return initialPacket;

        return switch (initialPacket) {
            case ClientboundBundlePacket packet -> handle(player, packet);
            case ClientboundContainerSetSlotPacket packet -> handle(player, packet);
            case ClientboundContainerSetContentPacket packet -> handle(player, packet);
            case ClientboundPlaceGhostRecipePacket packet -> handle(player, packet);
            case ClientboundSetCursorItemPacket packet -> handle(player, packet);
            case ClientboundSetEquipmentPacket packet -> handle(player, packet);
            case ClientboundSetEntityDataPacket packet -> handle(player, packet);
            case ClientboundSetPlayerInventoryPacket packet -> handle(player, packet);
            case ClientboundUpdateRecipesPacket packet -> handle(player, packet);
            case ClientboundRecipeBookAddPacket packet -> handle(player, packet);
            case ClientboundUpdateAdvancementsPacket packet -> handle(player, packet);
            case ClientboundMerchantOffersPacket packet -> handle(player, packet);
            case ClientboundLevelParticlesPacket packet -> handle(player, packet);
            case ClientboundSystemChatPacket packet -> handle(player, packet);
            case ClientboundDisguisedChatPacket packet -> handle(player, packet);
            default -> initialPacket;
        };
    }

    @SuppressWarnings("unchecked")
    private static Packet<?> handle(@Nullable CraftPlayer player, ClientboundBundlePacket initialPacket) {
        List<Packet<? super ClientGamePacketListener>> packets;
        if (initialPacket.packets instanceof ArrayList list) {
            packets = list;
        } else {
            List<Packet<? super ClientGamePacketListener>> packetsCopy = new ArrayList<>();
            initialPacket.packets.forEach(packetsCopy::add);
            packets = packetsCopy;
        }

        ListIterator<Packet<? super ClientGamePacketListener>> iterator = packets.listIterator();
        while (iterator.hasNext()) {
            Packet<? super ClientGamePacketListener> packet = iterator.next();
            Packet<? super ClientGamePacketListener> newPacket = (Packet<? super ClientGamePacketListener>) processPacket(player, packet);
            if (packet != newPacket) {
                iterator.set(newPacket);
            }
        }

        initialPacket.packets = packets;

        return initialPacket;
    }

    private static Packet<?> handle(@Nullable CraftPlayer player, ClientboundContainerSetSlotPacket packet) {
        ItemStack original = packet.getItem();
        var context = new SlottedItemPacketContext(ItemModifierContextType.SET_SLOT, null, packet, packet.getSlot());
        var contextBox = new ItemContextBox(player, context, original.asBukkitCopy());
        ItemStack result = fromBukkit(contextBox, original);
        if (result != null) packet.itemStack = result;
        return packet;
    }

    private static Packet<?> handle(@Nullable CraftPlayer player, ClientboundContainerSetContentPacket packet) {
        boolean modified = false;
        List<ItemStack> items = packet.items();
        if (!(items instanceof ArrayList<ItemStack>))
            items = new ArrayList<>(items);
        for (int i = 0; i < items.size(); i++) {
            ItemStack original = items.get(i);
            var context = new SlottedItemPacketContext(ItemModifierContextType.WINDOW_ITEMS, null, packet, i);
            var contextBox = new ItemContextBox(player, context, original.asBukkitCopy());
            ItemStack result = fromBukkit(contextBox, original);
            if (result == null) continue;

            items.set(i, result);
            modified = true;
        }

        ItemStack carriedOriginal = packet.carriedItem();
        var context = new SlottedItemPacketContext(ItemModifierContextType.WINDOW_ITEMS, null, packet, InventorySlotHelper.CURSOR);
        var contextBox = new ItemContextBox(player, context, carriedOriginal.asBukkitCopy());
        ItemStack carriedResult = fromBukkit(contextBox, carriedOriginal);
        if (carriedResult != null) modified = true;

        return modified ? new ClientboundContainerSetContentPacket(packet.containerId(), packet.stateId(), items, carriedResult == null ? carriedOriginal : carriedResult) : packet;
    }

    private static Packet<?> handle(@Nullable CraftPlayer player, ClientboundPlaceGhostRecipePacket packet) {
        RecipeDisplay display = packet.recipeDisplay();
        RecipeDisplay newDisplay = replaceRecipeDisplay(packet, ItemModifierContextType.RECIPE_GHOST, player, display); // TODO recipe id :F
        return display == newDisplay ? packet : new ClientboundPlaceGhostRecipePacket(packet.containerId(), newDisplay);
    }

    private static Packet<?> handle(@Nullable CraftPlayer player, ClientboundSetCursorItemPacket packet) {
        ItemStack original = packet.contents();
        var context = new SlottedItemPacketContext(ItemModifierContextType.SET_SLOT, null, packet, InventorySlotHelper.CURSOR);
        var contextBox = new ItemContextBox(player, context, original.asBukkitCopy());
        ItemStack result = fromBukkit(contextBox, original);
        return result == null ? packet : new ClientboundSetCursorItemPacket(result);
    }

    private static Packet<?> handle(@Nullable CraftPlayer player, ClientboundSetEquipmentPacket packet) {
        List<Pair<EquipmentSlot, ItemStack>> slots = packet.getSlots();
        int entityId = packet.getEntity();
        org.bukkit.entity.Entity entity = null;
        World world = player == null ? null : player.getWorld();
        for (int i = 0; i < slots.size(); i++) {
            Pair<EquipmentSlot, ItemStack> slot = slots.get(i);
            ItemStack original = slot.getSecond();
            org.bukkit.entity.Entity finalEntity = entity;
            var context = new EntityEquipmentPacketContext(ItemModifierContextType.ENTITY_EQUIPMENT, null, packet, world, entityId, () -> {
                if (finalEntity == null && world != null) {
                    Entity lookup = ((CraftWorld) world).getHandle().moonrise$getEntityLookup().get(entityId);
                    return lookup == null ? null : lookup.getBukkitEntity();
                }
                return finalEntity;
            }, CraftEquipmentSlot.getSlot(slot.getFirst()));
            var contextBox = new ItemContextBox(player, context, original.asBukkitCopy());
            ItemStack result = fromBukkit(contextBox, original);
            if (result != null) slots.set(i, Pair.of(slot.getFirst(), result));
            if (entity == null && context.isFetched()) entity = context.getEntity();
        }
        return packet;
    }

    // As much as I dislike this, not sure if there's a better way
    private static Packet<?> handle(@Nullable CraftPlayer player, ClientboundSetEntityDataPacket packet) {
        int entityId = packet.id();
        Entity entity = null;
        if (player == null) {
            for (World world : Bukkit.getWorlds()) {
                entity = ((CraftWorld) world).getHandle().moonrise$getEntityLookup().get(entityId);
                if (entity != null) {
                    break;
                }
            }
        } else {
            entity = ((CraftWorld) player.getWorld()).getHandle().moonrise$getEntityLookup().get(entityId);
        }
        if (entity == null) {
            List<SynchedEntityData.DataValue<?>> dataValues = packet.packedItems();
            for (int i = 0; i < dataValues.size(); i++) {
                SynchedEntityData.DataValue<?> dataValue = dataValues.get(i);
                if (dataValue.serializer() != EntityDataSerializers.ITEM_STACK) continue;

                ItemStack original = (ItemStack) dataValue.value();
                var contextType = ItemModifierContextType.UNKNOWN_ENTITY_DATA;
                var context = new UnknownEntityDataContext(contextType, null, packet, player == null ? null : player.getWorld(), entityId);
                var contextBox = new ItemContextBox(player, context, original.asBukkitCopy());
                ItemStack result = fromBukkit(contextBox, original);
                if (result != null) dataValues.set(i, new SynchedEntityData.DataValue<>(dataValue.id(), EntityDataSerializers.ITEM_STACK, result));
                break;
            }
            return packet;
        }

        List<SynchedEntityData.DataValue<?>> dataValues = packet.packedItems();
        ItemModifierContext context;
        ItemStack original;
        switch (entity) {
            case ItemEntity droppedItem -> {
                context = new EntityDataPacketContext(ItemModifierContextType.ENTITY_DATA, null, packet, entity.level().getWorld(), entityId, CraftEntityType.minecraftToBukkit(entity.getType()), droppedItem);
                original = droppedItem.getItem();
            }
            case ItemFrame itemFrame -> {
                context = new EntityDataPacketContext(ItemModifierContextType.ENTITY_DATA, null, packet, entity.level().getWorld(), entityId, CraftEntityType.minecraftToBukkit(entity.getType()), itemFrame);
                original = itemFrame.getItem();
            }
            case ThrowableItemProjectile throwableProjectile -> {
                context = new EntityDataPacketContext(ItemModifierContextType.ENTITY_DATA, null, packet, entity.level().getWorld(), entityId, CraftEntityType.minecraftToBukkit(entity.getType()), throwableProjectile);
                original = throwableProjectile.getItem();
            }
            case EyeOfEnder eyeOfEnder -> {
                context = new EntityDataPacketContext(ItemModifierContextType.ENTITY_DATA, null, packet, entity.level().getWorld(), entityId, CraftEntityType.minecraftToBukkit(entity.getType()), eyeOfEnder);
                original = eyeOfEnder.getItem();
            }
            case FireworkRocketEntity firework -> {
                context = new EntityDataPacketContext(ItemModifierContextType.ENTITY_DATA, null, packet, entity.level().getWorld(), entityId, CraftEntityType.minecraftToBukkit(entity.getType()), firework);
                original = firework.getItem();
            }
            case Fireball fireball -> {
                context = new EntityDataPacketContext(ItemModifierContextType.ENTITY_DATA, null, packet, entity.level().getWorld(), entityId, CraftEntityType.minecraftToBukkit(entity.getType()), fireball);
                original = fireball.getItem();
            }
            case AbstractWindCharge windCharge -> {
                context = new EntityDataPacketContext(ItemModifierContextType.ENTITY_DATA, null, packet, entity.level().getWorld(), entityId, CraftEntityType.minecraftToBukkit(entity.getType()), windCharge);
                original = windCharge.getItem();
            }
            case Display.ItemDisplay itemDisplay -> {
                context = new EntityDataPacketContext(ItemModifierContextType.ENTITY_DATA, null, packet, entity.level().getWorld(), entityId, CraftEntityType.minecraftToBukkit(entity.getType()), itemDisplay);
                original = itemDisplay.getItemStack();
            }
            case OminousItemSpawner ominousItemSpawner -> {
                context = new EntityDataPacketContext(ItemModifierContextType.ENTITY_DATA, null, packet, entity.level().getWorld(), entityId, CraftEntityType.minecraftToBukkit(entity.getType()), ominousItemSpawner);
                original = ominousItemSpawner.getItem();
            }
            default -> {
                return packet;
            }
        }
        var contextBox = new ItemContextBox(player, context, original.asBukkitCopy());
        for (int i = 0; i < dataValues.size(); i++) {
            SynchedEntityData.DataValue<?> dataValue = dataValues.get(i);
            if (dataValue.serializer() != EntityDataSerializers.ITEM_STACK) continue;

            ItemStack result = fromBukkit(contextBox, original);
            if (result != null) dataValues.set(i, new SynchedEntityData.DataValue<>(dataValue.id(), EntityDataSerializers.ITEM_STACK, result));
            break;
        }
        return packet;
    }

    private static Packet<?> handle(@Nullable CraftPlayer player, ClientboundSetPlayerInventoryPacket packet) {
        ItemStack original = packet.contents();
        int slot = packet.slot();
        var context = new SlottedItemPacketContext(ItemModifierContextType.SET_SLOT, null, packet, slot);
        var contextBox = new ItemContextBox(player, context, original.asBukkitCopy());
        ItemStack result = fromBukkit(contextBox, original);
        return result == null ? packet : new ClientboundSetPlayerInventoryPacket(slot, result);
    }

    // Yes, this also sucks!
    private static Packet<?> handle(@Nullable CraftPlayer player, ClientboundUpdateRecipesPacket packet) {
        Map<ResourceKey<RecipePropertySet>, RecipePropertySet> resourceKeyRecipePropertySetMap = new HashMap<>(packet.itemSets());
        resourceKeyRecipePropertySetMap.replaceAll((key, set) -> {
            boolean[] modified = {false};
            List<Holder<Item>> items = new ArrayList<>(set.items);
            items.replaceAll(holder -> {
                RecipeBookPacketContext.DisplayType displayType;
                if (key == RecipePropertySet.SMITHING_BASE)
                    displayType = RecipeBookPacketContext.DisplayType.SMITHING_BASE;
                else if (key == RecipePropertySet.SMITHING_TEMPLATE)
                    displayType = RecipeBookPacketContext.DisplayType.SMITHING_TEMPLATE;
                else if (key == RecipePropertySet.SMITHING_ADDITION)
                    displayType = RecipeBookPacketContext.DisplayType.SMITHING_ADDITION;
                else
                    displayType = RecipeBookPacketContext.DisplayType.INGREDIENT;

                var ingredientContext = new RecipeBookPacketContext(ItemModifierContextType.RECIPE_BOOK, null, packet, displayType);

                ItemStack replacement = replaceItem(player, ingredientContext, new ItemStack(holder.value()));
                if (replacement != null) modified[0] = true;
                return replacement == null ? holder : replacement.typeHolder();
            });
            return modified[0] ? new RecipePropertySet(new HashSet<>(items)) : set;
        });

        List<SelectableRecipe.SingleInputEntry<StonecutterRecipe>> stonecutterRecipes = new ArrayList<>();
        for (SelectableRecipe.SingleInputEntry<StonecutterRecipe> stonecutterRecipeEntry : packet.stonecutterRecipes().entries()) {
            Optional<RecipeHolder<StonecutterRecipe>> recipeHolder = stonecutterRecipeEntry.recipe().recipe();
            if (recipeHolder.isEmpty()) {
                stonecutterRecipes.add(stonecutterRecipeEntry);
                continue;
            }

            var ingredientContext = new RecipeBookPacketContext(ItemModifierContextType.RECIPE_BOOK, null, packet, RecipeBookPacketContext.DisplayType.INGREDIENT);
            var resultContext = new RecipeBookPacketContext(ItemModifierContextType.RECIPE_BOOK, null, packet, RecipeBookPacketContext.DisplayType.RESULT);

            RecipeHolder<StonecutterRecipe> recipe = recipeHolder.get();
            StonecutterRecipe nmsRecipe = recipe.value();
            Ingredient ingredient = replaceIngredient(player, ingredientContext, stonecutterRecipeEntry.input(), false);

            ItemStackTemplate originalTemplate = nmsRecipe.result;
            ItemStack original = originalTemplate.create();
            ItemStack result = replaceItem(player, resultContext, original);
            ItemStackTemplate resultTemplate = result == null ? originalTemplate : ItemStackTemplate.fromNonEmptyStack(result);

            nmsRecipe = new StonecutterRecipe(new Recipe.CommonInfo(nmsRecipe.showNotification()), ingredient, resultTemplate);
            stonecutterRecipes.add(new SelectableRecipe.SingleInputEntry<>(ingredient, new SelectableRecipe<>(new SlotDisplay.ItemStackSlotDisplay(resultTemplate), Optional.of(new RecipeHolder<>(recipe.id(), nmsRecipe)))));
        }

        return new ClientboundUpdateRecipesPacket(resourceKeyRecipePropertySetMap, new SelectableRecipe.SingleInputSet<>(stonecutterRecipes));
    }

    // This one is a real sucker (and I thought the entity data was bad :')
    // Changing items inside the recipes is a no-go and there's lack of context upon
    // writing to the buffer, so we have to recreate the recipes in there...
    private static Packet<?> handle(@Nullable CraftPlayer player, ClientboundRecipeBookAddPacket packet) {
        List<ClientboundRecipeBookAddPacket.Entry> entries = new ArrayList<>(packet.entries());
        for (int r = 0; r < entries.size(); r++) {
            ClientboundRecipeBookAddPacket.Entry entry = entries.get(r);
            RecipeDisplayEntry contents = entry.contents();
            RecipeDisplay display = contents.display();
            RecipeDisplay newDisplay = replaceRecipeDisplay(packet, ItemModifierContextType.RECIPE_BOOK, player, display);

            boolean modifiedRequirements = false;
            Optional<List<Ingredient>> craftingRequirements = contents.craftingRequirements();
            if (craftingRequirements.isPresent()) {
                List<Ingredient> ingredients = new ArrayList<>(craftingRequirements.get());
                var ingredientContext = new RecipeBookPacketContext(ItemModifierContextType.RECIPE_BOOK, null, packet, RecipeBookPacketContext.DisplayType.INGREDIENT);
                for (int i = 0; i < ingredients.size(); i++) {
                    Ingredient ingredient = ingredients.get(i);
                    Ingredient newIngredient = replaceIngredient(player, ingredientContext, ingredient, true);
                    if (ingredient != newIngredient) {
                        ingredients.set(i, newIngredient);
                        modifiedRequirements = true;
                    }
                }
                if (modifiedRequirements)
                    craftingRequirements = Optional.of(ingredients);
            }

            if (display != newDisplay || modifiedRequirements) {
                entries.set(r, new ClientboundRecipeBookAddPacket.Entry(
                    new RecipeDisplayEntry(
                        contents.id(),
                        newDisplay,
                        contents.group(),
                        contents.category(),
                        craftingRequirements
                    ),
                    entry.flags()
                ));
            }
        }

        return new ClientboundRecipeBookAddPacket(entries, packet.replace());
    }

    private static RecipeDisplay replaceRecipeDisplay(Packet<?> packet, ItemModifierContextType contextType, @Nullable CraftPlayer player, RecipeDisplay display) {
        return switch (display) {
            case FurnaceRecipeDisplay furnaceRecipeDisplay -> {
                SlotDisplay ingredient = replaceSlotDisplay(player, contextType, new RecipeBookPacketContext(contextType, null, packet, RecipeBookPacketContext.DisplayType.INGREDIENT), furnaceRecipeDisplay.ingredient());
                SlotDisplay fuel = replaceSlotDisplay(player, contextType, new RecipeBookPacketContext(contextType, null, packet, RecipeBookPacketContext.DisplayType.FUEL), furnaceRecipeDisplay.fuel());
                SlotDisplay result = replaceSlotDisplay(player, contextType, new RecipeBookPacketContext(contextType, null, packet, RecipeBookPacketContext.DisplayType.RESULT), furnaceRecipeDisplay.result());
                SlotDisplay craftingStation = replaceSlotDisplay(player, contextType, new RecipeBookPacketContext(contextType, null, packet, RecipeBookPacketContext.DisplayType.CRAFTING_STATION), furnaceRecipeDisplay.craftingStation());
                yield new FurnaceRecipeDisplay(ingredient, fuel, result, craftingStation, furnaceRecipeDisplay.duration(), furnaceRecipeDisplay.experience());
            }
            case ShapedCraftingRecipeDisplay shapedCraftingRecipeDisplay -> {
                List<SlotDisplay> ingredients = new ArrayList<>(shapedCraftingRecipeDisplay.ingredients());
                ingredients.replaceAll(slotDisplay -> replaceSlotDisplay(player, contextType, new RecipeBookPacketContext(contextType, null, packet, RecipeBookPacketContext.DisplayType.INGREDIENT), slotDisplay));
                SlotDisplay result = replaceSlotDisplay(player, contextType, new RecipeBookPacketContext(contextType, null, packet, RecipeBookPacketContext.DisplayType.RESULT), shapedCraftingRecipeDisplay.result());
                SlotDisplay craftingStation = replaceSlotDisplay(player, contextType, new RecipeBookPacketContext(contextType, null, packet, RecipeBookPacketContext.DisplayType.CRAFTING_STATION), shapedCraftingRecipeDisplay.craftingStation());
                yield new ShapedCraftingRecipeDisplay(shapedCraftingRecipeDisplay.width(), shapedCraftingRecipeDisplay.height(), ingredients, result, craftingStation);
            }
            case ShapelessCraftingRecipeDisplay shapelessCraftingRecipeDisplay -> {
                List<SlotDisplay> ingredients = new ArrayList<>(shapelessCraftingRecipeDisplay.ingredients());
                ingredients.replaceAll(slotDisplay -> replaceSlotDisplay(player, contextType, new RecipeBookPacketContext(contextType, null, packet, RecipeBookPacketContext.DisplayType.INGREDIENT), slotDisplay));
                SlotDisplay result = replaceSlotDisplay(player, contextType, new RecipeBookPacketContext(contextType, null, packet, RecipeBookPacketContext.DisplayType.RESULT), shapelessCraftingRecipeDisplay.result());
                SlotDisplay craftingStation = replaceSlotDisplay(player, contextType, new RecipeBookPacketContext(contextType, null, packet, RecipeBookPacketContext.DisplayType.CRAFTING_STATION), shapelessCraftingRecipeDisplay.craftingStation());
                yield new ShapelessCraftingRecipeDisplay(ingredients, result, craftingStation);
            }
            case SmithingRecipeDisplay smithingRecipeDisplay -> {
                SlotDisplay template = replaceSlotDisplay(player, contextType, new RecipeBookPacketContext(contextType, null, packet, RecipeBookPacketContext.DisplayType.SMITHING_TEMPLATE), smithingRecipeDisplay.template());
                SlotDisplay base = replaceSlotDisplay(player, contextType, new RecipeBookPacketContext(contextType, null, packet, RecipeBookPacketContext.DisplayType.SMITHING_BASE), smithingRecipeDisplay.base());
                SlotDisplay addition = replaceSlotDisplay(player, contextType, new RecipeBookPacketContext(contextType, null, packet, RecipeBookPacketContext.DisplayType.SMITHING_ADDITION), smithingRecipeDisplay.addition());
                SlotDisplay result = replaceSlotDisplay(player, contextType, new RecipeBookPacketContext(contextType, null, packet, RecipeBookPacketContext.DisplayType.RESULT), smithingRecipeDisplay.result());
                SlotDisplay craftingStation = replaceSlotDisplay(player, contextType, new RecipeBookPacketContext(contextType, null, packet, RecipeBookPacketContext.DisplayType.CRAFTING_STATION), smithingRecipeDisplay.craftingStation());
                yield new SmithingRecipeDisplay(template, base, addition, result, craftingStation);
            }
            case StonecutterRecipeDisplay stonecutterRecipeDisplay -> {
                SlotDisplay input = replaceSlotDisplay(player, contextType, new RecipeBookPacketContext(contextType, null, packet, RecipeBookPacketContext.DisplayType.INGREDIENT), stonecutterRecipeDisplay.input());
                SlotDisplay result = replaceSlotDisplay(player, contextType, new RecipeBookPacketContext(contextType, null, packet, RecipeBookPacketContext.DisplayType.RESULT), stonecutterRecipeDisplay.result());
                SlotDisplay craftingStation = replaceSlotDisplay(player, contextType, new RecipeBookPacketContext(contextType, null, packet, RecipeBookPacketContext.DisplayType.CRAFTING_STATION), stonecutterRecipeDisplay.craftingStation());
                yield new StonecutterRecipeDisplay(input, result, craftingStation);
            }
            default -> display;
        };
    }

    private static SlotDisplay replaceSlotDisplay(@Nullable CraftPlayer player, ItemModifierContextType contextType, RecipeBookPacketContext context, SlotDisplay slotDisplay) {
        return switch (slotDisplay) {
            case SlotDisplay.Composite composite -> {
                List<SlotDisplay> displays = new ArrayList<>(composite.contents());
                displays.replaceAll(display -> replaceSlotDisplay(player, contextType, context, display));
                yield new SlotDisplay.Composite(displays);
            }
            case SlotDisplay.ItemSlotDisplay itemSlotDisplay -> {
                ItemStack replacement = replaceItem(player, context, itemSlotDisplay.item().value().getDefaultInstance());
                yield replacement == null ? itemSlotDisplay : new SlotDisplay.ItemStackSlotDisplay(ItemStackTemplate.fromNonEmptyStack(replacement));
            }
            case SlotDisplay.ItemStackSlotDisplay itemStackSlotDisplay -> {
                ItemStackTemplate stackTemplate = itemStackSlotDisplay.stack();
                ItemStack replacement = replaceItem(player, context, stackTemplate.create());
                yield replacement == null ? itemStackSlotDisplay : new SlotDisplay.ItemStackSlotDisplay(ItemStackTemplate.fromNonEmptyStack(replacement));
            }
            case SlotDisplay.SmithingTrimDemoSlotDisplay smithingTrimDemoSlotDisplay -> {
                SlotDisplay base = replaceSlotDisplay(player, contextType, context, smithingTrimDemoSlotDisplay.base());
                SlotDisplay material = replaceSlotDisplay(player, contextType, context, smithingTrimDemoSlotDisplay.material());
                if (base == smithingTrimDemoSlotDisplay.base() && material == smithingTrimDemoSlotDisplay.material())
                    yield smithingTrimDemoSlotDisplay;
                yield new SlotDisplay.SmithingTrimDemoSlotDisplay(base, material, smithingTrimDemoSlotDisplay.pattern());
            }
            case SlotDisplay.WithRemainder withRemainder -> {
                SlotDisplay input = replaceSlotDisplay(player, contextType, context, withRemainder.input());
                SlotDisplay remainder = replaceSlotDisplay(player, contextType, context, withRemainder.remainder());
                if (input == withRemainder.input() && remainder == withRemainder.remainder())
                    yield withRemainder;
                yield new SlotDisplay.WithRemainder(input, remainder);
            }
            default -> slotDisplay;
        };
    }

    private static Ingredient replaceIngredient(@Nullable CraftPlayer player, RecipeBookPacketContext context, Ingredient ingredient, boolean fakeRecipeBook) {
        final boolean[] modified = {false};

        Ingredient superDirtyCopy = CraftRecipe.toIngredient(CraftRecipe.toChoice(ingredient), false);

        List<Holder<Item>> values = superDirtyCopy.values.stream().toList();
        List<Holder<Item>> valuesCleaned = values.stream()
            .map(holder -> {
                if (fakeRecipeBook && KiterinoConfig.disableCraftableRecipes) {
                    modified[0] = true;
                    return net.minecraft.world.item.Items.DEBUG_STICK.builtInRegistryHolder();
                }
                if (holder.unwrapKey().map(itemResourceKey -> "minecraft".equals(itemResourceKey.identifier().getNamespace())).orElse(true)) {
                    return holder;
                }

                Item item = holder.value();
                org.bukkit.inventory.ItemStack parsed = ItemModifier.modifyItem(player, player == null ? Locale.US : player.locale(), new ItemStack(item).asBukkitMirror());
                if (parsed == null) {
                    return holder;
                }

                modified[0] = true;
                return Holder.direct(ItemStack.fromBukkitCopy(parsed).getItem());
            })
            .toList();

        if (modified[0]) {
            superDirtyCopy.values = HolderSet.direct(valuesCleaned.stream().map(item -> item.value().builtInRegistryHolder()).toList());
        }

        Set<ItemStack> items = superDirtyCopy.itemStacks();
        if (items == null) {
            return modified[0] ? superDirtyCopy : ingredient;
        }

        boolean modifiedItems = false;
        Set<ItemStack> updatedItems = new HashSet<>();
        for (ItemStack item : items) {
            ItemStack result = replaceItem(player, context, item);
            updatedItems.add(result == null ? item : result);
            if (result != null) modifiedItems = true;
        }
        if (!modifiedItems) {
            return modified[0] ? superDirtyCopy : ingredient;
        }

        superDirtyCopy.itemStacks = updatedItems;
        return superDirtyCopy;
    }

    private static @Nullable ItemStack replaceItem(@Nullable CraftPlayer player, RecipeBookPacketContext context, ItemStack original) {
        var contextBox = new ItemContextBox(player, context, original.asBukkitCopy());
        return fromBukkit(contextBox, original);
    }

    // I may have sinned, twice
    private static Packet<?> handle(@Nullable CraftPlayer player, ClientboundUpdateAdvancementsPacket packet) {
        List<AdvancementHolder> advancements = new ArrayList<>(packet.getAdded());
        for (int i = 0; i < advancements.size(); i++) {
            AdvancementHolder oldHolder = advancements.get(i);
            Advancement oldAdvancement = oldHolder.value();
            DisplayInfo oldDisplay = oldAdvancement.display().orElse(null);
            if (oldDisplay == null) continue;

            var namespacedKey = new NamespacedKey(oldHolder.id().getNamespace(), oldHolder.id().getPath());
            var context = new AdvancementPacketContext(ItemModifierContextType.ADVANCEMENT, null, packet, namespacedKey, oldHolder::toBukkit);
            ItemStackTemplate originalTemplate = oldDisplay.getIcon();
            ItemStack original = originalTemplate.create();
            var contextBox = new ItemContextBox(player, context, original.asBukkitCopy());
            ItemStack result = fromBukkit(contextBox, original);
            if (result == null) continue;

            var displayInfo = new DisplayInfo(
                ItemStackTemplate.fromNonEmptyStack(result),
                oldDisplay.getTitle(),
                oldDisplay.getDescription(),
                oldDisplay.getBackground(),
                oldDisplay.getType(),
                oldDisplay.shouldShowToast(),
                oldDisplay.shouldAnnounceChat(),
                oldDisplay.isHidden()
            );
            displayInfo.setLocation(oldDisplay.getX(), oldDisplay.getY());
            var advancement = new Advancement(
                oldAdvancement.parent(),
                Optional.of(displayInfo),
                oldAdvancement.rewards(),
                oldAdvancement.criteria(),
                oldAdvancement.requirements(),
                oldAdvancement.sendsTelemetryEvent()
            );
            var holder = new AdvancementHolder(oldHolder.id(), advancement);
            advancements.set(i, holder);
        }
        packet.added = advancements;
        return packet;
    }

    // ..thrice?!
    private static Packet<?> handle(@Nullable CraftPlayer player, ClientboundMerchantOffersPacket packet) {
        MerchantOffers newOffers = new MerchantOffers();
        MerchantOffers oldOffers = packet.getOffers();
        for (MerchantOffer merchantOffer : oldOffers) {
            boolean modified = false;
            ItemCost itemCost = merchantOffer.getItemCostA();
            ItemStack original = itemCost.itemStack();
            var context = new MerchantOfferPacketContext(ItemModifierContextType.MERCHANT_OFFER, null, packet, merchantOffer.asBukkit(), MerchantOfferPacketContext.Slot.FIRST);
            var contextBox = new ItemContextBox(player, context, original.asBukkitCopy());
            ItemStack result = fromBukkit(contextBox, original);
            if (result != null) {
                modified = true;
                merchantOffer = merchantOffer.copy();
                merchantOffer.baseCostA = new ItemCost(result.typeHolder(), itemCost.count(), DataComponentExactPredicate.allOf(result.getComponents()), result);
            }

            itemCost = merchantOffer.getItemCostB().orElse(null);
            if (itemCost != null) {
                original = itemCost.itemStack();
                context = new MerchantOfferPacketContext(ItemModifierContextType.MERCHANT_OFFER, null, packet, merchantOffer.asBukkit(), MerchantOfferPacketContext.Slot.SECOND);
                contextBox = new ItemContextBox(player, context, original.asBukkitCopy());
                result = fromBukkit(contextBox, original);
                if (result != null) {
                    if (!modified) {
                        modified = true;
                        merchantOffer = merchantOffer.copy();
                    }
                    merchantOffer.costB = Optional.of(new ItemCost(result.typeHolder(), itemCost.count(), DataComponentExactPredicate.allOf(result.getComponents()), result));
                }
            }

            original = merchantOffer.getResult();
            context = new MerchantOfferPacketContext(ItemModifierContextType.MERCHANT_OFFER, null, packet, merchantOffer.asBukkit(), MerchantOfferPacketContext.Slot.RESULT);
            contextBox = new ItemContextBox(player, context, original.asBukkitCopy());
            result = fromBukkit(contextBox, original);
            if (result != null) {
                if (!modified) {
                    merchantOffer = merchantOffer.copy();
                }
                merchantOffer.result = result;
            }
            newOffers.add(merchantOffer);
        }
        packet.offers = newOffers;
        return packet;
    }

    private static Packet<?> handle(@Nullable CraftPlayer player, ClientboundLevelParticlesPacket initialPacket) {
        if (!(initialPacket.getParticle() instanceof ItemParticleOption particleOption)) return initialPacket;

        ItemStackTemplate originalTemplate = particleOption.itemStack;
        ItemStack original = originalTemplate.create();
        var contextBox = new ItemContextBox(player, new BasePacketContext(ItemModifierContextType.PARTICLE, null, initialPacket), original.asBukkitCopy());
        ItemStack result = fromBukkit(contextBox, original);
        if (result != null) {
            particleOption.itemStack = ItemStackTemplate.fromNonEmptyStack(result);
        }
        return initialPacket;
    }

    // Kiterino start - Parse hover events
    private static Packet<?> handle(@Nullable CraftPlayer player, ClientboundSystemChatPacket initialPacket) {
        if (KiterinoConfig.parseItemHoversEverywhere) {
            return initialPacket;
        }

        if (initialPacket.overlay()) { // Action bar
            return initialPacket;
        }

        boolean prev = ComponentSerialization.DONT_RENDER_TRANSLATABLES.get();
        ComponentSerialization.DONT_RENDER_TRANSLATABLES.set(true);
        Component component = initialPacket.content();
        component = ComponentSerialization.replaceHoverEvent(player,player == null ? Locale.US : player.locale(), component);
        ComponentSerialization.DONT_RENDER_TRANSLATABLES.set(prev);
        return new ClientboundSystemChatPacket(component, false);
    }

    private static Packet<?> handle(@Nullable CraftPlayer player, ClientboundDisguisedChatPacket initialPacket) {
        if (KiterinoConfig.parseItemHoversEverywhere) {
            return initialPacket;
        }

        boolean prev = ComponentSerialization.DONT_RENDER_TRANSLATABLES.get();
        ComponentSerialization.DONT_RENDER_TRANSLATABLES.set(true);
        Component component = initialPacket.message();
        component = ComponentSerialization.replaceHoverEvent(player, player == null ? Locale.US : player.locale(), component);
        ComponentSerialization.DONT_RENDER_TRANSLATABLES.set(prev);
        return new ClientboundDisguisedChatPacket(component, initialPacket.chatType());
    }
    // Kiterino end - Parse hover events

    private static @Nullable ItemStack fromBukkit(ItemContextBox contextBox, ItemStack original) {
        var bukkitItem = ItemModifier.modifyItem(contextBox);

        // Kiterino start - Prevent creative from overriding items
        CraftPlayer player = (CraftPlayer) contextBox.getViewer();
        if (player != null && me.sosedik.kiterino.KiterinoConfig.preventCreativeItemOverride && player.getGameMode() == org.bukkit.GameMode.CREATIVE && (!org.bukkit.inventory.ItemStack.isEmpty(bukkitItem) || !original.isEmpty())) {
            ItemStack itemStack = bukkitItem == null ? original.copy() : ItemStack.fromBukkitCopy(bukkitItem);
            CompoundTag itemData = original.isEmpty() ? null : (CompoundTag) net.minecraft.world.item.ItemStack.CODEC.encodeStart(
                SERIALIZATION_CONTEXT,
                original
            ).getOrThrow();
            byte[] serializedItem;
            if (itemData != null) {
                var outputStream = new ByteArrayOutputStream();
                try {
                    net.minecraft.nbt.NbtIo.writeCompressed(
                        itemData,
                        outputStream
                    );
                } catch (IOException _) {
                    return itemStack;
                }
                serializedItem = outputStream.toByteArray();
            } else {
                serializedItem = new byte[0];
            }
            net.minecraft.world.item.component.CustomData.update(DataComponents.CUSTOM_DATA, itemStack, tag -> tag.putByteArray("kiterino_og_item", serializedItem));
            return itemStack;
        }
        // Kiterino end - Prevent creative from overriding items

        return bukkitItem == null ? null : ItemStack.fromBukkitCopy(bukkitItem);
    }

}
