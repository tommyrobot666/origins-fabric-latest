package io.github.apace100.origins.badge;

import io.github.apace100.apoli.power.Power;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.registry.DataObject;
import io.github.apace100.calio.registry.DataObjectFactory;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.Identifier;

import java.util.List;

public interface Badge extends DataObject<Badge> {

	int SIZE = 9;

	Identifier spriteId();
    
    boolean hasTooltip();
    
    @Environment(EnvType.CLIENT)
    List<ClientTooltipComponent> getTooltipComponents(Power power, int widthLimit, float time, Font textRenderer);

    SerializableData.Instance toData(SerializableData.Instance instance);

    BadgeFactory getBadgeFactory();

    @Override
    default DataObjectFactory<Badge> getFactory() {
        return this.getBadgeFactory();
    }

    static Badge receive(RegistryFriendlyByteBuf buf) {
        return BadgeManager.REGISTRY.receiveDataObject(buf);
    }

    default void send(RegistryFriendlyByteBuf buf) {

        DataObjectFactory<Badge> factory = this.getFactory();
        buf.writeIdentifier(this.getBadgeFactory().id());

        factory.getSerializableData().send(buf, factory.toData(this));

    }
    
}
