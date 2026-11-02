package io.github.apace100.apoli.action.type.item;

import io.github.apace100.apoli.access.EntityLinkedItemStack;
import io.github.apace100.apoli.action.ActionConfiguration;
import io.github.apace100.apoli.action.context.ItemActionContext;
import io.github.apace100.apoli.action.type.ItemActionType;
import io.github.apace100.apoli.action.type.ItemActionTypes;
import io.github.apace100.apoli.data.TypedDataObjectFactory;
import io.github.apace100.apoli.loot.context.ApoliLootContextTypes;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.function.LootFunction;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class ModifyItemActionType extends ItemActionType {

    public static final TypedDataObjectFactory<ModifyItemActionType> DATA_FACTORY = TypedDataObjectFactory.simple(
        new SerializableData()
            .add("modifier", SerializableDataTypes.ITEM_MODIFIER),
        data -> new ModifyItemActionType(
            data.get("modifier")
        ),
        (actionType, serializableData) -> serializableData.instance()
            .set("modifier", actionType.modifier)
    );

    private final RegistryKey<LootFunction> modifier;

    public ModifyItemActionType(RegistryKey<LootFunction> modifier) {
        this.modifier = modifier;
    }

    @Override
    public void accept(ItemActionContext context) {

        ServerWorld world = context.world();
        StackReference stackReference = context.stackReference();

        ItemStack oldStack = stackReference.get();
        LootFunction itemModifier = world.getServer().getReloadableRegistries()
            .getRegistryManager()
            .get(RegistryKeys.ITEM_MODIFIER)
            .getOrThrow(modifier);

        LootContextParameterSet lootContextParameterSet = new LootContextParameterSet.Builder(world)
            .add(LootContextParameters.ORIGIN, world.getSpawnPos().toCenterPos())
            .add(LootContextParameters.TOOL, oldStack)
            .addOptional(LootContextParameters.THIS_ENTITY, ((EntityLinkedItemStack) oldStack).apoli$getEntity())
            .build(ApoliLootContextTypes.ANY);

        ItemStack newStack = itemModifier.apply(oldStack, new LootContext.Builder(lootContextParameterSet).build(Optional.empty()));
        stackReference.set(newStack);

    }

    @Override
    public @NotNull ActionConfiguration<?> getConfig() {
        return ItemActionTypes.MODIFY;
    }

}
