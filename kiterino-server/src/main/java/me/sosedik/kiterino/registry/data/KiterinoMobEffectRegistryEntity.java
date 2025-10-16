package me.sosedik.kiterino.registry.data;

import io.papermc.paper.registry.PaperRegistryBuilder;
import io.papermc.paper.registry.data.util.Conversions;
import me.sosedik.kiterino.registry.wrapper.KiterinoMobEffectBehaviourWrapper;
import me.sosedik.kiterino.world.effect.KiterinoMobEffectWrapper;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import org.bukkit.potion.PotionEffectType;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
// Kiterino - Data-driven mob effects
public class KiterinoMobEffectRegistryEntity implements MobEffectRegistryEntity {

    protected KiterinoMobEffectBehaviourWrapper wrapper;
    protected MobEffectCategory category;
    protected int color;

    protected final Conversions conversions;

    public KiterinoMobEffectRegistryEntity(
        final Conversions conversions,
        final @Nullable MobEffect internal
    ) {
        this.conversions = conversions;
        if (internal == null) {
            this.wrapper = new KiterinoMobEffectBehaviourWrapper() { };
            this.category = MobEffectCategory.NEUTRAL;
            return;
        }

        this.category = internal.getCategory();
        this.color = internal.getColor();
    }

    @Override
    public KiterinoMobEffectBehaviourWrapper wrapper() {
        return this.wrapper;
    }

    @Override
    public PotionEffectType.Category category() {
        return conversions.asBukkit(this.category);
    }

    @Override
    public int color() {
        return this.color;
    }

    public static final class KiterinoBuilder extends KiterinoMobEffectRegistryEntity implements Builder,
            PaperRegistryBuilder<MobEffect, PotionEffectType> {

        public KiterinoBuilder(final Conversions conversions, final @Nullable MobEffect internal) {
            super(conversions, internal);
        }

        @Override
        public Builder wrapper(KiterinoMobEffectBehaviourWrapper wrapper) {
            this.wrapper = wrapper;
            return this;
        }

        @Override
        public Builder category(PotionEffectType.Category category) {
            this.category = conversions.asVanilla(category);
            return this;
        }

        @Override
        public Builder color(int color) {
            this.color = color;
            return this;
        }

        @Override
        public MobEffect build() {
            return new KiterinoMobEffectWrapper(this.category, this.color, this.wrapper);
        }

    }

}
