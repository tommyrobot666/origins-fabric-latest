package io.github.apace100.apoli.condition.type.bientity.meta;

import io.github.apace100.apoli.condition.BiEntityCondition;
import io.github.apace100.apoli.condition.ConditionConfiguration;
import io.github.apace100.apoli.condition.context.BiEntityConditionContext;
import io.github.apace100.apoli.condition.type.BiEntityConditionType;
import io.github.apace100.apoli.condition.type.BiEntityConditionTypes;
import io.github.apace100.apoli.data.TypedDataObjectFactory;
import io.github.apace100.calio.data.SerializableData;
import org.jetbrains.annotations.NotNull;

public class InvertBiEntityConditionType extends BiEntityConditionType {

    public static final TypedDataObjectFactory<InvertBiEntityConditionType> DATA_FACTORY = TypedDataObjectFactory.simple(
        new SerializableData()
            .add("condition", BiEntityCondition.DATA_TYPE),
        data -> new InvertBiEntityConditionType(
            data.get("condition")
        ),
        (conditionType, serializableData) -> serializableData.instance()
            .set("condition", conditionType.biEntityCondition)
    );

    private final BiEntityCondition biEntityCondition;

    public InvertBiEntityConditionType(BiEntityCondition biEntityCondition) {
        this.biEntityCondition = biEntityCondition;
    }

    @Override
    public boolean test(BiEntityConditionContext context) {
        return biEntityCondition.test(context.target(), context.actor());
    }

    @Override
    public @NotNull ConditionConfiguration<?> getConfig() {
        return BiEntityConditionTypes.INVERT;
    }

}
