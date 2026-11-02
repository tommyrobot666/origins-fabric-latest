package io.github.apace100.apoli.action.type.block;

import io.github.apace100.apoli.action.ActionConfiguration;
import io.github.apace100.apoli.action.EntityAction;
import io.github.apace100.apoli.action.context.BlockActionContext;
import io.github.apace100.apoli.action.type.BlockActionType;
import io.github.apace100.apoli.action.type.BlockActionTypes;
import io.github.apace100.apoli.data.TypedDataObjectFactory;
import io.github.apace100.apoli.util.MiscUtil;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class SpawnEntityBlockActionType extends BlockActionType {

    public static final TypedDataObjectFactory<SpawnEntityBlockActionType> DATA_FACTORY = TypedDataObjectFactory.simple(
        new SerializableData()
            .add("entity_type", SerializableDataTypes.ENTITY_TYPE)
            .add("entity_action", EntityAction.DATA_TYPE.optional(), Optional.empty())
            .add("tag", SerializableDataTypes.NBT_COMPOUND, new NbtCompound()),
        data -> new SpawnEntityBlockActionType(
            data.get("entity_type"),
            data.get("entity_action"),
            data.get("tag")
        ),
        (actionType, serializableData) -> serializableData.instance()
            .set("entity_type", actionType.entityType)
            .set("entity_action", actionType.entityAction)
            .set("tag", actionType.tag)
    );

    private final EntityType<?> entityType;

    private final Optional<EntityAction> entityAction;
    private final NbtCompound tag;

    public SpawnEntityBlockActionType(EntityType<?> entityType, Optional<EntityAction> entityAction, NbtCompound tag) {
        this.entityType = entityType;
        this.entityAction = entityAction;
        this.tag = tag;
    }

    @Override
    public void accept(BlockActionContext context) {

        ServerWorld world = context.world();
        BlockPos pos = context.pos();

        MiscUtil.getEntityWithPassengers(world, entityType, tag, pos.toBottomCenterPos(), Optional.empty(), Optional.empty()).ifPresent(entity -> {
            world.spawnNewEntityAndPassengers(entity);
            entityAction.ifPresent(action -> action.execute(entity));
        });

    }

    @Override
    public @NotNull ActionConfiguration<?> getConfig() {
        return BlockActionTypes.SPAWN_ENTITY;
    }

}
