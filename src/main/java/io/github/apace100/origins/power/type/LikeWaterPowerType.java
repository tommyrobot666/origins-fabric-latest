package io.github.apace100.origins.power.type;

import com.google.common.collect.Iterables;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.condition.EntityCondition;
import io.github.apace100.apoli.power.PowerConfiguration;
import io.github.apace100.apoli.power.type.PowerType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class LikeWaterPowerType extends PowerType {

    public LikeWaterPowerType(Optional<EntityCondition> condition) {
        super(condition);
    }

    @Override
    public @NotNull PowerConfiguration<?> getConfig() {
        return OriginsPowerTypes.LIKE_WATER;
    }

    public static Vec3 modify(Entity entity, Vec3 motion) {
        return doesApply(entity, motion)
            ? new Vec3(motion.x(), 0, motion.z())
            : motion;
    }

    private static boolean doesApply(Entity entity, Vec3 motion) {
        return Math.abs(motion.y()) < 0.025
            && Iterables.isEmpty(entity.level().getBlockCollisions(entity, entity.getBoundingBox().expandTowards(0.0, motion.y(), 0.0)))
            && PowerHolderComponent.hasPowerType(entity, LikeWaterPowerType.class);
    }

}
