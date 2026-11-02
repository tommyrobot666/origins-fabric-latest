package io.github.apace100.apoli.condition.type.item.meta;

import io.github.apace100.apoli.condition.ConditionConfiguration;
import io.github.apace100.apoli.condition.ItemCondition;
import io.github.apace100.apoli.condition.context.ItemConditionContext;
import io.github.apace100.apoli.condition.type.ItemConditionType;
import io.github.apace100.apoli.condition.type.ItemConditionTypes;
import io.github.apace100.apoli.condition.type.meta.AllOfMetaConditionType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AllOfItemConditionType extends ItemConditionType implements AllOfMetaConditionType<ItemConditionContext, ItemCondition> {

	private final List<ItemCondition> conditions;

	public AllOfItemConditionType(List<ItemCondition> conditions) {
		this.conditions = conditions;
	}

	@Override
	public boolean test(ItemConditionContext context) {
		return testConditions(context);
	}

	@Override
	public @NotNull ConditionConfiguration<?> getConfig() {
		return ItemConditionTypes.ALL_OF;
	}

	@Override
	public List<ItemCondition> conditions() {
		return conditions;
	}

}
