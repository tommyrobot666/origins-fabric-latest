package io.github.apace100.apoli.action.context;

import io.github.apace100.apoli.condition.context.BlockConditionContext;
import io.github.apace100.apoli.util.context.ActionContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.Optional;

public record BlockActionContext(ServerWorld world, BlockPos pos, Optional<Direction> direction) implements ActionContext<BlockConditionContext> {

	@Override
	public BlockConditionContext forCondition() {
		return new BlockConditionContext(world(), pos());
	}

}
