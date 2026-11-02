package io.github.apace100.apoli.action.type.bientity;

import io.github.apace100.apoli.action.ActionConfiguration;
import io.github.apace100.apoli.action.context.BiEntityActionContext;
import io.github.apace100.apoli.action.type.BiEntityActionType;
import io.github.apace100.apoli.action.type.BiEntityActionTypes;
import io.github.apace100.apoli.util.requirement.BiEntityRequirement;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.NotNull;

public class SetInLoveBiEntityActionType extends BiEntityActionType {

    @Override
    public void accept(BiEntityActionContext context) {

        if (context.target() instanceof AnimalEntity animalTarget && context.actor() instanceof PlayerEntity playerActor) {
            animalTarget.lovePlayer(playerActor);
        }

    }

    @Override
    public @NotNull ActionConfiguration<?> getConfig() {
        return BiEntityActionTypes.SET_IN_LOVE;
    }

    @Override
    public BiEntityRequirement getRequirement() {
        return BiEntityRequirement.BOTH;
    }

}
