package io.github.apace100.origins.registry;

import io.github.apace100.origins.Origins;
import io.github.apace100.origins.content.OrbOfOriginItem;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;


public class ModItems {

    public static final Item ORB_OF_ORIGIN;

    public static void register() {

    }

    static {
        ORB_OF_ORIGIN = Registry.register(BuiltInRegistries.ITEM, Origins.identifier("orb_of_origin"), new OrbOfOriginItem());
    }

}
