package io.github.apace100.apoli.condition.type.damage.meta;

import io.github.apace100.apoli.condition.ConditionConfiguration;
import io.github.apace100.apoli.condition.DamageCondition;
import io.github.apace100.apoli.condition.context.DamageConditionContext;
import io.github.apace100.apoli.condition.type.DamageConditionType;
import io.github.apace100.apoli.condition.type.DamageConditionTypes;
import io.github.apace100.apoli.condition.type.meta.AllOfMetaConditionType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AllOfDamageConditionType extends DamageConditionType implements AllOfMetaConditionType<DamageConditionContext, DamageCondition> {

	private final List<DamageCondition> conditions;

	public AllOfDamageConditionType(List<DamageCondition> conditions) {
		this.conditions = conditions;
	}

	@Override
	public boolean test(DamageConditionContext context) {
		return testConditions(context);
	}

	@Override
	public @NotNull ConditionConfiguration<?> getConfig() {
		return DamageConditionTypes.ALL_OF;
	}

	@Override
	public List<DamageCondition> conditions() {
		return conditions;
	}

}
