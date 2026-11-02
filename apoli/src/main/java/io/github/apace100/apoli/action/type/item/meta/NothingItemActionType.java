package io.github.apace100.apoli.action.type.item.meta;

import io.github.apace100.apoli.action.ActionConfiguration;
import io.github.apace100.apoli.action.context.ItemActionContext;
import io.github.apace100.apoli.action.type.ItemActionType;
import io.github.apace100.apoli.action.type.ItemActionTypes;
import io.github.apace100.apoli.action.type.meta.NothingMetaActionType;
import org.jetbrains.annotations.NotNull;

public class NothingItemActionType extends ItemActionType implements NothingMetaActionType {

	@Override
	public void accept(ItemActionContext context) {

	}

	@Override
	public @NotNull ActionConfiguration<?> getConfig() {
		return ItemActionTypes.NOTHING;
	}

}
