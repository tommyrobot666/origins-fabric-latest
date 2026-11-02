package io.github.apace100.apoli.condition.type.damage;

import io.github.apace100.apoli.condition.ConditionConfiguration;
import io.github.apace100.apoli.condition.context.DamageConditionContext;
import io.github.apace100.apoli.condition.type.DamageConditionType;
import io.github.apace100.apoli.condition.type.DamageConditionTypes;
import io.github.apace100.apoli.data.TypedDataObjectFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import org.jetbrains.annotations.NotNull;

public class TypeDamageConditionType extends DamageConditionType {

    public static final TypedDataObjectFactory<TypeDamageConditionType> DATA_FACTORY = TypedDataObjectFactory.simple(
        new SerializableData()
            .add("damage_type", SerializableDataTypes.DAMAGE_TYPE),
        data -> new TypeDamageConditionType(
            data.get("damage_type")
        ),
        (conditionType, serializableData) -> serializableData.instance()
            .set("damage_type", conditionType.damageType)
    );

    private final RegistryKey<DamageType> damageType;

    public TypeDamageConditionType(RegistryKey<DamageType> damageType) {
        this.damageType = damageType;
    }

    @Override
    public boolean test(DamageConditionContext context) {
        return context.source().isOf(damageType);
    }

    @Override
    public @NotNull ConditionConfiguration<?> getConfig() {
        return DamageConditionTypes.TYPE;
    }

}
