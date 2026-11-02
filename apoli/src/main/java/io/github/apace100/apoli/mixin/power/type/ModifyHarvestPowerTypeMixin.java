package io.github.apace100.apoli.mixin.power.type;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import io.github.apace100.apoli.Apoli;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.type.ModifyHarvestPowerType;
import io.github.apace100.apoli.util.SavedBlockPosition;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.resource.featuretoggle.ToggleableFeature;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public abstract class ModifyHarvestPowerTypeMixin {

	@Mixin(AbstractBlock.class)
	public abstract static class BlockBreakingDeltaProxy implements ToggleableFeature {

		@WrapOperation(method = "calcBlockBreakingDelta", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;canHarvest(Lnet/minecraft/block/BlockState;)Z"))
		private boolean apoli$modifyHarvest(PlayerEntity player, BlockState state, Operation<Boolean> original, BlockState mState, PlayerEntity mPlayer, BlockView world, BlockPos pos) {
			return PowerHolderComponent.getPowerTypes(player, ModifyHarvestPowerType.class)
				.stream()
				.filter(powerType -> powerType.doesApply(world, pos))
				.max(ModifyHarvestPowerType::compareTo)
				.map(ModifyHarvestPowerType::isAllowed)
				.orElseGet(() -> original.call(player, state));
		}

	}

	@Mixin(ServerPlayerInteractionManager.class)
	public abstract static class HarvestabilityProxy {

		@Shadow
		protected ServerWorld world;

		@Shadow
		@Final
		protected ServerPlayerEntity player;

		@Inject(method = "tryBreakBlock", at = @At("HEAD"))
		private void apoli$cacheBreakingBlock(BlockPos pos, CallbackInfoReturnable<Boolean> cir, @Share(value = "breakingBlock", namespace = Apoli.MODID) LocalRef<SavedBlockPosition> breakingBlockRef) {
			breakingBlockRef.set(new SavedBlockPosition(this.world, pos));
		}

		@WrapOperation(method = "tryBreakBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;canHarvest(Lnet/minecraft/block/BlockState;)Z"))
		private boolean apoli$modifyHarvest(ServerPlayerEntity player, BlockState state, Operation<Boolean> original, @Share(value = "breakingBlock", namespace = Apoli.MODID) LocalRef<SavedBlockPosition> breakingBlockRef, @Share(value = "modifiedHarvest", namespace = Apoli.MODID) LocalBooleanRef modifiedHarvestRef) {

			boolean result = PowerHolderComponent.getPowerTypes(this.player, ModifyHarvestPowerType.class)
				.stream()
				.filter(powerType -> powerType.doesApply(breakingBlockRef.get()))
				.max(ModifyHarvestPowerType::compareTo)
				.map(ModifyHarvestPowerType::isAllowed)
				.orElseGet(() -> original.call(player, state));

			modifiedHarvestRef.set(result);
			return result;

		}

	}

}
