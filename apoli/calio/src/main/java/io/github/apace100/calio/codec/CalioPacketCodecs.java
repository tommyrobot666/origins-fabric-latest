package io.github.apace100.calio.codec;

import io.github.apace100.calio.data.DataException;
import io.github.apace100.calio.mixin.TagEntryAccessor;
import io.github.apace100.calio.util.DynamicIdentifier;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagEntry;
import net.minecraft.util.ExtraCodecs;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.IntFunction;
import java.util.function.Supplier;

public class CalioPacketCodecs {

    public static final StreamCodec<ByteBuf, ExtraCodecs.TagOrElementLocation> TAG_ENTRY_ID = ByteBufCodecs.STRING_UTF8.map(
        str -> {

            boolean isTag = str.startsWith("#");
            String idStr = isTag
                ? str.substring(1)
                : str;

            Identifier id = DynamicIdentifier.of(idStr);
            return new ExtraCodecs.TagOrElementLocation(id, isTag);

        },
        ExtraCodecs.TagOrElementLocation::toString
    );

    public static final StreamCodec<ByteBuf, TagEntry> TAG_ENTRY = StreamCodec.composite(
        TAG_ENTRY_ID, tagEntry -> ((TagEntryAccessor) tagEntry).callGetIdForCodec(),
        ByteBufCodecs.BOOL, tagEntry -> ((TagEntryAccessor) tagEntry).isRequired(),
        TagEntry::new
    );

    public static final StreamCodec<ByteBuf, Set<TagEntry>> TAG_ENTRY_SET = collection(HashSet::new, () -> CalioPacketCodecs.TAG_ENTRY);

    public static <B extends ByteBuf, E, C extends Collection<E>> StreamCodec<B, C> collection(IntFunction<C> collectionFactory, Supplier<StreamCodec<? super B, E>> elementCodec) {
        return collection(collectionFactory, elementCodec, Integer.MAX_VALUE);
    }

    public static <B extends ByteBuf, E, C extends Collection<E>> StreamCodec<B, C> collection(IntFunction<C> collectionFactory, Supplier<StreamCodec<? super B, E>> elementCodec, int maxSize) {
        return StreamCodec.of(
            (buf, elements) -> {

                ByteBufCodecs.writeCount(buf, elements.size(), maxSize);
                int index = 0;

                for (E element : elements) {

                    try {
                        elementCodec.get().encode(buf, element);
                        index++;
                    }

                    catch (DataException de) {
                        throw de.prependArray(index);
                    }

                    catch (Exception e) {
                        throw new DataException(DataException.Phase.SENDING, index, e);
                    }

                }

            },
            buf -> {

                int size = ByteBufCodecs.readCount(buf, maxSize);
                C elements = collectionFactory.apply(Math.min(size, 65536));

                for (int index = 0; index < size; index++) {

                    try {
                        elements.add(elementCodec.get().decode(buf));
                    }

                    catch (DataException de) {
                        throw de.prependArray(index);
                    }

                    catch (Exception e) {
                        throw new DataException(DataException.Phase.RECEIVING, index, e);
                    }

                }

                return elements;

            }
        );
    }

}
