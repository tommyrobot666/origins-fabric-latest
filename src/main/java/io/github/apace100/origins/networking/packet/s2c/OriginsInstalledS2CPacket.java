package io.github.apace100.origins.networking.packet.s2c;

import io.github.apace100.origins.Origins;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public class OriginsInstalledS2CPacket implements CustomPacketPayload {

	public static final OriginsInstalledS2CPacket INSTANCE = new OriginsInstalledS2CPacket();

	public static final Type<OriginsInstalledS2CPacket> PACKET_ID = new Type<>(Origins.identifier("s2c/origins_installed"));
	public static final StreamCodec<ByteBuf, OriginsInstalledS2CPacket> PACKET_CODEC = StreamCodec.unit(INSTANCE);

	private OriginsInstalledS2CPacket() {

	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return PACKET_ID;
	}
}
