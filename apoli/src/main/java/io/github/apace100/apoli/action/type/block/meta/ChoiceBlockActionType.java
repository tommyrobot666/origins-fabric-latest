package io.github.apace100.apoli.action.type.block.meta;

import io.github.apace100.apoli.action.ActionConfiguration;
import io.github.apace100.apoli.action.BlockAction;
import io.github.apace100.apoli.action.context.BlockActionContext;
import io.github.apace100.apoli.action.type.BlockActionType;
import io.github.apace100.apoli.action.type.BlockActionTypes;
import io.github.apace100.apoli.action.type.meta.ChoiceMetaActionType;
import net.minecraft.util.collection.WeightedList;
import org.jetbrains.annotations.NotNull;

public class ChoiceBlockActionType extends BlockActionType implements ChoiceMetaActionType<BlockActionContext, BlockAction> {

	private final WeightedList<BlockAction> actions;

	public ChoiceBlockActionType(WeightedList<BlockAction> actions) {
		this.actions = actions;
	}

	@Override
	public void accept(BlockActionContext context) {
		this.executeActions(context);
	}

	@Override
	public @NotNull ActionConfiguration<?> getConfig() {
		return BlockActionTypes.CHOICE;
	}

	@Override
	public WeightedList<BlockAction> actions() {
		return actions;
	}

}
