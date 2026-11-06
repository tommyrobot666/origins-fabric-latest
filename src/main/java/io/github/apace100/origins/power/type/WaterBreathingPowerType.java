package io.github.apace100.origins.power.type;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.condition.EntityCondition;
import io.github.apace100.apoli.mixin.EntityAccessor;
import io.github.apace100.apoli.power.PowerConfiguration;
import io.github.apace100.apoli.power.type.PowerType;
import io.github.apace100.origins.mixin.LivingEntityAccessor;
import io.github.apace100.origins.registry.ModDamageTypes;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class WaterBreathingPowerType extends PowerType {

    public WaterBreathingPowerType(Optional<EntityCondition> condition) {
        super(condition);
    }

    @Override
    public @NotNull PowerConfiguration<?> getConfig() {
        return OriginsPowerTypes.WATER_BREATHING;
    }

    public static boolean shouldDrown(LivingEntity entity) {
        return !entity.isEyeInFluid(FluidTags.WATER)
            && !entity.hasEffect(MobEffects.WATER_BREATHING)
            && !entity.hasEffect(MobEffects.CONDUIT_POWER);
    }

    public static void tick(LivingEntity entity) {

        if (!PowerHolderComponent.hasPowerType(entity, WaterBreathingPowerType.class)) {
            return;
        }

        LivingEntityAccessor entityAccess = (LivingEntityAccessor) entity;
        if (WaterBreathingPowerType.shouldDrown(entity)) {

            int landGain = entityAccess.callIncreaseAirSupply(0);
            int landLoss = entityAccess.callDecreaseAirSupply(entity.getAirSupply());

            if (!((EntityAccessor) entity).callIsInRain()) {

                entity.setAirSupply(landLoss - landGain);
                if (entity.getAirSupply() != -20) {
                    return;
                }

                entity.setAirSupply(0);
                entity.hurt(entity.damageSources().source(ModDamageTypes.NO_WATER_FOR_GILLS), 2.0F);

                for (int i = 0; i < 8; ++i) {

                    double dx = entity.getRandom().nextDouble() - entity.getRandom().nextDouble();
                    double dy = entity.getRandom().nextDouble() - entity.getRandom().nextDouble();
                    double dz = entity.getRandom().nextDouble() - entity.getRandom().nextDouble();

                    entity.level().addParticle(ParticleTypes.BUBBLE, entity.getRandomX(0.5), entity.getEyeHeight(entity.getPose()), entity.getRandomZ(0.5), dx * 0.5, dy * 0.5 + 0.25, dz * 0.5);

                }

            }

            else {
                entity.setAirSupply(entity.getAirSupply() - landGain);
            }

        }

        else if (entity.getAirSupply() < entity.getMaxAirSupply()) {
            entity.setAirSupply(entityAccess.callIncreaseAirSupply(entity.getAirSupply()));
        }

    }

}
