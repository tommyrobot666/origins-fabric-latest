package io.github.apace100.apoli.condition.type.bientity.meta;

import io.github.apace100.apoli.condition.BiEntityCondition;
import io.github.apace100.apoli.condition.ConditionConfiguration;
import io.github.apace100.apoli.condition.context.BiEntityConditionContext;
import io.github.apace100.apoli.condition.type.BiEntityConditionType;
import io.github.apace100.apoli.condition.type.BiEntityConditionTypes;
import io.github.apace100.apoli.data.TypedDataObjectFactory;
import io.github.apace100.calio.data.SerializableData;
import org.jetbrains.annotations.NotNull;

public class UndirectedBiEntityConditionType extends BiEntityConditionType {

    public static final TypedDataObjectFactory<UndirectedBiEntityConditionType> DATA_FACTORY = TypedDataObjectFactory.simple(
        new SerializableData()
            .add("condition", BiEntityCondition.DATA_TYPE),
        data -> new UndirectedBiEntityConditionType(
            data.get("condition")
        ),
        (conditionType, serializableData) -> serializableData.instance()
            .set("condition", conditionType.biEntityCondition)
    );

    private final BiEntityCondition biEntityCondition;

    public UndirectedBiEntityConditionType(BiEntityCondition biEntityCondition) {
        this.biEntityCondition = biEntityCondition;
    }

    @Override
    public boolean test(BiEntityConditionContext context) {
        return biEntityCondition.test(context)
            || biEntityCondition.test(context.target(), context.actor());
    }

    @Override
    public @NotNull ConditionConfiguration<?> getConfig() {
        return BiEntityConditionTypes.UNDIRECTED;
    }

}
