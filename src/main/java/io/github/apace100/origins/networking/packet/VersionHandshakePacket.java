package io.github.apace100.origins.networking.packet;

import io.github.apace100.origins.Origins;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record VersionHandshakePacket(int[] semver) implements CustomPacketPayload {

    private static final StreamCodec<FriendlyByteBuf, int[]> INT_ARRAY = StreamCodec.of(FriendlyByteBuf::writeVarIntArray, FriendlyByteBuf::readVarIntArray);

    public static final Type<VersionHandshakePacket> PACKET_ID = new Type<>(Origins.identifier("handshake/version"));
    public static final StreamCodec<FriendlyByteBuf, VersionHandshakePacket> PACKET_CODEC = INT_ARRAY.map(VersionHandshakePacket::new, VersionHandshakePacket::semver);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PACKET_ID;
    }
}
