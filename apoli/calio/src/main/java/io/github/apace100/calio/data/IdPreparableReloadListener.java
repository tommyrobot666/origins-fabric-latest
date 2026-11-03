package io.github.apace100.calio.data;

import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import org.jetbrains.annotations.Nullable;

public interface IdPreparableReloadListener extends PreparableReloadListener {
    Identifier getId();
    @Nullable Identifier getBefore();
}
