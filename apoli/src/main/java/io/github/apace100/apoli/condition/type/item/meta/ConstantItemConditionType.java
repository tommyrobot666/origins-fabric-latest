package io.github.apace100.apoli.condition.type.item.meta;

import io.github.apace100.apoli.condition.ConditionConfiguration;
import io.github.apace100.apoli.condition.context.ItemConditionContext;
import io.github.apace100.apoli.condition.type.ItemConditionType;
import io.github.apace100.apoli.condition.type.ItemConditionTypes;
import io.github.apace100.apoli.condition.type.meta.ConstantMetaConditionType;
import org.jetbrains.annotations.NotNull;

public class ConstantItemConditionType extends ItemConditionType implements ConstantMetaConditionType {

	private final boolean value;

	public ConstantItemConditionType(boolean value) {
		this.value = value;
	}

	@Override
	public boolean test(ItemConditionContext context) {
		return value();
	}

	@Override
	public @NotNull ConditionConfiguration<?> getConfig() {
		return ItemConditionTypes.CONSTANT;
	}

	@Override
	public boolean value() {
		return value;
	}

}
