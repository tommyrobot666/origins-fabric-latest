package io.github.apace100.origins.registry;

import io.github.apace100.origins.Origins;
import io.github.apace100.origins.component.item.ItemOriginsComponent;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;

public class ModDataComponentTypes {

    public static final DataComponentType<ItemOriginsComponent> ORIGIN = DataComponentType.<ItemOriginsComponent>builder()
        .persistent(ItemOriginsComponent.CODEC)
        .networkSynchronized(ItemOriginsComponent.PACKET_CODEC)
        .build();

    public static void register() {
        Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, Origins.identifier("origin"), ORIGIN);
    }

}
