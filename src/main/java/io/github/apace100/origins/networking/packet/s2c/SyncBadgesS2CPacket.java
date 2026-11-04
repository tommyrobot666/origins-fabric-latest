package io.github.apace100.origins.networking.packet.s2c;

import io.github.apace100.origins.Origins;
import io.github.apace100.origins.badge.Badge;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public record SyncBadgesS2CPacket(Map<Identifier, List<Badge>> badgesById) implements CustomPacketPayload {

    public static final Type<SyncBadgesS2CPacket> PACKET_ID = new Type<>(Origins.identifier("s2c/sync_badge_registry"));
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncBadgesS2CPacket> PACKET_CODEC = StreamCodec.of((b,p)-> p.write(b), SyncBadgesS2CPacket::read);

    public static SyncBadgesS2CPacket read(RegistryFriendlyByteBuf buf) {

        Map<Identifier, List<Badge>> badgesById = new HashMap<>();
        int entriesCount = buf.readVarInt();

        for (int i = 0; i < entriesCount; i++) {

            Identifier id = buf.readIdentifier();
            int badgesCount = buf.readVarInt();

            for (int j = 0; j < badgesCount; j++) {
                badgesById
                    .computeIfAbsent(id, k -> new LinkedList<>())
                    .add(Badge.receive(buf));
            }

        }

        return new SyncBadgesS2CPacket(badgesById);

    }

    public void write(RegistryFriendlyByteBuf buf) {

        buf.writeVarInt(badgesById.size());
        for (Map.Entry<Identifier, List<Badge>> entry : badgesById.entrySet()) {

            Identifier id = entry.getKey();
            List<Badge> badges = entry.getValue();

            buf.writeIdentifier(id);
            buf.writeVarInt(badges.size());

            for (Badge badge : badges) {
                badge.send(buf);
            }

        }

    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PACKET_ID;
    }
}
