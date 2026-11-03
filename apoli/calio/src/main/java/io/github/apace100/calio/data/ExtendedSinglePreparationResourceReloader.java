package io.github.apace100.calio.data;

import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.Profiler;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public abstract class ExtendedSinglePreparationResourceReloader<T> extends IdSimplePreparableReloadListener<T> {

    @Override
    public final @NonNull CompletableFuture<Void> reload(final PreparableReloadListener.@NonNull SharedState currentReload, final @NonNull Executor taskExecutor, final PreparableReloadListener.@NonNull PreparationBarrier preparationBarrier, final @NonNull Executor reloadExecutor) {
        return CompletableFuture.supplyAsync(() -> this.prepare(currentReload.resourceManager(), null), taskExecutor)
                .thenCompose(preparationBarrier::wait)
                .thenAcceptAsync(prepared -> this.processBeforeApply(prepared, currentReload.resourceManager(), null), reloadExecutor);
//        return CompletableFuture.supplyAsync(() -> this.prepare(manager, prepareProfiler), prepareExecutor)
//            .thenCompose(synchronizer::whenPrepared)
//            .thenAcceptAsync(prepared -> this.processBeforeApply(prepared, manager, applyProfiler), applyExecutor);
    }

    protected final void processBeforeApply(T prepared, ResourceManager manager, Profiler profiler) {
        this.preApply(prepared, manager, profiler);
        this.apply(prepared, manager, null);//profiler);
    }

    protected void preApply(T prepared, ResourceManager manager, Profiler profiler) {

    }

}
