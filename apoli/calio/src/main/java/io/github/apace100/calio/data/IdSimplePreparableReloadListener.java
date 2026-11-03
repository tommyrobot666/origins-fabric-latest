package io.github.apace100.calio.data;

import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;

public abstract class IdSimplePreparableReloadListener<T> extends SimplePreparableReloadListener<T> {
    abstract Identifier getId();
    abstract Identifier getBefore();
}
