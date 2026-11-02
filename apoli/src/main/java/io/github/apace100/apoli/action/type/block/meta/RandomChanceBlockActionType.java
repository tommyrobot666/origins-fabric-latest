package io.github.apace100.apoli.action.type.block.meta;

import io.github.apace100.apoli.action.ActionConfiguration;
import io.github.apace100.apoli.action.BlockAction;
import io.github.apace100.apoli.action.context.BlockActionContext;
import io.github.apace100.apoli.action.type.BlockActionType;
import io.github.apace100.apoli.action.type.BlockActionTypes;
import io.github.apace100.apoli.action.type.meta.RandomChanceMetaActionType;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class RandomChanceBlockActionType extends BlockActionType implements RandomChanceMetaActionType<BlockActionContext, BlockAction> {

	private final BlockAction successAction;
	private final Optional<BlockAction> failAction;

	private final float chance;

	public RandomChanceBlockActionType(BlockAction successAction, Optional<BlockAction> failAction, float chance) {
		this.successAction = successAction;
		this.failAction = failAction;
		this.chance = chance;
	}

	@Override
	public void accept(BlockActionContext context) {
		this.executeAction(context);
	}

	@Override
	public @NotNull ActionConfiguration<?> getConfig() {
		return BlockActionTypes.RANDOM_CHANCE;
	}

	@Override
	public BlockAction successAction() {
		return successAction;
	}

	@Override
	public Optional<BlockAction> failAction() {
		return failAction;
	}

	@Override
	public float chance() {
		return chance;
	}

}
