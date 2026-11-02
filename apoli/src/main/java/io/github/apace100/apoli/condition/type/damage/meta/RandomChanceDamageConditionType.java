package io.github.apace100.apoli.condition.type.damage.meta;

import io.github.apace100.apoli.condition.ConditionConfiguration;
import io.github.apace100.apoli.condition.context.DamageConditionContext;
import io.github.apace100.apoli.condition.type.DamageConditionType;
import io.github.apace100.apoli.condition.type.DamageConditionTypes;
import io.github.apace100.apoli.condition.type.meta.RandomChanceMetaConditionType;
import org.jetbrains.annotations.NotNull;

public class RandomChanceDamageConditionType extends DamageConditionType implements RandomChanceMetaConditionType {

	private final float chance;

	public RandomChanceDamageConditionType(float chance) {
		this.chance = chance;
	}

	@Override
	public boolean test(DamageConditionContext context) {
		return testCondition();
	}

	@Override
	public @NotNull ConditionConfiguration<?> getConfig() {
		return DamageConditionTypes.RANDOM_CHANCE;
	}

	@Override
	public float chance() {
		return chance;
	}

}
