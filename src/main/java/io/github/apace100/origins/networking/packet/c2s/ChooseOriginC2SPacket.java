package io.github.apace100.origins.networking.packet.c2s;

import io.github.apace100.origins.Origins;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record ChooseOriginC2SPacket(Identifier layerId, Identifier originId) implements CustomPacketPayload {

    public static final Type<ChooseOriginC2SPacket> PACKET_ID = new Type<>(Origins.identifier("c2s/choose_origin"));
    public static final StreamCodec<ByteBuf, ChooseOriginC2SPacket> PACKET_CODEC = StreamCodec.composite(
        Identifier.STREAM_CODEC, ChooseOriginC2SPacket::layerId,
        Identifier.STREAM_CODEC, ChooseOriginC2SPacket::originId,
        ChooseOriginC2SPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PACKET_ID;
    }
}
