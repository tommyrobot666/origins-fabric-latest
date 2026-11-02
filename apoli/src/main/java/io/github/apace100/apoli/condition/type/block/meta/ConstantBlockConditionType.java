package io.github.apace100.apoli.condition.type.block.meta;

import io.github.apace100.apoli.condition.ConditionConfiguration;
import io.github.apace100.apoli.condition.context.BlockConditionContext;
import io.github.apace100.apoli.condition.type.BlockConditionType;
import io.github.apace100.apoli.condition.type.BlockConditionTypes;
import io.github.apace100.apoli.condition.type.meta.ConstantMetaConditionType;
import org.jetbrains.annotations.NotNull;

public class ConstantBlockConditionType extends BlockConditionType implements ConstantMetaConditionType {

	private final boolean value;

	public ConstantBlockConditionType(boolean value) {
		this.value = value;
	}

	@Override
	public boolean test(BlockConditionContext context) {
		return value();
	}

	@Override
	public @NotNull ConditionConfiguration<?> getConfig() {
		return BlockConditionTypes.CONSTANT;
	}

	@Override
	public boolean value() {
		return value;
	}

}
