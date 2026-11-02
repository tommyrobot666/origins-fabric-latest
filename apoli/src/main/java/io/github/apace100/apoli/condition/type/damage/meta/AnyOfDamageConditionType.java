package io.github.apace100.apoli.condition.type.damage.meta;

import io.github.apace100.apoli.condition.ConditionConfiguration;
import io.github.apace100.apoli.condition.DamageCondition;
import io.github.apace100.apoli.condition.context.DamageConditionContext;
import io.github.apace100.apoli.condition.type.DamageConditionType;
import io.github.apace100.apoli.condition.type.DamageConditionTypes;
import io.github.apace100.apoli.condition.type.meta.AnyOfMetaConditionType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AnyOfDamageConditionType extends DamageConditionType implements AnyOfMetaConditionType<DamageConditionContext, DamageCondition> {

	private final List<DamageCondition> conditions;

	public AnyOfDamageConditionType(List<DamageCondition> conditions) {
		this.conditions = conditions;
	}

	@Override
	public boolean test(DamageConditionContext context) {
		return testConditions(context);
	}

	@Override
	public @NotNull ConditionConfiguration<?> getConfig() {
		return DamageConditionTypes.ANY_OF;
	}

	@Override
	public List<DamageCondition> conditions() {
		return conditions;
	}

}
