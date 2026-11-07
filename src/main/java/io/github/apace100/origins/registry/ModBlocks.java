package io.github.apace100.origins.registry;

import io.github.apace100.origins.Origins;
import io.github.apace100.origins.content.TemporaryCobwebBlock;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

import java.util.function.Supplier;

public class ModBlocks {

    public static final TemporaryCobwebBlock TEMPORARY_COBWEB = register("temporary_cobweb", false, () -> new TemporaryCobwebBlock(BlockBehaviour.Properties.of()
        .mapColor(MapColor.COLOR_LIGHT_GRAY)
        .strength(4.0F)
            .requiresCorrectToolForDrops()
        .noCollision()
            .forceSolidOn()));

    public static void register() {

    }

    private static <B extends Block> B register(String name, boolean withBlockItem, Supplier<B> blockSupplier) {

        Identifier blockId = Origins.identifier(name);
        B block = Registry.register(BuiltInRegistries.BLOCK, blockId, blockSupplier.get());

        if (withBlockItem) {
            Registry.register(BuiltInRegistries.ITEM, blockId, new BlockItem(block, new Item.Properties()));
        }

        return block;

    }

}
