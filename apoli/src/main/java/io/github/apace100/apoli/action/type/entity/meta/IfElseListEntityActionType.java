package io.github.apace100.apoli.action.type.entity.meta;

import io.github.apace100.apoli.action.ActionConfiguration;
import io.github.apace100.apoli.action.EntityAction;
import io.github.apace100.apoli.action.context.EntityActionContext;
import io.github.apace100.apoli.action.type.EntityActionType;
import io.github.apace100.apoli.action.type.EntityActionTypes;
import io.github.apace100.apoli.action.type.meta.IfElseListMetaActionType;
import io.github.apace100.apoli.condition.EntityCondition;
import io.github.apace100.apoli.condition.context.EntityConditionContext;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class IfElseListEntityActionType extends EntityActionType implements IfElseListMetaActionType<EntityActionContext, EntityConditionContext, EntityAction, EntityCondition> {

	private final List<ConditionedAction<EntityAction, EntityCondition>> conditionedActions;

	public IfElseListEntityActionType(List<ConditionedAction<EntityAction, EntityCondition>> conditionedActions) {
		this.conditionedActions = conditionedActions;
	}

	@Override
	public void accept(EntityActionContext context) {
		this.executeActions(context);
	}

	@Override
	public boolean shouldExecute(EntityActionContext context) {
		return true;
	}

	@Override
	public @NotNull ActionConfiguration<?> getConfig() {
		return EntityActionTypes.IF_ELSE_LIST;
	}

	@Override
	public List<ConditionedAction<EntityAction, EntityCondition>> conditionedActions() {
		return conditionedActions;
	}

}
