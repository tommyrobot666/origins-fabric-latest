package io.github.apace100.apoli.condition.type.bientity;

import io.github.apace100.apoli.condition.ConditionConfiguration;
import io.github.apace100.apoli.condition.context.BiEntityConditionContext;
import io.github.apace100.apoli.condition.type.BiEntityConditionType;
import io.github.apace100.apoli.condition.type.BiEntityConditionTypes;
import io.github.apace100.apoli.util.requirement.BiEntityRequirement;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class RidingBiEntityConditionType extends BiEntityConditionType {

    @Override
    public boolean test(BiEntityConditionContext context) {
        return Objects.equals(context.actor().getVehicle(), context.target());
    }

    @Override
    public BiEntityRequirement getRequirement() {
        return BiEntityRequirement.BOTH;
    }

    @Override
    public @NotNull ConditionConfiguration<?> getConfig() {
        return BiEntityConditionTypes.RIDING;
    }

}
