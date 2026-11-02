package io.github.apace100.apoli.action.type.bientity.meta;

import io.github.apace100.apoli.action.ActionConfiguration;
import io.github.apace100.apoli.action.context.BiEntityActionContext;
import io.github.apace100.apoli.action.type.BiEntityActionType;
import io.github.apace100.apoli.action.type.BiEntityActionTypes;
import io.github.apace100.apoli.action.type.meta.NothingMetaActionType;
import org.jetbrains.annotations.NotNull;

public class NothingBiEntityActionType extends BiEntityActionType implements NothingMetaActionType {

	@Override
	public void accept(BiEntityActionContext context) {

	}

	@Override
	public @NotNull ActionConfiguration<?> getConfig() {
		return BiEntityActionTypes.NOTHING;
	}

}
