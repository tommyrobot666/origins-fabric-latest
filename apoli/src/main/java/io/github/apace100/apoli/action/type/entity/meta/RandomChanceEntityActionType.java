package io.github.apace100.apoli.action.type.entity.meta;

import io.github.apace100.apoli.action.ActionConfiguration;
import io.github.apace100.apoli.action.EntityAction;
import io.github.apace100.apoli.action.context.EntityActionContext;
import io.github.apace100.apoli.action.type.EntityActionType;
import io.github.apace100.apoli.action.type.EntityActionTypes;
import io.github.apace100.apoli.action.type.meta.RandomChanceMetaActionType;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class RandomChanceEntityActionType extends EntityActionType implements RandomChanceMetaActionType<EntityActionContext, EntityAction> {

	private final EntityAction successAction;
	private final Optional<EntityAction> failAction;

	private final float chance;

	public RandomChanceEntityActionType(EntityAction successAction, Optional<EntityAction> failAction, float chance) {
		this.successAction = successAction;
		this.failAction = failAction;
		this.chance = chance;
	}

	@Override
	public void accept(EntityActionContext context) {
		this.executeAction(context);
	}

	@Override
	public boolean shouldExecute(EntityActionContext context) {
		return true;
	}

	@Override
	public @NotNull ActionConfiguration<?> getConfig() {
		return EntityActionTypes.RANDOM_CHANCE;
	}

	@Override
	public EntityAction successAction() {
		return successAction;
	}

	@Override
	public Optional<EntityAction> failAction() {
		return failAction;
	}

	@Override
	public float chance() {
		return chance;
	}

}
