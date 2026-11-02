package io.github.apace100.apoli.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.apace100.apoli.power.type.ActionOnItemPickupPowerType;
import io.github.apace100.apoli.power.type.PreventItemPickupPowerType;
import io.github.apace100.apoli.power.type.Prioritized;
import io.github.apace100.apoli.util.InventoryUtil;
import io.github.apace100.apoli.util.MiscUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.UUID;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity {

    @Shadow
    @Nullable
    private UUID throwerUuid;

    @Shadow
    public abstract ItemStack getStack();

    @Shadow
    public abstract void setStack(ItemStack stack);

    private ItemEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @WrapOperation(method = "onPlayerCollision", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerInventory;insertStack(Lnet/minecraft/item/ItemStack;)Z"))
    private boolean apoli$onItemPickup(PlayerInventory playerInventory, ItemStack stack, Operation<Boolean> original, PlayerEntity player) {

        if (PreventItemPickupPowerType.doesPrevent(thisAsItemEntity(), player)) {
            return false;
        }

        else if (MiscUtil.hasSpaceInInventory(playerInventory, stack)) {

            StackReference stackReference = InventoryUtil.createStackReference(stack);
            Entity thrower = MiscUtil.getEntityByUuid(this.throwerUuid, this.getServer());

            Prioritized.CallInstance<ActionOnItemPickupPowerType> callInstance = ActionOnItemPickupPowerType.executeItemAction(thrower, stackReference, player);
            this.setStack(stackReference.get());

            boolean result = original.call(playerInventory, this.getStack());
            if (result) {
                ActionOnItemPickupPowerType.executeBiEntityAction(callInstance, thrower);
            }

            return result;

        }

        else {
            return original.call(playerInventory, stack);
        }

    }

    @Unique
    private ItemEntity thisAsItemEntity() {
        return (ItemEntity) (Object) this;
    }

}
