package io.github.apace100.apoli.mixin.power.type;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.type.GameEventListenerPowerType;
import net.minecraft.entity.Entity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.event.Vibrations;
import net.minecraft.world.event.listener.EntityGameEventHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BiConsumer;

public abstract class GameEventListenerPowerTypeMixin {

	@Mixin(Vibrations.Callback.class)
	public interface CustomCallbackHandler {

		@WrapOperation(method = "canAccept", at = @At(value = "INVOKE", target = "Lnet/minecraft/registry/entry/RegistryEntry;isIn(Lnet/minecraft/registry/tag/TagKey;)Z", ordinal = 0))
		private boolean apoli$acceptsGameEvent(RegistryEntry<GameEvent> gameEvent, TagKey<GameEvent> gameEventTag, Operation<Boolean> original) {

			if ((Vibrations.Callback) this instanceof GameEventListenerPowerType.Callback powerCallback) {
				return powerCallback.containsEvent(gameEvent);
			}

			else {
				return original.call(gameEvent, gameEventTag);
			}

		}

	}

	@Mixin(Vibrations.Ticker.class)
	public interface ParticleAppearanceHandler {

		@WrapWithCondition(method = "method_51408", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;spawnParticles(Lnet/minecraft/particle/ParticleEffect;DDDIDDDD)I"))
		private static boolean apoli$onlyShowParticleWhenSpecified(ServerWorld world, ParticleEffect particle, double x, double y, double z, int count, double deltaX, double deltaY, double deltaZ, double speed, Vibrations.ListenerData listenerData) {

			if (listenerData instanceof GameEventListenerPowerType.ListenerData powerListenerData) {
				return powerListenerData.shouldShowParticle();
			}

			else {
				return true;
			}

		}

	}

	@Mixin(Entity.class)
	public static abstract class EventHandlerUpdater {

		@Shadow
		public abstract World getWorld();

		@Inject(method = "updateEventHandler", at = @At("HEAD"))
		private void apoli$update(BiConsumer<EntityGameEventHandler<?>, ServerWorld> callback, CallbackInfo ci) {

			if (getWorld() instanceof ServerWorld serverWorld) {
				PowerHolderComponent.getPowerTypes((Entity) (Object) this, GameEventListenerPowerType.class, true)
					.stream()
					.map(GameEventListenerPowerType::getGameEventHandler)
					.forEach(listener -> callback.accept(listener, serverWorld));
			}

		}

	}

}
