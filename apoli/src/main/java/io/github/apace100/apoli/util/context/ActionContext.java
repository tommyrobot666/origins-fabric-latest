package io.github.apace100.apoli.util.context;

public interface ActionContext<C extends ConditionContext> {

	C forCondition();

}
