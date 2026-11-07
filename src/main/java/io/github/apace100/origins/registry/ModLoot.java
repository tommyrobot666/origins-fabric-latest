package io.github.apace100.origins.registry;

import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetEnchantmentsFunction;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BiFunction;

public class ModLoot {

    private static final Set<ResourceKey<LootTable>> AFFECTED_TABLES = new HashSet<>();

    private static final ResourceKey<LootTable> SIMPLE_DUNGEON = getAndAddTable("chests/simple_dungeon");
    private static final ResourceKey<LootTable> STRONGHOLD_LIBRARY = getAndAddTable("chests/stronghold_library");
    private static final ResourceKey<LootTable> MINESHAFT = getAndAddTable("chests/abandoned_mineshaft");
    private static final ResourceKey<LootTable> SMALL_UNDERWATER_RUIN = getAndAddTable("chests/underwater_ruin_small");

    private static final BiFunction<Holder<Enchantment>, Integer, SetEnchantmentsFunction.Builder> SIMPLE_ENCHANTMENT_SETTER = (enchantmentEntry, levels) ->
        new SetEnchantmentsFunction.Builder()
            .withEnchantment(enchantmentEntry, ContextIntProviders.exactly(levels));

    private static ResourceKey<LootTable> getAndAddTable(String path) {

        ResourceKey<LootTable> tableKey = ResourceKey.create(Registries.LOOT_TABLE, Identifier.withDefaultNamespace(path));
        AFFECTED_TABLES.add(tableKey);

        return tableKey;

    }

    public static void registerLootTables() {
        LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {

            if (!AFFECTED_TABLES.contains(key)) {
                return;
            }

            Holder<Enchantment> waterProtection = registries
                .lookupOrThrow(Registries.ENCHANTMENT)
                .get(ModEnchantments.WATER_PROTECTION).orElse(null);

            if (waterProtection == null) return;

            if (key.equals(SIMPLE_DUNGEON)) {
                tableBuilder.pool(new LootPool.Builder()
                    .setRolls(ContextIntProviders.exactly(1))
                        .add(LootItem.lootTableItem(Items.BOOK)
                                .setWeight(20)
                        .apply(SIMPLE_ENCHANTMENT_SETTER.apply(waterProtection, 1)))
                    .add(LootItem.lootTableItem(Items.BOOK)
                        .setWeight(10)
                        .apply(SIMPLE_ENCHANTMENT_SETTER.apply(waterProtection, 2)))
                    .add(EmptyLootItem.emptyItem()
                        .setWeight(80)).build());
            }

            else if (key.equals(STRONGHOLD_LIBRARY)) {
                tableBuilder.pool(new LootPool.Builder()
                    .setRolls(ContextIntProviders.exactly(1))
                    .add(LootItem.lootTableItem(Items.BOOK)
                        .setWeight(20)
                        .apply(SIMPLE_ENCHANTMENT_SETTER.apply(waterProtection, 2)))
                    .add(LootItem.lootTableItem(Items.BOOK)
                        .setWeight(10)
                        .apply(SIMPLE_ENCHANTMENT_SETTER.apply(waterProtection, 3)))
                    .add(EmptyLootItem.emptyItem()
                        .setWeight(80)).build());
            }

            else if (key.equals(MINESHAFT)) {
                tableBuilder.pool(new LootPool.Builder()
                    .setRolls(ContextIntProviders.exactly(1))
                    .add(LootItem.lootTableItem(Items.BOOK)
                        .setWeight(20)
                        .apply(SIMPLE_ENCHANTMENT_SETTER.apply(waterProtection, 1)))
                    .add(LootItem.lootTableItem(Items.BOOK)
                        .setWeight(5)
                        .apply(SIMPLE_ENCHANTMENT_SETTER.apply(waterProtection, 2)))
                    .add(EmptyLootItem.emptyItem()
                        .setWeight(90)).build());
            }

            else if (key.equals(SMALL_UNDERWATER_RUIN)) {
                tableBuilder.pool(new LootPool.Builder()
                    .setRolls(ContextIntProviders.exactly(1))
                    .add(LootItem.lootTableItem(Items.BOOK)
                        .setWeight(10)
                        .apply(SIMPLE_ENCHANTMENT_SETTER.apply(waterProtection, 1)))
                    .add(LootItem.lootTableItem(Items.BOOK)
                        .setWeight(20)
                        .apply(SIMPLE_ENCHANTMENT_SETTER.apply(waterProtection, 2)))
                    .add(EmptyLootItem.emptyItem()
                        .setWeight(110)).build());
            }

        });
    }

}
