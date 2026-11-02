package io.github.apace100.apoli.action.type.entity.meta;

import io.github.apace100.apoli.action.ActionConfiguration;
import io.github.apace100.apoli.action.context.EntityActionContext;
import io.github.apace100.apoli.action.type.EntityActionType;
import io.github.apace100.apoli.action.type.EntityActionTypes;
import io.github.apace100.apoli.action.type.meta.NothingMetaActionType;
import org.jetbrains.annotations.NotNull;

public class NothingEntityActionType extends EntityActionType implements NothingMetaActionType {

	@Override
	public void accept(EntityActionContext context) {

	}

	@Override
	public boolean shouldExecute(EntityActionContext context) {
		return true;
	}

	@Override
	public @NotNull ActionConfiguration<?> getConfig() {
		return EntityActionTypes.NOTHING;
	}

}
