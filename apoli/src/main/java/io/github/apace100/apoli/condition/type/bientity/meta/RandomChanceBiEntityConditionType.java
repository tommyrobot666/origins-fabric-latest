package io.github.apace100.apoli.condition.type.bientity.meta;

import io.github.apace100.apoli.condition.ConditionConfiguration;
import io.github.apace100.apoli.condition.context.BiEntityConditionContext;
import io.github.apace100.apoli.condition.type.BiEntityConditionType;
import io.github.apace100.apoli.condition.type.BiEntityConditionTypes;
import io.github.apace100.apoli.condition.type.meta.RandomChanceMetaConditionType;
import org.jetbrains.annotations.NotNull;

public class RandomChanceBiEntityConditionType extends BiEntityConditionType implements RandomChanceMetaConditionType {

	private final float chance;

	public RandomChanceBiEntityConditionType(float chance) {
		this.chance = chance;
	}

	@Override
	public boolean test(BiEntityConditionContext context) {
		return this.testCondition();
	}

	@Override
	public @NotNull ConditionConfiguration<?> getConfig() {
		return BiEntityConditionTypes.RANDOM_CHANCE;
	}

	@Override
	public float chance() {
		return chance;
	}

}
