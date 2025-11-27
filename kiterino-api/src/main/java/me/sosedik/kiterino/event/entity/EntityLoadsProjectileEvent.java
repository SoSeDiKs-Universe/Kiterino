package me.sosedik.kiterino.event.entity;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.function.Predicate;

/**
 * Called when some item needs a projectile for further actions
 */
@NullMarked
// Kiterino - Add EntityLoadsProjectileEvent
public class EntityLoadsProjectileEvent extends EntityEvent {

    private static final HandlerList handlers = new HandlerList();

    private final ItemStack weapon;
    private final Predicate<ItemStack> projectileValidator;
    private ItemStack projectile;

    public EntityLoadsProjectileEvent(LivingEntity entity, ItemStack weapon, @Nullable ItemStack projectile, Predicate<ItemStack> projectileValidator) {
        super(entity);
        this.weapon = weapon;
        this.projectile = projectile == null ? ItemStack.empty() : projectile;
        this.projectileValidator = projectileValidator;
    }

    /**
     * Returns the entity firing the weapon
     *
     * @return the entity firing the weapon
     */
    @Override
    public LivingEntity getEntity() {
        return (LivingEntity) this.entity;
    }

    /**
     * Returns the weapon requesting the projectile
     *
     * @return weapon
     */
    public ItemStack getWeapon() {
        return weapon;
    }

    /**
     * Returns the projectile that will be submitted to the weapon.
     * {@link ItemStack#isEmpty()} items mean no projectile.
     *
     * @return projectile
     */
    public ItemStack getProjectile() {
        return projectile;
    }

    /**
     * Sets the projectile that will be used by the weapon
     *
     * @param projectile projectile
     */
    public void setProjectile(@Nullable ItemStack projectile) {
        this.projectile = projectile == null ? ItemStack.empty() : projectile;
    }

    /**
     * Checks whether the provided item can be fired from the weapon.
     * <br>
     * You may still provide a non-fireable projectile to it.
     *
     * @param item projectile item
     * @return whether the provided item can be fired from the weapon
     */
    public boolean canBeFired(@Nullable ItemStack item) {
        return item != null && this.projectileValidator.test(item);
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
