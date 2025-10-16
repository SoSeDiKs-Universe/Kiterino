package me.sosedik.kiterino.world.effect;

import me.sosedik.kiterino.registry.wrapper.KiterinoMobEffectBehaviourWrapper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.bukkit.craftbukkit.damage.CraftDamageSource;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
// Kiterino - Data-driven mob effects
public class KiterinoMobEffectWrapper extends MobEffect {

    private final KiterinoMobEffectBehaviourWrapper mobEffect;

    public KiterinoMobEffectWrapper(MobEffectCategory category, int color, KiterinoMobEffectBehaviourWrapper mobEffect) {
        super(category, color);
        this.mobEffect = mobEffect;
    }

    @Override
    public int getBlendInDurationTicks() {
        return this.mobEffect.getBlendInDurationTicks();
    }

    @Override
    public int getBlendOutDurationTicks() {
        return this.mobEffect.getBlendOutDurationTicks();
    }

    @Override
    public int getBlendOutAdvanceTicks() {
        return this.mobEffect.getBlendOutAdvanceTicks();
    }

    @Override
    public boolean applyEffectTick(ServerLevel world, LivingEntity entity, int amplifier) {
        return this.mobEffect.applyEffectTick(entity.getBukkitLivingEntity(), amplifier);
    }

    @Override
    public void applyInstantenousEffect(ServerLevel world, @Nullable Entity source, @Nullable Entity attacker, LivingEntity target, int amplifier, double proximity) {
        this.mobEffect.applyInstantaneousEffect(source == null ? null : source.getBukkitEntity(), attacker == null ? null : attacker.getBukkitEntity(), target.getBukkitLivingEntity(), amplifier, proximity);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return this.mobEffect.shouldApplyEffectTickThisTick(duration, amplifier);
    }

    @Override
    public void onEffectStarted(LivingEntity entity, int amplifier) {
        this.mobEffect.onEffectStarted(entity.getBukkitLivingEntity(), amplifier);
    }

    @Override
    public void onEffectAdded(LivingEntity entity, int amplifier) {
        this.mobEffect.onEffectAdded(entity.getBukkitLivingEntity(), amplifier);
    }

    @Override
    public void onMobRemoved(ServerLevel world, LivingEntity entity, int amplifier, Entity.RemovalReason reason) {
        if (reason == Entity.RemovalReason.KILLED) {
            this.mobEffect.onMobKilled(entity.getBukkitLivingEntity(), amplifier);
        }
    }

    @Override
    public void onMobHurt(ServerLevel world, LivingEntity entity, int amplifier, DamageSource source, float amount) {
        this.mobEffect.onMobHurt(entity.getBukkitLivingEntity(), amplifier, new CraftDamageSource(source), amount);
    }

    @Override
    public boolean isInstantenous() {
        return this.mobEffect.isInstantaneous();
    }

}
