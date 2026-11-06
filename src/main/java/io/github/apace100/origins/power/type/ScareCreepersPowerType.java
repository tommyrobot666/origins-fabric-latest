package io.github.apace100.origins.power.type;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.condition.EntityCondition;
import io.github.apace100.apoli.power.PowerConfiguration;
import io.github.apace100.apoli.power.type.PowerType;
import io.github.apace100.apoli.util.MiscUtil;
import io.github.apace100.origins.mixin.MobAccessor;
import io.github.apace100.origins.mixin.NearestAttackableTargetGoalAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

public class ScareCreepersPowerType extends PowerType {

    public ScareCreepersPowerType(Optional<EntityCondition> condition) {
        super(condition);
    }

    @Override
    public @NotNull PowerConfiguration<?> getConfig() {
        return OriginsPowerTypes.SCARE_CREEPERS;
    }

    public static void modifyGoals(PathfinderMob pathAwareEntity) {

        GoalSelector targetSelector = ((MobAccessor) pathAwareEntity).getTargetSelector();
        GoalSelector goalSelector = pathAwareEntity.getGoalSelector();

        Iterator<WrappedGoal> oldTargetPrioGoals = targetSelector.getAvailableGoals().iterator();
        Set<WrappedGoal> newTargetPrioGoals = new HashSet<>();

        while (oldTargetPrioGoals.hasNext()) {

            WrappedGoal oldTargetPrioGoal = oldTargetPrioGoals.next();
            if (!(oldTargetPrioGoal.getGoal() instanceof NearestAttackableTargetGoal oldTargetGoal)) {
                continue;
            }

            Predicate<LivingEntity> targetCondition = MiscUtil.combineAnd(e -> ((NearestAttackableTargetGoalAccessor) oldTargetGoal).getTargetConditions().test((ServerLevel) e.level(),null,e), e -> !PowerHolderComponent.hasPowerType((Entity) e, ScareCreepersPowerType.class));
            WrappedGoal newTargetPrioGoal = new WrappedGoal(oldTargetPrioGoal.getPriority(), new NearestAttackableTargetGoal<>(pathAwareEntity, ((NearestAttackableTargetGoalAccessor) oldTargetGoal).getTargetType(), ((NearestAttackableTargetGoalAccessor) oldTargetGoal).getRandomInterval(), ((NearestAttackableTargetGoalAccessor) oldTargetGoal).getMustSee(), ((NearestAttackableTargetGoalAccessor) oldTargetGoal).getMustReach(), (e, l) -> targetCondition.test(e)));

            newTargetPrioGoals.add(newTargetPrioGoal);
            oldTargetPrioGoals.remove();

        }

        goalSelector.addGoal(3, new AvoidEntityGoal<>(pathAwareEntity, LivingEntity.class, e -> PowerHolderComponent.hasPowerType(e, ScareCreepersPowerType.class), 6.0F, 1.0D, 1.2D, EntitySelector.NO_CREATIVE_OR_SPECTATOR::test));
        newTargetPrioGoals.forEach(pg -> targetSelector.addGoal(pg.getPriority(), pg.getGoal()));

    }

}
