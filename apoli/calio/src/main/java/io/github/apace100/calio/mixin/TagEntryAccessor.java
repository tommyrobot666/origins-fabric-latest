package io.github.apace100.calio.mixin;

import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagEntry;
import net.minecraft.util.ExtraCodecs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(TagEntry.class)
public interface TagEntryAccessor {

    @Invoker
    ExtraCodecs.TagOrElementLocation callGetIdForCodec();

    @Accessor
    Identifier getId();

    @Accessor
    boolean isTag();

    @Accessor
    boolean isRequired();

}
