package io.github.apace100.apoli.condition.type.bientity.meta;

import io.github.apace100.apoli.condition.ConditionConfiguration;
import io.github.apace100.apoli.condition.EntityCondition;
import io.github.apace100.apoli.condition.context.BiEntityConditionContext;
import io.github.apace100.apoli.condition.type.BiEntityConditionType;
import io.github.apace100.apoli.condition.type.BiEntityConditionTypes;
import io.github.apace100.apoli.data.TypedDataObjectFactory;
import io.github.apace100.apoli.util.requirement.BiEntityRequirement;
import io.github.apace100.calio.data.SerializableData;
import org.jetbrains.annotations.NotNull;

public class EitherBiEntityConditionType extends BiEntityConditionType {

    public static final TypedDataObjectFactory<EitherBiEntityConditionType> DATA_FACTORY = TypedDataObjectFactory.simple(
        new SerializableData()
            .add("condition", EntityCondition.DATA_TYPE),
        data -> new EitherBiEntityConditionType(
            data.get("condition")
        ),
        (conditionType, serializableData) -> serializableData.instance()
            .set("condition", conditionType.entityCondition)
    );

    private final EntityCondition entityCondition;

    public EitherBiEntityConditionType(EntityCondition entityCondition) {
        this.entityCondition = entityCondition;
    }

    @Override
    public boolean test(BiEntityConditionContext context) {
        return entityCondition.test(context.actor())
            || entityCondition.test(context.target());
    }

    @Override
    public BiEntityRequirement getRequirement() {
        return BiEntityRequirement.EITHER;
    }

    @Override
    public @NotNull ConditionConfiguration<?> getConfig() {
        return BiEntityConditionTypes.EITHER;
    }

}
