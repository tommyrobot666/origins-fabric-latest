package io.github.apace100.calio.data;

import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import org.jetbrains.annotations.Nullable;

public interface IdPreparableReloadListener extends PreparableReloadListener {
    Identifier getId();
    @Nullable Identifier getBefore();

    static void registerReloadListener(PackType packType, IdPreparableReloadListener listener){
        ResourceLoader.get(packType).registerReloadListener(listener.getId(),listener);
        if (listener.getBefore() != null) {
            ResourceLoader.get(packType).addListenerOrdering(listener.getBefore(), listener.getId());
        }
    }

    static void registerReloadListener(PackType packType, IdSimplePreparableReloadListener<?> listener){
        ResourceLoader.get(packType).registerReloadListener(listener.getId(),listener);
        if (listener.getBefore() != null) {
            ResourceLoader.get(packType).addListenerOrdering(listener.getBefore(), listener.getId());
        }
    }
}
