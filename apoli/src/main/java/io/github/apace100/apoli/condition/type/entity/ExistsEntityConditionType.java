package io.github.apace100.apoli.condition.type.entity;

import io.github.apace100.apoli.condition.ConditionConfiguration;
import io.github.apace100.apoli.condition.context.EntityConditionContext;
import io.github.apace100.apoli.condition.type.EntityConditionType;
import io.github.apace100.apoli.condition.type.EntityConditionTypes;
import org.jetbrains.annotations.NotNull;

public class ExistsEntityConditionType extends EntityConditionType {

	@Override
	public boolean test(EntityConditionContext context) {
		return super.shouldTest(context);
	}

	@Override
	public boolean shouldTest(EntityConditionContext context) {
		return true;
	}

	@Override
	public @NotNull ConditionConfiguration<?> getConfig() {
		return EntityConditionTypes.EXISTS;
	}

}
