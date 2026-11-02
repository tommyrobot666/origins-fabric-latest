package io.github.apace100.apoli.condition.type.bientity.meta;

import io.github.apace100.apoli.condition.ConditionConfiguration;
import io.github.apace100.apoli.condition.context.BiEntityConditionContext;
import io.github.apace100.apoli.condition.type.BiEntityConditionType;
import io.github.apace100.apoli.condition.type.BiEntityConditionTypes;
import io.github.apace100.apoli.condition.type.meta.ConstantMetaConditionType;
import org.jetbrains.annotations.NotNull;

public class ConstantBiEntityConditionType extends BiEntityConditionType implements ConstantMetaConditionType {

	private final boolean value;

	public ConstantBiEntityConditionType(boolean value) {
		this.value = value;
	}

	@Override
	public boolean test(BiEntityConditionContext context) {
		return value();
	}

	@Override
	public @NotNull ConditionConfiguration<?> getConfig() {
		return BiEntityConditionTypes.CONSTANT;
	}

	@Override
	public boolean value() {
		return value;
	}

}
