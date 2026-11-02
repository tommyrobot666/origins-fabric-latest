package io.github.apace100.apoli.condition.type.biome.meta;

import io.github.apace100.apoli.condition.ConditionConfiguration;
import io.github.apace100.apoli.condition.context.BiomeConditionContext;
import io.github.apace100.apoli.condition.type.BiomeConditionType;
import io.github.apace100.apoli.condition.type.BiomeConditionTypes;
import io.github.apace100.apoli.condition.type.meta.RandomChanceMetaConditionType;
import org.jetbrains.annotations.NotNull;

public class RandomChanceBiomeConditionType extends BiomeConditionType implements RandomChanceMetaConditionType {

	private final float chance;

	public RandomChanceBiomeConditionType(float chance) {
		this.chance = chance;
	}

	@Override
	public boolean test(BiomeConditionContext context) {
		return testCondition();
	}

	@Override
	public @NotNull ConditionConfiguration<?> getConfig() {
		return BiomeConditionTypes.RANDOM_CHANCE;
	}

	@Override
	public float chance() {
		return chance;
	}

}
