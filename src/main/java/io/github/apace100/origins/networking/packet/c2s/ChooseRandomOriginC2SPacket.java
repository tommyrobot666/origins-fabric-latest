package io.github.apace100.origins.networking.packet.c2s;

import io.github.apace100.origins.Origins;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record ChooseRandomOriginC2SPacket(Identifier layerId) implements CustomPacketPayload {

    public static final Type<ChooseRandomOriginC2SPacket> PACKET_ID = new Type<>(Origins.identifier("c2s/choose_random_origin"));
    public static final StreamCodec<ByteBuf, ChooseRandomOriginC2SPacket> PACKET_CODEC = Identifier.STREAM_CODEC.map(ChooseRandomOriginC2SPacket::new, ChooseRandomOriginC2SPacket::layerId);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PACKET_ID;
    }
}
