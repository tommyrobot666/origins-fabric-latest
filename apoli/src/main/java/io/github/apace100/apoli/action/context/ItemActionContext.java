package io.github.apace100.apoli.action.context;

import io.github.apace100.apoli.condition.context.ItemConditionContext;
import io.github.apace100.apoli.util.context.ActionContext;
import net.minecraft.inventory.StackReference;
import net.minecraft.server.world.ServerWorld;

public record ItemActionContext(ServerWorld world, StackReference stackReference) implements ActionContext<ItemConditionContext> {

	@Override
	public ItemConditionContext forCondition() {
		return new ItemConditionContext(world(), stackReference().get());
	}

}
