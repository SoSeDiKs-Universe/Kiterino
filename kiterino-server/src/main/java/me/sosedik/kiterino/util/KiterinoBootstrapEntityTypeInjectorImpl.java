package me.sosedik.kiterino.util;

import net.kyori.adventure.key.Key;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.entity.CraftEntityTypes;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import static me.sosedik.kiterino.util.KiterinoUnsafeUtil.getField;

@NullMarked
public class KiterinoBootstrapEntityTypeInjectorImpl extends KiterinoEnumExtender<EntityType> implements IKiterinoBootstrapEntityTypeInjector {

    public static final Map<net.minecraft.world.entity.EntityType<?>, net.minecraft.world.entity.EntityType<?>> ENTITY_TYPE_REPLACEMENTS = new HashMap<>();

    private final Field nameField = getField(EntityType.class, "name");
    private final Field clazzField = getField(EntityType.class, "clazz");
    private final Field typeIdField = getField(EntityType.class, "typeId");
    private final Field independentField = getField(EntityType.class, "independent");
    private final Field livingField = getField(EntityType.class, "living");
    private final Field keyField = getField(EntityType.class, "key");
    private final Field nameMapField = getField(EntityType.class, "NAME_MAP");

    private final List<InjectBundle> injectBundles = new ArrayList<>();
    private final List<AttributeBundle> attributesToRegister = new ArrayList<>();
    private @Nullable InjectBundle currentInjectBundle;

    @Override
    public void addEntityTypes(Class<?> entityTypesClass, Class<?> nmsEntityTypesClass, Function<Key, Object> entityTypeDataProvider, Consumer<Key> attributeProvider) {
        injectBundles.add(new InjectBundle(entityTypesClass, nmsEntityTypesClass, entityTypeDataProvider, attributeProvider));
    }

    @Override
    public void injectEnum(EntityType value) throws Exception {
        CraftEntityTypes.EntityTypeData entityTypeData = (CraftEntityTypes.EntityTypeData) currentInjectBundle.entityTypeDataProvider.apply(value.key());
        clazzField.set(value, entityTypeData.entityClass());
        livingField.set(value, LivingEntity.class.isAssignableFrom(entityTypeData.entityClass()));
        CraftEntityTypes.register(entityTypeData);
        attributesToRegister.add(new AttributeBundle(value.key(), currentInjectBundle.attributeProvider()));
    }

    @SuppressWarnings({"unchecked", "java:S3011"})
    @Override
    public EntityType allocateEnum(Key key) {
        try {
            EntityType entityType = super.allocateEnum(key);
            independentField.set(entityType, true);
            typeIdField.set(entityType, (short) -1);
            nameField.set(entityType, key.value());
            keyField.set(entityType, new NamespacedKey(key.namespace(), key.value()));

            String enumName = entityType.name();
            enumNameField.set(entityType, enumName);
            ((Map<String, EntityType>) nameMapField.get(null)).put(enumName.toLowerCase(Locale.US), entityType);

            return entityType;
        } catch (Exception e) {
            throw new KiterinoEnumExtenderRuntimeException("Couldn't extend EntityType: " + key, e);
        }
    }

    private void startInject() {
        for (InjectBundle injectBundle : injectBundles) {
            currentInjectBundle = injectBundle;
            injectEnums(injectBundle.entityTypesClass);
        }
        injectAllocated();
        currentInjectBundle = null;
    }

    private void registerAttributes() {
        attributesToRegister.forEach(bundle -> bundle.attributeProvider().accept(bundle.key()));
    }

    public static void init() {
        KiterinoBootstrapEntityTypeInjector.injector = new KiterinoBootstrapEntityTypeInjectorImpl();
    }

    public static void destruct() {
        KiterinoBootstrapEntityTypeInjector.injector = null;
    }

    public static void setup() {
        if (KiterinoBootstrapEntityTypeInjector.injector != null) {
            ((KiterinoBootstrapEntityTypeInjectorImpl) KiterinoBootstrapEntityTypeInjector.injector).startInject();
        }
    }

    public static void attributes() {
        if (KiterinoBootstrapEntityTypeInjector.injector != null) {
            ((KiterinoBootstrapEntityTypeInjectorImpl) KiterinoBootstrapEntityTypeInjector.injector).registerAttributes();
        }
    }

    private record InjectBundle(Class<?> entityTypesClass, Class<?> nmsEntityTypesClass, Function<Key, Object> entityTypeDataProvider, Consumer<Key> attributeProvider) {}
    private record AttributeBundle(Key key, Consumer<Key> attributeProvider) {}

}
