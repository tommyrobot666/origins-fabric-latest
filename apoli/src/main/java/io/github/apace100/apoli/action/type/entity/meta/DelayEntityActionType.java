package io.github.apace100.apoli.action.type.entity.meta;

import io.github.apace100.apoli.action.ActionConfiguration;
import io.github.apace100.apoli.action.EntityAction;
import io.github.apace100.apoli.action.context.EntityActionContext;
import io.github.apace100.apoli.action.type.EntityActionType;
import io.github.apace100.apoli.action.type.EntityActionTypes;
import io.github.apace100.apoli.action.type.meta.DelayMetaActionType;
import org.jetbrains.annotations.NotNull;

public class DelayEntityActionType extends EntityActionType implements DelayMetaActionType<EntityActionContext, EntityAction> {

	private final EntityAction action;
	private final int ticks;

	public DelayEntityActionType(EntityAction action, int ticks) {
		this.action = action;
		this.ticks = ticks;
	}

	@Override
	public void accept(EntityActionContext context) {
		this.executeAction(context);
	}

	@Override
	public @NotNull ActionConfiguration<?> getConfig() {
		return EntityActionTypes.DELAY;
	}

	@Override
	public EntityAction action() {
		return action;
	}

	@Override
	public int ticks() {
		return ticks;
	}

}
