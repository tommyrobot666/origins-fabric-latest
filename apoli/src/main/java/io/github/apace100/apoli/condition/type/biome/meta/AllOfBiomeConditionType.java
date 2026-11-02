package io.github.apace100.apoli.condition.type.biome.meta;

import io.github.apace100.apoli.condition.BiomeCondition;
import io.github.apace100.apoli.condition.ConditionConfiguration;
import io.github.apace100.apoli.condition.context.BiomeConditionContext;
import io.github.apace100.apoli.condition.type.BiomeConditionType;
import io.github.apace100.apoli.condition.type.BiomeConditionTypes;
import io.github.apace100.apoli.condition.type.meta.AllOfMetaConditionType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AllOfBiomeConditionType extends BiomeConditionType implements AllOfMetaConditionType<BiomeConditionContext, BiomeCondition> {

	private final List<BiomeCondition> conditions;

	public AllOfBiomeConditionType(List<BiomeCondition> conditions) {
		this.conditions = conditions;
	}

	@Override
	public boolean test(BiomeConditionContext context) {
		return testConditions(context);
	}

	@Override
	public @NotNull ConditionConfiguration<?> getConfig() {
		return BiomeConditionTypes.ALL_OF;
	}

	@Override
	public List<BiomeCondition> conditions() {
		return conditions;
	}

}
