package io.github.apace100.apoli.condition.type.damage.meta;

import io.github.apace100.apoli.condition.ConditionConfiguration;
import io.github.apace100.apoli.condition.context.DamageConditionContext;
import io.github.apace100.apoli.condition.type.DamageConditionType;
import io.github.apace100.apoli.condition.type.DamageConditionTypes;
import io.github.apace100.apoli.condition.type.meta.ConstantMetaConditionType;
import org.jetbrains.annotations.NotNull;

public class ConstantDamageConditionType extends DamageConditionType implements ConstantMetaConditionType {

	private final boolean value;

	public ConstantDamageConditionType(boolean value) {
		this.value = value;
	}

	@Override
	public boolean test(DamageConditionContext context) {
		return value();
	}

	@Override
	public @NotNull ConditionConfiguration<?> getConfig() {
		return DamageConditionTypes.CONSTANT;
	}

	@Override
	public boolean value() {
		return value;
	}

}
