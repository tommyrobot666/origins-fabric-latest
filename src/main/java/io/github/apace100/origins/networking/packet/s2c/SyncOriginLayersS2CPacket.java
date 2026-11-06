package io.github.apace100.origins.networking.packet.s2c;

import io.github.apace100.origins.Origins;
import io.github.apace100.origins.origin.OriginLayer;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public record SyncOriginLayersS2CPacket(Map<Identifier, OriginLayer> layersById) implements CustomPacketPayload {

    public static final Type<SyncOriginLayersS2CPacket> PACKET_ID = new Type<>(Origins.identifier("s2c/sync_origin_layer_registry"));
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncOriginLayersS2CPacket> PACKET_CODEC = StreamCodec.of((b,p) -> p.write(b), SyncOriginLayersS2CPacket::read);

    public static SyncOriginLayersS2CPacket read(RegistryFriendlyByteBuf buf) {

        Map<Identifier, OriginLayer> layersById = new HashMap<>();
        int count = buf.readVarInt();

        for (int i = 0; i < count; i++) {

            try {
                OriginLayer layer = OriginLayer.DATA_TYPE.receive(buf);
                layersById.put(layer.getId(), layer);
            }

            catch (Exception e) {
                Origins.LOGGER.error(e);
                throw e;
            }

        }

        return new SyncOriginLayersS2CPacket(layersById);

    }

    public void write(RegistryFriendlyByteBuf buf) {

        Collection<OriginLayer> layers = layersById.values();

        buf.writeVarInt(layers.size());
        layers.forEach(layer -> OriginLayer.DATA_TYPE.send(buf, layer));

    }

    @Override
    public @NonNull Type<? extends CustomPacketPayload> type() {
        return PACKET_ID;
    }
}
