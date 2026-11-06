package io.github.apace100.origins.networking.packet.s2c;

import io.github.apace100.origins.Origins;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jspecify.annotations.NonNull;

public record OpenChooseOriginScreenS2CPacket(boolean showBackground) implements CustomPacketPayload {

    public static final Type<OpenChooseOriginScreenS2CPacket> PACKET_ID = new Type<>(Origins.identifier("s2c/open_origin_screen"));
    public static final StreamCodec<ByteBuf, OpenChooseOriginScreenS2CPacket> PACKET_CODEC = ByteBufCodecs.BOOL.map(OpenChooseOriginScreenS2CPacket::new, OpenChooseOriginScreenS2CPacket::showBackground);

    @Override
    public @NonNull Type<? extends CustomPacketPayload> type() {
        return PACKET_ID;
    }
}
