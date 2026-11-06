package io.github.apace100.origins.networking.packet.s2c;

import io.github.apace100.origins.Origins;
import io.github.apace100.origins.origin.Origin;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public record SyncOriginsS2CPacket(Map<Identifier, Origin> originsById) implements CustomPacketPayload {

    public static final Type<SyncOriginsS2CPacket> PACKET_ID = new Type<>(Origins.identifier("s2c/sync_origin_registry"));
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncOriginsS2CPacket> PACKET_CODEC = StreamCodec.of((b,p) -> p.write(b), SyncOriginsS2CPacket::read);

    public static SyncOriginsS2CPacket read(RegistryFriendlyByteBuf buf) {

        try {

            Collection<Origin> origins = new ObjectArrayList<>();
            int originsCount = buf.readVarInt();

            for (int i = 0; i < originsCount; i++) {
                origins.add(Origin.DATA_TYPE.receive(buf));
            }

            return new SyncOriginsS2CPacket(origins
                .stream()
                .collect(Collectors.toMap(Origin::getId, Function.identity(), (oldOrigin, newOrigin) -> newOrigin, Object2ObjectOpenHashMap::new)));

        }

        catch (Exception e) {
            Origins.LOGGER.error(e.getMessage());
            throw e;
        }

    }

    public void write(RegistryFriendlyByteBuf buf) {

        Collection<Origin> origins = this.originsById().values();
        buf.writeVarInt(origins.size());

        origins.forEach(origin -> Origin.DATA_TYPE.send(buf, origin));

    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PACKET_ID;
    }
}
