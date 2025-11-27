package me.sosedik.kiterino.event.inventory;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.view.CrafterView;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Called when previewing a result in a crafter
 */
// Kiterino - Add CrafterCraftPreviewEvent
@NullMarked
public class CrafterCraftPreviewEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();

    private final CrafterView view;
    private final Recipe recipe;
    private ItemStack result;

    /**
     * Crafter craft preview event
     *
     * @param player player
     * @param view crafter view
     * @param recipe recipe
     * @param result preview result
     */
    public CrafterCraftPreviewEvent(Player player, CrafterView view, Recipe recipe, ItemStack result) {
        super(player);
        this.view = view;
        this.recipe = recipe;
        this.result = result;
    }

    /**
     * Gets the crafter view
     *
     * @return the crafter view
     */
    public CrafterView getView() {
        return this.view;
    }

    /**
     * Gets the recipe
     *
     * @return recipe
     */
    public Recipe getRecipe() {
        return this.recipe;
    }

    /**
     * Gets the preview result
     *
     * @return the preview result
     */
    public ItemStack getResult() {
        return this.result;
    }

    /**
     * Sets the preview result
     *
     * @param result the preview result
     */
    public void setResult(@Nullable ItemStack result) {
        this.result = result == null ? ItemStack.empty() : result;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
