package io.github.apace100.apoli.action.type.entity;

import com.mojang.serialization.DataResult;
import io.github.apace100.apoli.action.ActionConfiguration;
import io.github.apace100.apoli.action.context.EntityActionContext;
import io.github.apace100.apoli.action.type.EntityActionType;
import io.github.apace100.apoli.action.type.EntityActionTypes;
import io.github.apace100.apoli.data.TypedDataObjectFactory;
import io.github.apace100.apoli.util.MiscUtil;
import io.github.apace100.apoli.util.modifier.Modifier;
import io.github.apace100.apoli.util.modifier.ModifierUtil;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class DamageEntityActionType extends EntityActionType {

    public static final TypedDataObjectFactory<DamageEntityActionType> DATA_FACTORY = TypedDataObjectFactory.simple(
        new SerializableData()
            .add("damage_type", SerializableDataTypes.DAMAGE_TYPE)
            .add("amount", SerializableDataTypes.FLOAT.optional(), Optional.empty())
            .add("modifier", Modifier.DATA_TYPE, null)
            .addFunctionedDefault("modifiers", Modifier.LIST_TYPE, data -> MiscUtil.singletonListOrNull(data.get("modifier")))
            .validate(data -> {

                if (MiscUtil.anyPresent(data, "modifier", "modifiers")) {
                    return DataResult.success(data);
                }

                else {
                    Optional<Float> amount = data.get("amount");
                    return amount
                        .map(value -> DataResult.success(data))
                        .orElseGet(() -> DataResult.error(() -> "Any of 'amount', 'modifier', or 'modifier' fields must be defined!"));
                }

            }),
        data -> new DamageEntityActionType(
            data.get("damage_type"),
            data.get("amount"),
            data.get("modifiers")
        ),
        (actionType, serializableData) -> serializableData.instance()
            .set("damage_type", actionType.damageType)
            .set("amount", actionType.amount)
            .set("modifiers", actionType.modifiers)
    );

    private final RegistryKey<DamageType> damageType;
    private final Optional<Float> amount;

    private final List<Modifier> modifiers;

    public DamageEntityActionType(RegistryKey<DamageType> damageType, Optional<Float> amount, List<Modifier> modifiers) {
        this.damageType = damageType;
        this.amount = amount;
        this.modifiers = modifiers;
    }

    @Override
    public void accept(EntityActionContext context) {

        Entity entity = context.entity();
        DamageSources damageSources = entity.getDamageSources();

        this.amount
            .or(() -> getModifiedAmount(entity))
            .ifPresent(amount -> entity.damage(damageSources.create(damageType), amount));

    }

    @Override
    public @NotNull ActionConfiguration<?> getConfig() {
        return EntityActionTypes.DAMAGE;
    }

    private Optional<Float> getModifiedAmount(Entity entity) {
        return !modifiers.isEmpty() && entity instanceof LivingEntity livingEntity
            ? Optional.of((float) ModifierUtil.applyModifiers(entity, modifiers, livingEntity.getMaxHealth()))
            : Optional.empty();
    }

}
