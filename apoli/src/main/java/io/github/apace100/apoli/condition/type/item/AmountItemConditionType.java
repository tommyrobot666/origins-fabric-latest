package io.github.apace100.apoli.condition.type.item;

import io.github.apace100.apoli.condition.ConditionConfiguration;
import io.github.apace100.apoli.condition.context.ItemConditionContext;
import io.github.apace100.apoli.condition.type.ItemConditionType;
import io.github.apace100.apoli.condition.type.ItemConditionTypes;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.data.TypedDataObjectFactory;
import io.github.apace100.apoli.util.Comparison;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import org.jetbrains.annotations.NotNull;

public class AmountItemConditionType extends ItemConditionType {

    public static final TypedDataObjectFactory<AmountItemConditionType> DATA_FACTORY = TypedDataObjectFactory.simple(
        new SerializableData()
            .add("comparison", ApoliDataTypes.COMPARISON)
            .add("compare_to", SerializableDataTypes.INT),
        data -> new AmountItemConditionType(
            data.get("comparison"),
            data.get("compare_to")
        ),
        (conditionType, serializableData) -> serializableData.instance()
            .set("comparison", conditionType.comparison)
            .set("compare_to", conditionType.compareTo)
    );

    private final Comparison comparison;
    private final int compareTo;

    public AmountItemConditionType(Comparison comparison, int compareTo) {
        this.comparison = comparison;
        this.compareTo = compareTo;
    }

    @Override
    public boolean test(ItemConditionContext context) {
        return comparison.compare(context.stack().getCount(), compareTo);
    }

    @Override
    public @NotNull ConditionConfiguration<?> getConfig() {
        return ItemConditionTypes.AMOUNT;
    }

}
