package me.sosedik.kiterino.modifier.item;

import com.mojang.datafixers.util.Pair;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.BlockItemDataProperties;
import io.papermc.paper.datacomponent.item.BundleContents;
import io.papermc.paper.datacomponent.item.UseRemainder;
import me.sosedik.kiterino.KiterinoConfig;
import me.sosedik.kiterino.inventory.InventorySlotHelper;
import me.sosedik.kiterino.modifier.item.context.ItemModifierContext;
import me.sosedik.kiterino.modifier.item.context.ItemModifierContextType;
import me.sosedik.kiterino.modifier.item.context.packet.AdvancementPacketContext;
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
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundBundlePacket;
import net.minecraft.network.protocol.game.ClientboundContainerSetContentPacket;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
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
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.decoration.GlowItemFrame;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.EyeOfEnder;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.entity.projectile.ThrownEgg;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.entity.projectile.ThrownExperienceBottle;
import net.minecraft.world.entity.projectile.windcharge.AbstractWindCharge;
import net.minecraft.world.entity.projectile.windcharge.WindCharge;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
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
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.block.BlockType;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.CraftEquipmentSlot;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.inventory.CraftRecipe;
import org.bukkit.entity.EntityType;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;

@NullMarked
public class ItemModifiersHandlerImpl extends ItemModifiersHandler {

    public static final Map<NamespacedKey, ItemModifier> modifiers = new HashMap<>();

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
        for (NamespacedKey modifierId : KiterinoConfig.itemModifiersOrder) {
            ItemModifier modifier = modifiers.get(modifierId);
            if (modifier == null) continue;
            if (modifier.skipAir() && contextBox.getItem().getType() == Material.AIR) continue;
            if (modifier.skipContext(contextBox.getContextType())) continue;

            ModificationResult result = modifier.modify(contextBox);
            if (result == ModificationResult.PASS) continue;
            if (result == ModificationResult.RETURN)
                return contextBox.getItem();

            modified = true;
        }

	    // Special nbt cases
	    org.bukkit.inventory.ItemStack item = contextBox.getItem();
	    if (!item.isEmpty()) {
		    // Bundles
		    if (item.hasData(DataComponentTypes.BUNDLE_CONTENTS)) {
			    BundleContents bundleContents = item.getData(DataComponentTypes.BUNDLE_CONTENTS);
			    assert bundleContents != null;
			    List<org.bukkit.inventory.ItemStack> items = new ArrayList<>(bundleContents.contents());
			    for (int i = 0; i < items.size(); i++) {
				    org.bukkit.inventory.ItemStack bundleItem = items.get(i);
				    org.bukkit.inventory.ItemStack newItem = ItemModifier.modifyItem(new ItemContextBox(contextBox.getViewer(), contextBox.getLocale(), ItemModifierContextType.EMPTY_NO_LORE, ItemModifierContext.EMPTY, bundleItem));
				    if (newItem != null) {
					    items.set(i, bundleItem);
					    modified = true;
				    }
			    }
			    if (modified) {
				    item.setData(DataComponentTypes.BUNDLE_CONTENTS, BundleContents.bundleContents(items));
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

					    org.bukkit.inventory.ItemStack newItem = ItemModifier.modifyItem(new ItemContextBox(contextBox.getViewer(), contextBox.getLocale(), ItemModifierContextType.EMPTY_NO_LORE, ItemModifierContext.EMPTY, containerItem));
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

					    org.bukkit.inventory.ItemStack newItem = ItemModifier.modifyItem(new ItemContextBox(contextBox.getViewer(), contextBox.getLocale(), ItemModifierContextType.EMPTY_NO_LORE, ItemModifierContext.EMPTY, containerItem));
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
			    org.bukkit.inventory.ItemStack newItem = ItemModifier.modifyItem(new ItemContextBox(contextBox.getViewer(), contextBox.getLocale(), ItemModifierContextType.EMPTY_NO_LORE, ItemModifierContext.EMPTY, useRemainder.transformInto()));
			    if (newItem != null) {
				    modified = true;
				    item.setData(DataComponentTypes.USE_REMAINDER, UseRemainder.useRemainder(newItem));
			    }
		    }
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

    public static Packet<?> processPacket(CraftPlayer player, Packet<?> initialPacket) {
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
            case ClientboundSystemChatPacket packet -> handle(player, packet);
            default -> initialPacket;
        };
    }

    @SuppressWarnings("unchecked")
    private static Packet<?> handle(CraftPlayer player, ClientboundBundlePacket initialPacket) {
        if (!(initialPacket.subPackets() instanceof ArrayList list)) {
            KiterinoConfig.log(Level.WARNING, "Bundle packets are not in ArrayList: " + initialPacket.subPackets().getClass());
            return initialPacket;
        }

        ListIterator<Packet<?>> iterator = list.listIterator();
        while (iterator.hasNext()) {
            Packet<?> packet = iterator.next();
            Packet<?> newPacket = processPacket(player, packet);
            if (packet != newPacket) {
                iterator.set(newPacket);
            }
        }

        return initialPacket;
    }

    private static Packet<?> handle(CraftPlayer player, ClientboundContainerSetSlotPacket packet) {
        ItemStack original = packet.getItem();
        var context = new SlottedItemPacketContext(packet, packet.getSlot());
        var contextBox = new ItemContextBox(player, ItemModifierContextType.SET_SLOT, context, original.asBukkitCopy());
        ItemStack result = fromBukkit(contextBox, original);
        if (result != null) packet.itemStack = result;
        return packet;
    }

    private static Packet<?> handle(CraftPlayer player, ClientboundContainerSetContentPacket packet) {
        List<ItemStack> items = packet.getItems();
        for (int i = 0; i < items.size(); i++) {
            ItemStack original = items.get(i);
            var context = new SlottedItemPacketContext(packet, i);
            var contextBox = new ItemContextBox(player, ItemModifierContextType.WINDOW_ITEMS, context, original.asBukkitCopy());
            ItemStack result = fromBukkit(contextBox, original);
            if (result == null) continue;

            items.set(i, result);
        }

        ItemStack original = packet.getCarriedItem();
        var context = new SlottedItemPacketContext(packet, InventorySlotHelper.CURSOR);
        var contextBox = new ItemContextBox(player, ItemModifierContextType.WINDOW_ITEMS, context, original.asBukkitCopy());
        ItemStack result = fromBukkit(contextBox, original);
        if (result != null) packet.carriedItem = result;

        return packet;
    }

    private static Packet<?> handle(CraftPlayer player, ClientboundPlaceGhostRecipePacket packet) {
        RecipeDisplay display = packet.recipeDisplay();
        RecipeDisplay newDisplay = replaceRecipeDisplay(packet, ItemModifierContextType.RECIPE_GHOST, player, display); // TODO recipe id :F
        return display == newDisplay ? packet : new ClientboundPlaceGhostRecipePacket(packet.containerId(), newDisplay);
    }

    private static Packet<?> handle(CraftPlayer player, ClientboundSetCursorItemPacket packet) {
        ItemStack original = packet.contents();
        var context = new SlottedItemPacketContext(packet, InventorySlotHelper.CURSOR);
        var contextBox = new ItemContextBox(player, ItemModifierContextType.SET_SLOT, context, original.asBukkitCopy());
        ItemStack result = fromBukkit(contextBox, original);
        return result == null ? packet : new ClientboundSetCursorItemPacket(result);
    }

    private static Packet<?> handle(CraftPlayer player, ClientboundSetEquipmentPacket packet) {
        List<Pair<EquipmentSlot, ItemStack>> slots = packet.getSlots();
        int entityId = packet.getEntity();
        org.bukkit.entity.Entity entity = null;
        World world = player.getWorld();
        for (int i = 0; i < slots.size(); i++) {
            Pair<EquipmentSlot, ItemStack> slot = slots.get(i);
            ItemStack original = slot.getSecond();
            var context = new EntityEquipmentPacketContext(packet, world, entityId, entity, CraftEquipmentSlot.getSlot(slot.getFirst()));
            var contextBox = new ItemContextBox(player, ItemModifierContextType.ENTITY_EQUIPMENT, context, original.asBukkitCopy());
            ItemStack result = fromBukkit(contextBox, original);
            if (result != null) slots.set(i, Pair.of(slot.getFirst(), result));
            if (entity == null && context.isFetched()) entity = context.getEntity();
        }
        return packet;
    }

    // As much as I dislike this, not sure if there's a better way
    private static Packet<?> handle(CraftPlayer player, ClientboundSetEntityDataPacket packet) {
        int entityId = packet.id();
        Entity entity = ((CraftWorld) player.getWorld()).getHandle().moonrise$getEntityLookup().get(entityId);
        if (entity == null) {
            List<SynchedEntityData.DataValue<?>> dataValues = packet.packedItems();
            for (int i = 0; i < dataValues.size(); i++) {
                SynchedEntityData.DataValue<?> dataValue = dataValues.get(i);
                if (dataValue.serializer() != EntityDataSerializers.ITEM_STACK) continue;

                ItemStack original = (ItemStack) dataValue.value();
                var contextType = ItemModifierContextType.UNKNOWN_ENTITY_DATA;
                var context = new UnknownEntityDataContext(packet, player.getWorld(), entityId);
                var contextBox = new ItemContextBox(player, contextType, context, original.asBukkitCopy());
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
                context = new EntityDataPacketContext(packet, player.getWorld(), entityId, EntityType.ITEM, droppedItem);
                original = droppedItem.getItem();
            }
            case ItemFrame itemFrame -> {
                var entityType = itemFrame instanceof GlowItemFrame ? EntityType.GLOW_ITEM_FRAME : EntityType.ITEM_FRAME;
                context = new EntityDataPacketContext(packet, player.getWorld(), entityId, entityType, itemFrame);
                original = itemFrame.getItem();
            }
            case ThrowableItemProjectile throwableProjectile -> {
                EntityType entityType = switch (throwableProjectile) {
                    case Snowball snowball -> EntityType.SNOWBALL;
                    case ThrownEgg thrownEgg -> EntityType.EGG;
                    case ThrownEnderpearl thrownEnderpearl -> EntityType.ENDER_PEARL;
                    case ThrownExperienceBottle thrownExperienceBottle -> EntityType.EXPERIENCE_BOTTLE;
                    default -> EntityType.POTION;
                };
                context = new EntityDataPacketContext(packet, player.getWorld(), entityId, entityType, throwableProjectile);
                original = throwableProjectile.getItem();
            }
            case EyeOfEnder eyeOfEnder -> {
                context = new EntityDataPacketContext(packet, player.getWorld(), entityId, EntityType.EYE_OF_ENDER, eyeOfEnder);
                original = eyeOfEnder.getItem();
            }
            case FireworkRocketEntity firework -> {
                context = new EntityDataPacketContext(packet, player.getWorld(), entityId, EntityType.FIREWORK_ROCKET, firework);
                original = firework.getItem();
            }
            case Fireball fireball -> {
                var entityType = fireball instanceof LargeFireball ? EntityType.FIREBALL : EntityType.SMALL_FIREBALL;
                context = new EntityDataPacketContext(packet, player.getWorld(), entityId, entityType, fireball);
                original = fireball.getItem();
            }
            case AbstractWindCharge windCharge -> {
                var entityType = windCharge instanceof WindCharge ? EntityType.WIND_CHARGE : EntityType.BREEZE_WIND_CHARGE;
                context = new EntityDataPacketContext(packet, player.getWorld(), entityId, entityType, windCharge);
                original = windCharge.getItem();
            }
            case Display.ItemDisplay itemDisplay -> {
                context = new EntityDataPacketContext(packet, player.getWorld(), entityId, EntityType.ITEM_DISPLAY, itemDisplay);
                original = itemDisplay.getItemStack();
            }
            default -> {
                return packet;
            }
        }
        var contextBox = new ItemContextBox(player, ItemModifierContextType.ENTITY_DATA, context, original.asBukkitCopy());
        for (int i = 0; i < dataValues.size(); i++) {
            SynchedEntityData.DataValue<?> dataValue = dataValues.get(i);
            if (dataValue.serializer() != EntityDataSerializers.ITEM_STACK) continue;

            ItemStack result = fromBukkit(contextBox, original);
            if (result != null) dataValues.set(i, new SynchedEntityData.DataValue<>(dataValue.id(), EntityDataSerializers.ITEM_STACK, result));
            break;
        }
        return packet;
    }

    private static Packet<?> handle(CraftPlayer player, ClientboundSetPlayerInventoryPacket packet) {
        ItemStack original = packet.contents();
        var context = new SlottedItemPacketContext(packet, packet.slot());
        var contextBox = new ItemContextBox(player, ItemModifierContextType.SET_SLOT, context, original.asBukkitCopy());
        ItemStack result = fromBukkit(contextBox, original);
        return result == null ? packet : new ClientboundSetCursorItemPacket(result);
    }

    // Yes, this also sucks!
    private static Packet<?> handle(CraftPlayer player, ClientboundUpdateRecipesPacket packet) {
        List<SelectableRecipe.SingleInputEntry<StonecutterRecipe>> stonecutterRecipes = new ArrayList<>();
        for (SelectableRecipe.SingleInputEntry<StonecutterRecipe> stonecutterRecipeEntry : packet.stonecutterRecipes().entries()) {
            if (stonecutterRecipeEntry.recipe().recipe().isEmpty()) {
                stonecutterRecipes.add(stonecutterRecipeEntry);
                continue;
            }

            var ingredientContext = new RecipeBookPacketContext(packet, RecipeBookPacketContext.DisplayType.INGREDIENT);
            var resultContext = new RecipeBookPacketContext(packet, RecipeBookPacketContext.DisplayType.RESULT);

            RecipeHolder<StonecutterRecipe> recipe = stonecutterRecipeEntry.recipe().recipe().get();
            StonecutterRecipe nmsRecipe = recipe.value();
            Ingredient ingredient = replaceIngredient(player, ItemModifierContextType.RECIPE_BOOK, ingredientContext, nmsRecipe.input());

            ItemStack original = nmsRecipe.result;
            ItemStack result = replaceItem(player, ItemModifierContextType.RECIPE_BOOK, resultContext, original);
            if (result == null) {
                result = original;
            }

            nmsRecipe = new StonecutterRecipe(nmsRecipe.group(), ingredient, result);
            stonecutterRecipes.add(new SelectableRecipe.SingleInputEntry<>(ingredient, new SelectableRecipe<>(new SlotDisplay.ItemStackSlotDisplay(result), Optional.of(new RecipeHolder<>(recipe.id(), nmsRecipe)))));
        }

        return new ClientboundUpdateRecipesPacket(packet.itemSets(), new SelectableRecipe.SingleInputSet<>(stonecutterRecipes));
    }

    // This one is a real sucker (and I thought the entity data was bad :')
    // Changing items inside the recipes is a no-go and there's lack of context upon
    // writing to the buffer, so we have to recreate the recipes in there...
    private static Packet<?> handle(CraftPlayer player, ClientboundRecipeBookAddPacket packet) {
        List<ClientboundRecipeBookAddPacket.Entry> entries = new ArrayList<>(packet.entries());
        for (int r = 0; r < entries.size(); r++) {
            ClientboundRecipeBookAddPacket.Entry entry = entries.get(r);
            RecipeDisplayEntry contents = entry.contents();
            RecipeDisplay display = contents.display();
            RecipeDisplay newDisplay = replaceRecipeDisplay(packet, ItemModifierContextType.RECIPE_BOOK, player, display);
            if (display != newDisplay) {
                entries.set(r, new ClientboundRecipeBookAddPacket.Entry(
                        new RecipeDisplayEntry(
                                contents.id(),
                                newDisplay,
                                contents.group(),
                                contents.category(),
                                contents.craftingRequirements()
                        ),
                        entry.flags()
                ));
            }
        }

        return new ClientboundRecipeBookAddPacket(entries, packet.replace());
    }

    private static RecipeDisplay replaceRecipeDisplay(Packet<?> packet, ItemModifierContextType contextType, CraftPlayer player, RecipeDisplay display) {
        return switch (display) {
            case FurnaceRecipeDisplay furnaceRecipeDisplay -> {
                SlotDisplay ingredient = replaceSlotDisplay(player, contextType, new RecipeBookPacketContext(packet, RecipeBookPacketContext.DisplayType.INGREDIENT), furnaceRecipeDisplay.ingredient());
                SlotDisplay fuel = replaceSlotDisplay(player, contextType, new RecipeBookPacketContext(packet, RecipeBookPacketContext.DisplayType.INGREDIENT), furnaceRecipeDisplay.fuel());
                SlotDisplay result = replaceSlotDisplay(player, contextType, new RecipeBookPacketContext(packet, RecipeBookPacketContext.DisplayType.RESULT), furnaceRecipeDisplay.result());
                SlotDisplay craftingStation = replaceSlotDisplay(player, contextType, new RecipeBookPacketContext(packet, RecipeBookPacketContext.DisplayType.CRAFTING_STATION), furnaceRecipeDisplay.craftingStation());
                yield new FurnaceRecipeDisplay(ingredient, fuel, result, craftingStation, furnaceRecipeDisplay.duration(), furnaceRecipeDisplay.experience());
            }
            case ShapedCraftingRecipeDisplay shapedCraftingRecipeDisplay -> {
                List<SlotDisplay> ingredients = new ArrayList<>(shapedCraftingRecipeDisplay.ingredients());
                ingredients.replaceAll(slotDisplay -> replaceSlotDisplay(player, contextType, new RecipeBookPacketContext(packet, RecipeBookPacketContext.DisplayType.INGREDIENT), slotDisplay));
                SlotDisplay result = replaceSlotDisplay(player, contextType, new RecipeBookPacketContext(packet, RecipeBookPacketContext.DisplayType.RESULT), shapedCraftingRecipeDisplay.result());
                SlotDisplay craftingStation = replaceSlotDisplay(player, contextType, new RecipeBookPacketContext(packet, RecipeBookPacketContext.DisplayType.CRAFTING_STATION), shapedCraftingRecipeDisplay.craftingStation());
                yield new ShapedCraftingRecipeDisplay(shapedCraftingRecipeDisplay.width(), shapedCraftingRecipeDisplay.height(), ingredients, result, craftingStation);
            }
            case ShapelessCraftingRecipeDisplay shapelessCraftingRecipeDisplay -> {
                List<SlotDisplay> ingredients = new ArrayList<>(shapelessCraftingRecipeDisplay.ingredients());
                ingredients.replaceAll(slotDisplay -> replaceSlotDisplay(player, contextType, new RecipeBookPacketContext(packet, RecipeBookPacketContext.DisplayType.INGREDIENT), slotDisplay));
                SlotDisplay result = replaceSlotDisplay(player, contextType, new RecipeBookPacketContext(packet, RecipeBookPacketContext.DisplayType.RESULT), shapelessCraftingRecipeDisplay.result());
                SlotDisplay craftingStation = replaceSlotDisplay(player, contextType, new RecipeBookPacketContext(packet, RecipeBookPacketContext.DisplayType.CRAFTING_STATION), shapelessCraftingRecipeDisplay.craftingStation());
                yield new ShapelessCraftingRecipeDisplay(ingredients, result, craftingStation);
            }
            case SmithingRecipeDisplay smithingRecipeDisplay -> {
                SlotDisplay template = replaceSlotDisplay(player, contextType, new RecipeBookPacketContext(packet, RecipeBookPacketContext.DisplayType.SMITHING_TEMPLATE), smithingRecipeDisplay.template());
                SlotDisplay base = replaceSlotDisplay(player, contextType, new RecipeBookPacketContext(packet, RecipeBookPacketContext.DisplayType.SMITHING_BASE), smithingRecipeDisplay.base());
                SlotDisplay addition = replaceSlotDisplay(player, contextType, new RecipeBookPacketContext(packet, RecipeBookPacketContext.DisplayType.SMITHING_ADDITION), smithingRecipeDisplay.addition());
                SlotDisplay result = replaceSlotDisplay(player, contextType, new RecipeBookPacketContext(packet, RecipeBookPacketContext.DisplayType.RESULT), smithingRecipeDisplay.result());
                SlotDisplay craftingStation = replaceSlotDisplay(player, contextType, new RecipeBookPacketContext(packet, RecipeBookPacketContext.DisplayType.CRAFTING_STATION), smithingRecipeDisplay.craftingStation());
                yield new SmithingRecipeDisplay(template, base, addition, result, craftingStation);
            }
            case StonecutterRecipeDisplay stonecutterRecipeDisplay -> {
                SlotDisplay input = replaceSlotDisplay(player, contextType, new RecipeBookPacketContext(packet, RecipeBookPacketContext.DisplayType.INGREDIENT), stonecutterRecipeDisplay.input());
                SlotDisplay result = replaceSlotDisplay(player, contextType, new RecipeBookPacketContext(packet, RecipeBookPacketContext.DisplayType.RESULT), stonecutterRecipeDisplay.result());
                SlotDisplay craftingStation = replaceSlotDisplay(player, contextType, new RecipeBookPacketContext(packet, RecipeBookPacketContext.DisplayType.CRAFTING_STATION), stonecutterRecipeDisplay.craftingStation());
                yield new StonecutterRecipeDisplay(input, result, craftingStation);
            }
            default -> display;
        };
    }

    private static SlotDisplay replaceSlotDisplay(CraftPlayer player, ItemModifierContextType contextType, RecipeBookPacketContext context, SlotDisplay slotDisplay) {
        return switch (slotDisplay) {
            case SlotDisplay.Composite composite -> {
                List<SlotDisplay> displays = new ArrayList<>(composite.contents());
                displays.replaceAll(display -> replaceSlotDisplay(player, contextType, context, display));
                yield new SlotDisplay.Composite(displays);
            }
            case SlotDisplay.ItemSlotDisplay itemSlotDisplay -> {
                ItemStack replacement = replaceItem(player, contextType, context, itemSlotDisplay.item().value().getDefaultInstance());
                yield replacement == null ? itemSlotDisplay : new SlotDisplay.ItemStackSlotDisplay(replacement);
            }
            case SlotDisplay.ItemStackSlotDisplay itemStackSlotDisplay -> {
                ItemStack replacement = replaceItem(player, contextType, context, itemStackSlotDisplay.stack());
                yield replacement == null ? itemStackSlotDisplay : new SlotDisplay.ItemStackSlotDisplay(replacement);
            }
            case SlotDisplay.SmithingTrimDemoSlotDisplay smithingTrimDemoSlotDisplay -> {
                SlotDisplay base = replaceSlotDisplay(player, contextType, context, smithingTrimDemoSlotDisplay.base());
                SlotDisplay material = replaceSlotDisplay(player, contextType, context, smithingTrimDemoSlotDisplay.material());
                SlotDisplay pattern = replaceSlotDisplay(player, contextType, context, smithingTrimDemoSlotDisplay.pattern());
                if (base == smithingTrimDemoSlotDisplay.base() && material == smithingTrimDemoSlotDisplay.material() && pattern == smithingTrimDemoSlotDisplay.pattern())
                    yield smithingTrimDemoSlotDisplay;
                yield new SlotDisplay.SmithingTrimDemoSlotDisplay(base, material, pattern);
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

    private static Ingredient replaceIngredient(CraftPlayer player, ItemModifierContextType contextType, RecipeBookPacketContext context, Ingredient ingredient) {
        Ingredient superDirtyCopy = CraftRecipe.toIngredient(CraftRecipe.toBukkit(ingredient), false);
        Set<ItemStack> items = superDirtyCopy.itemStacks();
        if (items == null) {
            return ingredient;
        }

        Set<ItemStack> updatedItems = new HashSet<>();
        for (ItemStack item : items) {
            ItemStack result = replaceItem(player, contextType, context, item);
            updatedItems.add(result == null ? item : result);
        }
        superDirtyCopy.itemStacks = updatedItems;
        return superDirtyCopy;
    }

    private static @Nullable ItemStack replaceItem(CraftPlayer player, ItemModifierContextType contextType, RecipeBookPacketContext context, ItemStack original) {
        var contextBox = new ItemContextBox(player, contextType, context, original.asBukkitCopy());
        return fromBukkit(contextBox, original);
    }

    // I may have sinned, twice
    private static Packet<?> handle(CraftPlayer player, ClientboundUpdateAdvancementsPacket packet) {
        List<AdvancementHolder> advancements = new ArrayList<>(packet.getAdded());
        for (int i = 0; i < advancements.size(); i++) {
            AdvancementHolder oldHolder = advancements.get(i);
            Advancement oldAdvancement = oldHolder.value();
            DisplayInfo oldDisplay = oldAdvancement.display().orElse(null);
            if (oldDisplay == null) continue;

            var namespacedKey = new NamespacedKey(oldHolder.id().getNamespace(), oldHolder.id().getPath());
            var context = new AdvancementPacketContext(packet, namespacedKey, oldHolder::toBukkit);
            ItemStack original = oldDisplay.getIcon();
            var contextBox = new ItemContextBox(player, ItemModifierContextType.ADVANCEMENT, context, original.asBukkitCopy());
            ItemStack result = fromBukkit(contextBox, original);
            if (result == null) continue;

            var displayInfo = new DisplayInfo(
                    result,
                    oldDisplay.getTitle(),
                    oldDisplay.getDescription(),
                    oldDisplay.getBackground(),
                    oldDisplay.getType(),
                    oldDisplay.shouldShowToast(),
                    oldDisplay.shouldAnnounceChat(),
                    oldDisplay.isHidden()
            );
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
    private static Packet<?> handle(CraftPlayer player, ClientboundMerchantOffersPacket packet) {
        MerchantOffers newOffers = new MerchantOffers();
        MerchantOffers oldOffers = packet.getOffers();
        for (MerchantOffer merchantOffer : oldOffers) {
            boolean modified = false;
            ItemCost itemCost = merchantOffer.getItemCostA();
            ItemStack original = itemCost.itemStack();
            var context = new MerchantOfferPacketContext(packet, merchantOffer.asBukkit(), MerchantOfferPacketContext.Slot.FIRST);
            var contextBox = new ItemContextBox(player, ItemModifierContextType.MERCHANT_OFFER, context, original.asBukkitCopy());
            ItemStack result = fromBukkit(contextBox, original);
            if (result != null) {
                modified = true;
                merchantOffer = merchantOffer.copy();
                merchantOffer.baseCostA = new ItemCost(result.getItemHolder(), itemCost.count(), itemCost.components());
            }

            itemCost = merchantOffer.getItemCostB().orElse(null);
            if (itemCost != null) {
                original = itemCost.itemStack();
                context = new MerchantOfferPacketContext(packet, merchantOffer.asBukkit(), MerchantOfferPacketContext.Slot.SECOND);
                contextBox = new ItemContextBox(player, ItemModifierContextType.MERCHANT_OFFER, context, original.asBukkitCopy());
                result = fromBukkit(contextBox, original);
                if (result != null) {
                    if (!modified) {
                        modified = true;
                        merchantOffer = merchantOffer.copy();
                    }
                    merchantOffer.costB = Optional.of(new ItemCost(result.getItemHolder(), itemCost.count(), itemCost.components()));
                }
            }

            original = merchantOffer.getResult();
            context = new MerchantOfferPacketContext(packet, merchantOffer.asBukkit(), MerchantOfferPacketContext.Slot.RESULT);
            contextBox = new ItemContextBox(player, ItemModifierContextType.MERCHANT_OFFER, context, original.asBukkitCopy());
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

    // Kiterino start - Parse items in show_item hover event
    private static Packet<?> handle(CraftPlayer player, ClientboundSystemChatPacket initialPacket) {
        if (initialPacket.overlay()) {
            return initialPacket;
        }

        boolean prev = ComponentSerialization.DONT_RENDER_TRANSLATABLES.get();
        ComponentSerialization.DONT_RENDER_TRANSLATABLES.set(true);
        Component component = initialPacket.content();
        component = ComponentSerialization.replaceHoverItems(player, player.locale(), component);
        ComponentSerialization.DONT_RENDER_TRANSLATABLES.set(prev);
        return new ClientboundSystemChatPacket(component, false);
    }
    // Kiterino end - Parse items in show_item hover event

    private static @Nullable ItemStack fromBukkit(ItemContextBox contextBox, ItemStack original) {
        var bukkitItem = ItemModifier.modifyItem(contextBox);
        if (bukkitItem == null) return null;

	    // Kiterino start - Prevent creative from overriding items
	    ItemStack itemStack = ItemStack.fromBukkitCopy(bukkitItem);
	    CraftPlayer player = (CraftPlayer) contextBox.getViewer();
	    if (player != null && me.sosedik.kiterino.KiterinoConfig.preventCreativeItemOverride && player.getGameMode() == org.bukkit.GameMode.CREATIVE) {
		    net.minecraft.world.item.component.CustomData customData = itemStack.get(DataComponents.CUSTOM_DATA);
		    if (customData == null) {
			    customData = net.minecraft.world.item.component.CustomData.of(new CompoundTag());
			    customData.getUnsafe().put("kiterino_og_item", original.saveOptional(player.getHandle().registryAccess()));
			    itemStack.set(DataComponents.CUSTOM_DATA, customData);
		    } else {
			    customData.getUnsafe().put("kiterino_og_item", original.saveOptional(player.getHandle().registryAccess()));
		    }
	    }
	    return itemStack;
	    // Kiterino end - Prevent creative from overriding items
    }

}
