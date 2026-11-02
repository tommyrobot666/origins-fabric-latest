package io.github.apace100.apoli.condition.type.biome.meta;

import io.github.apace100.apoli.condition.BiomeCondition;
import io.github.apace100.apoli.condition.ConditionConfiguration;
import io.github.apace100.apoli.condition.context.BiomeConditionContext;
import io.github.apace100.apoli.condition.type.BiomeConditionType;
import io.github.apace100.apoli.condition.type.BiomeConditionTypes;
import io.github.apace100.apoli.condition.type.meta.AnyOfMetaConditionType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AnyOfBiomeConditionType extends BiomeConditionType implements AnyOfMetaConditionType<BiomeConditionContext, BiomeCondition> {

	private final List<BiomeCondition> conditions;

	public AnyOfBiomeConditionType(List<BiomeCondition> conditions) {
		this.conditions = conditions;
	}

	@Override
	public boolean test(BiomeConditionContext context) {
		return testConditions(context);
	}

	@Override
	public @NotNull ConditionConfiguration<?> getConfig() {
		return BiomeConditionTypes.ANY_OF;
	}

	@Override
	public List<BiomeCondition> conditions() {
		return conditions;
	}

}
