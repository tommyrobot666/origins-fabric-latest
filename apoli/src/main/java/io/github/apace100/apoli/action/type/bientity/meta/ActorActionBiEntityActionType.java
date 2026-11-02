package io.github.apace100.apoli.action.type.bientity.meta;

import io.github.apace100.apoli.action.ActionConfiguration;
import io.github.apace100.apoli.action.EntityAction;
import io.github.apace100.apoli.action.context.BiEntityActionContext;
import io.github.apace100.apoli.action.type.BiEntityActionType;
import io.github.apace100.apoli.action.type.BiEntityActionTypes;
import io.github.apace100.apoli.data.TypedDataObjectFactory;
import io.github.apace100.calio.data.SerializableData;
import org.jetbrains.annotations.NotNull;

public class ActorActionBiEntityActionType extends BiEntityActionType {

    public static final TypedDataObjectFactory<ActorActionBiEntityActionType> DATA_FACTORY = TypedDataObjectFactory.simple(
        new SerializableData()
            .add("action", EntityAction.DATA_TYPE),
        data -> new ActorActionBiEntityActionType(
            data.get("action")
        ),
        (actionType, serializableData) -> serializableData.instance()
            .set("action", actionType.action)
    );

    private final EntityAction action;

    public ActorActionBiEntityActionType(EntityAction action) {
        this.action = action;
    }

    @Override
    public void accept(BiEntityActionContext context) {
        action.execute(context.actor());
    }

    @Override
    public @NotNull ActionConfiguration<?> getConfig() {
        return BiEntityActionTypes.ACTOR_ACTION;
    }

}
