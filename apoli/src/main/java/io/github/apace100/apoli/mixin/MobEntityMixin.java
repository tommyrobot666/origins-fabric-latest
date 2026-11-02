package io.github.apace100.apoli.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.apace100.apoli.power.type.ActionOnItemPickupPowerType;
import io.github.apace100.apoli.power.type.PreventItemPickupPowerType;
import io.github.apace100.apoli.power.type.Prioritized;
import io.github.apace100.apoli.util.InventoryUtil;
import io.github.apace100.apoli.util.MiscUtil;
import net.minecraft.entity.*;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin extends LivingEntity implements Targeter {

    private MobEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @WrapWithCondition(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/MobEntity;loot(Lnet/minecraft/entity/ItemEntity;)V"))
    private boolean apoli$preventItemPickup(MobEntity mobEntity, ItemEntity itemEntity) {
        return !PreventItemPickupPowerType.doesPrevent(itemEntity, this);
    }

    @WrapOperation(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/MobEntity;loot(Lnet/minecraft/entity/ItemEntity;)V"))
    private void apoli$actionOnItemPickup(MobEntity mobEntity, ItemEntity itemEntity, Operation<Void> original) {

        StackReference stackReference = InventoryUtil.createStackReference(itemEntity.getStack());
        Entity thrower = MiscUtil.getEntityByUuid(((ItemEntityAccessor) itemEntity).getThrowerUuid(), this.getServer());

        Prioritized.CallInstance<ActionOnItemPickupPowerType> callInstance = ActionOnItemPickupPowerType.executeItemAction(thrower, stackReference, this);
        itemEntity.setStack(stackReference.get());

        original.call(mobEntity, itemEntity);
        ActionOnItemPickupPowerType.executeBiEntityAction(callInstance, thrower);

    }

}
