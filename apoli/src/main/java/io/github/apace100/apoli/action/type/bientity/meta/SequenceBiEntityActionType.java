package io.github.apace100.apoli.action.type.bientity.meta;

import io.github.apace100.apoli.action.ActionConfiguration;
import io.github.apace100.apoli.action.BiEntityAction;
import io.github.apace100.apoli.action.context.BiEntityActionContext;
import io.github.apace100.apoli.action.type.BiEntityActionType;
import io.github.apace100.apoli.action.type.BiEntityActionTypes;
import io.github.apace100.apoli.action.type.meta.SequenceMetaActionType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SequenceBiEntityActionType extends BiEntityActionType implements SequenceMetaActionType<BiEntityActionContext, BiEntityAction> {

	private final List<BiEntityAction> actions;

	public SequenceBiEntityActionType(List<BiEntityAction> actions) {
		this.actions = actions;
	}

	@Override
	public void accept(BiEntityActionContext context) {
		this.executeActions(context);
	}

	@Override
	public @NotNull ActionConfiguration<?> getConfig() {
		return BiEntityActionTypes.SEQUENCE;
	}

	@Override
	public List<BiEntityAction> actions() {
		return actions;
	}

}
