package io.github.apace100.apoli.action.type.block.meta;

import io.github.apace100.apoli.action.ActionConfiguration;
import io.github.apace100.apoli.action.BlockAction;
import io.github.apace100.apoli.action.context.BlockActionContext;
import io.github.apace100.apoli.action.type.BlockActionType;
import io.github.apace100.apoli.action.type.BlockActionTypes;
import io.github.apace100.apoli.action.type.meta.SideMetaActionType;
import org.jetbrains.annotations.NotNull;

public class SideBlockActionType extends BlockActionType implements SideMetaActionType<BlockActionContext, BlockAction> {

	private final BlockAction action;
	private final Side side;

	public SideBlockActionType(BlockAction action, Side side) {
		this.action = action;
		this.side = side;
	}

	@Override
	public void accept(BlockActionContext context) {
		this.executeAction(context);
	}

	@Override
	public @NotNull ActionConfiguration<?> getConfig() {
		return BlockActionTypes.SIDE;
	}

	@Override
	public BlockAction action() {
		return action;
	}

	@Override
	public Side side() {
		return side;
	}

}
