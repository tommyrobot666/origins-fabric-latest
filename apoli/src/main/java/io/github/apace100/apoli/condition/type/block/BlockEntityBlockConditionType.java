package io.github.apace100.apoli.condition.type.block;

import io.github.apace100.apoli.condition.ConditionConfiguration;
import io.github.apace100.apoli.condition.context.BlockConditionContext;
import io.github.apace100.apoli.condition.type.BlockConditionType;
import io.github.apace100.apoli.condition.type.BlockConditionTypes;
import org.jetbrains.annotations.NotNull;

public class BlockEntityBlockConditionType extends BlockConditionType {

	@Override
	public boolean test(BlockConditionContext context) {
		return context.blockEntity().isPresent();
	}

	@Override
	public @NotNull ConditionConfiguration<?> getConfig() {
		return BlockConditionTypes.BLOCK_ENTITY;
	}

}
