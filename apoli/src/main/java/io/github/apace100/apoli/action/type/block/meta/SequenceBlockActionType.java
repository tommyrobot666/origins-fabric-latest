package io.github.apace100.apoli.action.type.block.meta;

import io.github.apace100.apoli.action.ActionConfiguration;
import io.github.apace100.apoli.action.BlockAction;
import io.github.apace100.apoli.action.context.BlockActionContext;
import io.github.apace100.apoli.action.type.BlockActionType;
import io.github.apace100.apoli.action.type.BlockActionTypes;
import io.github.apace100.apoli.action.type.meta.SequenceMetaActionType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SequenceBlockActionType extends BlockActionType implements SequenceMetaActionType<BlockActionContext, BlockAction> {

	private final List<BlockAction> actions;

	public SequenceBlockActionType(List<BlockAction> actions) {
		this.actions = actions;
	}

	@Override
	public void accept(BlockActionContext context) {
		this.executeActions(context);
	}

	@Override
	public @NotNull ActionConfiguration<?> getConfig() {
		return BlockActionTypes.SEQUENCE;
	}

	@Override
	public List<BlockAction> actions() {
		return actions;
	}

}
