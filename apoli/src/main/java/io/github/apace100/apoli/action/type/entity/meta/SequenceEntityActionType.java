package io.github.apace100.apoli.action.type.entity.meta;

import io.github.apace100.apoli.action.ActionConfiguration;
import io.github.apace100.apoli.action.EntityAction;
import io.github.apace100.apoli.action.context.EntityActionContext;
import io.github.apace100.apoli.action.type.EntityActionType;
import io.github.apace100.apoli.action.type.EntityActionTypes;
import io.github.apace100.apoli.action.type.meta.SequenceMetaActionType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SequenceEntityActionType extends EntityActionType implements SequenceMetaActionType<EntityActionContext, EntityAction> {

	private final List<EntityAction> actions;

	public SequenceEntityActionType(List<EntityAction> actions) {
		this.actions = actions;
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
		return EntityActionTypes.SEQUENCE;
	}

	@Override
	public List<EntityAction> actions() {
		return actions;
	}

}
