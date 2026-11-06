package io.github.apace100.origins.networking.packet.s2c;

import io.github.apace100.origins.Origins;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record ConfirmOriginS2CPacket(Identifier layerId, Identifier originId) implements CustomPacketPayload {

    public static final Type<ConfirmOriginS2CPacket> PACKET_ID = new Type<>(Origins.identifier("s2c/confirm_origin"));
    public static final StreamCodec<FriendlyByteBuf, ConfirmOriginS2CPacket> PACKET_CODEC = StreamCodec.composite(
        Identifier.STREAM_CODEC, ConfirmOriginS2CPacket::layerId,
        Identifier.STREAM_CODEC, ConfirmOriginS2CPacket::originId,
        ConfirmOriginS2CPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PACKET_ID;
    }
}
