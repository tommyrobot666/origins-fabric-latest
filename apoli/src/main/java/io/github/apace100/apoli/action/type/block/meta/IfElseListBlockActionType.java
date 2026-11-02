package io.github.apace100.apoli.action.type.block.meta;

import io.github.apace100.apoli.action.ActionConfiguration;
import io.github.apace100.apoli.action.BlockAction;
import io.github.apace100.apoli.action.context.BlockActionContext;
import io.github.apace100.apoli.action.type.BlockActionType;
import io.github.apace100.apoli.action.type.BlockActionTypes;
import io.github.apace100.apoli.action.type.meta.IfElseListMetaActionType;
import io.github.apace100.apoli.condition.BlockCondition;
import io.github.apace100.apoli.condition.context.BlockConditionContext;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class IfElseListBlockActionType extends BlockActionType implements IfElseListMetaActionType<BlockActionContext, BlockConditionContext, BlockAction, BlockCondition> {

	private final List<ConditionedAction<BlockAction, BlockCondition>> conditionedActions;

	public IfElseListBlockActionType(List<ConditionedAction<BlockAction, BlockCondition>> conditionedActions) {
		this.conditionedActions = conditionedActions;
	}

	@Override
	public void accept(BlockActionContext context) {
		this.executeActions(context);
	}

	@Override
	public @NotNull ActionConfiguration<?> getConfig() {
		return BlockActionTypes.IF_ELSE_LIST;
	}

	@Override
	public List<ConditionedAction<BlockAction, BlockCondition>> conditionedActions() {
		return conditionedActions;
	}

}
