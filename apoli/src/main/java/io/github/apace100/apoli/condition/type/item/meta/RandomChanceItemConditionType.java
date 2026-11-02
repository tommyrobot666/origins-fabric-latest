package io.github.apace100.apoli.condition.type.item.meta;

import io.github.apace100.apoli.condition.ConditionConfiguration;
import io.github.apace100.apoli.condition.context.ItemConditionContext;
import io.github.apace100.apoli.condition.type.ItemConditionType;
import io.github.apace100.apoli.condition.type.ItemConditionTypes;
import io.github.apace100.apoli.condition.type.meta.RandomChanceMetaConditionType;
import org.jetbrains.annotations.NotNull;

public class RandomChanceItemConditionType extends ItemConditionType implements RandomChanceMetaConditionType {

	private final float chance;

	public RandomChanceItemConditionType(float chance) {
		this.chance = chance;
	}

	@Override
	public boolean test(ItemConditionContext context) {
		return testCondition();
	}

	@Override
	public @NotNull ConditionConfiguration<?> getConfig() {
		return ItemConditionTypes.RANDOM_CHANCE;
	}

	@Override
	public float chance() {
		return chance;
	}

}
