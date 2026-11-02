package io.github.apace100.apoli.action.type.bientity.meta;

import io.github.apace100.apoli.action.ActionConfiguration;
import io.github.apace100.apoli.action.BiEntityAction;
import io.github.apace100.apoli.action.context.BiEntityActionContext;
import io.github.apace100.apoli.action.type.BiEntityActionType;
import io.github.apace100.apoli.action.type.BiEntityActionTypes;
import io.github.apace100.apoli.action.type.meta.RandomChanceMetaActionType;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class RandomChanceBiEntityActionType extends BiEntityActionType implements RandomChanceMetaActionType<BiEntityActionContext, BiEntityAction> {

	private final BiEntityAction successAction;
	private final Optional<BiEntityAction> failAction;

	private final float chance;

	public RandomChanceBiEntityActionType(BiEntityAction successAction, Optional<BiEntityAction> failAction, float chance) {
		this.successAction = successAction;
		this.failAction = failAction;
		this.chance = chance;
	}

	@Override
	public void accept(BiEntityActionContext context) {
		this.executeAction(context);
	}

	@Override
	public @NotNull ActionConfiguration<?> getConfig() {
		return BiEntityActionTypes.RANDOM_CHANCE;
	}

	@Override
	public BiEntityAction successAction() {
		return successAction;
	}

	@Override
	public Optional<BiEntityAction> failAction() {
		return failAction;
	}

	@Override
	public float chance() {
		return chance;
	}

}
