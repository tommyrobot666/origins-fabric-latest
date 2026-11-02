package io.github.apace100.apoli.action.type.block.meta;

import io.github.apace100.apoli.action.ActionConfiguration;
import io.github.apace100.apoli.action.BlockAction;
import io.github.apace100.apoli.action.context.BlockActionContext;
import io.github.apace100.apoli.action.type.BlockActionType;
import io.github.apace100.apoli.action.type.BlockActionTypes;
import io.github.apace100.apoli.action.type.meta.DelayMetaActionType;
import org.jetbrains.annotations.NotNull;

public class DelayBlockActionType extends BlockActionType implements DelayMetaActionType<BlockActionContext, BlockAction> {

	private final BlockAction action;
	private final int ticks;

	public DelayBlockActionType(BlockAction action, int ticks) {
		this.action = action;
		this.ticks = ticks;
	}

	@Override
	public void accept(BlockActionContext context) {
		this.executeAction(context);
	}

	@Override
	public @NotNull ActionConfiguration<?> getConfig() {
		return BlockActionTypes.DELAY;
	}

	@Override
	public BlockAction action() {
		return action;
	}

	@Override
	public int ticks() {
		return ticks;
	}

}
