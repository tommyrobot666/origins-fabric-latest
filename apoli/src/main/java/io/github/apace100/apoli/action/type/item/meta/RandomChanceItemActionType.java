package io.github.apace100.apoli.action.type.item.meta;

import io.github.apace100.apoli.action.ActionConfiguration;
import io.github.apace100.apoli.action.ItemAction;
import io.github.apace100.apoli.action.context.ItemActionContext;
import io.github.apace100.apoli.action.type.ItemActionType;
import io.github.apace100.apoli.action.type.ItemActionTypes;
import io.github.apace100.apoli.action.type.meta.RandomChanceMetaActionType;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class RandomChanceItemActionType extends ItemActionType implements RandomChanceMetaActionType<ItemActionContext, ItemAction> {

	private final ItemAction successAction;
	private final Optional<ItemAction> failAction;

	private final float chance;

	public RandomChanceItemActionType(ItemAction successAction, Optional<ItemAction> failAction, float chance) {
		this.successAction = successAction;
		this.failAction = failAction;
		this.chance = chance;
	}

	@Override
	public void accept(ItemActionContext context) {
		this.executeAction(context);
	}

	@Override
	public @NotNull ActionConfiguration<?> getConfig() {
		return ItemActionTypes.RANDOM_CHANCE;
	}

	@Override
	public ItemAction successAction() {
		return successAction;
	}

	@Override
	public Optional<ItemAction> failAction() {
		return failAction;
	}

	@Override
	public float chance() {
		return chance;
	}

}
