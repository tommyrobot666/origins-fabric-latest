package io.github.apace100.apoli.action.type.block.meta;

import io.github.apace100.apoli.action.ActionConfiguration;
import io.github.apace100.apoli.action.context.BlockActionContext;
import io.github.apace100.apoli.action.type.BlockActionType;
import io.github.apace100.apoli.action.type.BlockActionTypes;
import io.github.apace100.apoli.action.type.meta.NothingMetaActionType;
import org.jetbrains.annotations.NotNull;

public class NothingBlockActionType extends BlockActionType implements NothingMetaActionType {

	@Override
	public void accept(BlockActionContext context) {

	}

	@Override
	public @NotNull ActionConfiguration<?> getConfig() {
		return BlockActionTypes.NOTHING;
	}

}
