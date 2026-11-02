package io.github.apace100.apoli.action.type.bientity.meta;

import io.github.apace100.apoli.action.ActionConfiguration;
import io.github.apace100.apoli.action.EntityAction;
import io.github.apace100.apoli.action.context.BiEntityActionContext;
import io.github.apace100.apoli.action.type.BiEntityActionType;
import io.github.apace100.apoli.action.type.BiEntityActionTypes;
import io.github.apace100.apoli.data.TypedDataObjectFactory;
import io.github.apace100.calio.data.SerializableData;
import org.jetbrains.annotations.NotNull;

public class TargetActionBiEntityActionType extends BiEntityActionType {

    public static final TypedDataObjectFactory<TargetActionBiEntityActionType> DATA_FACTORY = TypedDataObjectFactory.simple(
        new SerializableData()
            .add("action", EntityAction.DATA_TYPE),
        data -> new TargetActionBiEntityActionType(
            data.get("action")
        ),
        (actionType, serializableData) -> serializableData.instance()
            .set("action", actionType.action)
    );

    private final EntityAction action;

    public TargetActionBiEntityActionType(EntityAction action) {
        this.action = action;
    }

    @Override
    public void accept(BiEntityActionContext context) {
        action.execute(context.target());
    }

    @Override
    public @NotNull ActionConfiguration<?> getConfig() {
        return BiEntityActionTypes.TARGET_ACTION;
    }

}
