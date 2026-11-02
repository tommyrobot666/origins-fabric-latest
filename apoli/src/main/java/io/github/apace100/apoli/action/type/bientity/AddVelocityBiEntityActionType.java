package io.github.apace100.apoli.action.type.bientity;

import io.github.apace100.apoli.action.ActionConfiguration;
import io.github.apace100.apoli.action.context.BiEntityActionContext;
import io.github.apace100.apoli.action.type.BiEntityActionType;
import io.github.apace100.apoli.action.type.BiEntityActionTypes;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.data.TypedDataObjectFactory;
import io.github.apace100.apoli.util.Space;
import io.github.apace100.apoli.util.requirement.BiEntityRequirement;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataType;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.apache.commons.lang3.function.TriConsumer;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import java.util.function.BiFunction;

public class AddVelocityBiEntityActionType extends BiEntityActionType {

    public static final TypedDataObjectFactory<AddVelocityBiEntityActionType> DATA_FACTORY = TypedDataObjectFactory.simple(
        new SerializableData()
            .add("x", SerializableDataTypes.FLOAT, 0F)
            .add("y", SerializableDataTypes.FLOAT, 0F)
            .add("z", SerializableDataTypes.FLOAT, 0F)
            .addFunctionedDefault("velocity", ApoliDataTypes.VECTOR_3_FLOAT, data -> new Vector3f(data.getFloat("x"), data.getFloat("y"), data.getFloat("z")))
            .add("reference", SerializableDataType.enumValue(Reference.class), Reference.POSITION)
            .add("set", SerializableDataTypes.BOOLEAN, false),
        data -> new AddVelocityBiEntityActionType(
            data.get("velocity"),
            data.get("reference"),
            data.get("set")
        ),
        (actionType, serializableData) -> serializableData.instance()
            .set("velocity", actionType.velocity)
            .set("reference", actionType.reference)
            .set("set", actionType.set)
    );

    private final Vector3f velocity;
    private final Reference reference;

    private final boolean set;

    public AddVelocityBiEntityActionType(Vector3f velocity, Reference reference, boolean set) {
        this.velocity = velocity;
        this.reference = reference;
        this.set = set;
    }

    @Override
    public void accept(BiEntityActionContext context) {

        Entity actor = context.actor();
        Entity target = context.target();

        Vector3f velocityCopy = new Vector3f(velocity);
        TriConsumer<Float, Float, Float> method = set
            ? target::setVelocity
            : target::addVelocity;

        Vec3d referenceVec = reference.apply(actor, target);
        Space.transformVectorToBase(referenceVec, velocityCopy, actor.getYaw(), true);  //  Vector normalized by method

        method.accept(velocityCopy.x(), velocityCopy.y(), velocityCopy.z());
        target.velocityModified = true;

    }

    @Override
    public @NotNull ActionConfiguration<?> getConfig() {
        return BiEntityActionTypes.ADD_VELOCITY;
    }

    @Override
    public BiEntityRequirement getRequirement() {
        return BiEntityRequirement.BOTH;
    }

    public enum Reference implements BiFunction<Entity, Entity, Vec3d> {

        POSITION {

            @Override
            public Vec3d apply(Entity actor, Entity target) {
                return target.getPos().subtract(actor.getPos());
            }

        },

        ROTATION {

            @Override
            public Vec3d apply(Entity actor, Entity target) {

                float pitch = actor.getPitch();
                float yaw = actor.getYaw();

                float i = 0.017453292F;

                float j = -MathHelper.sin(yaw * i) * MathHelper.cos(pitch * i);
                float k = -MathHelper.sin(pitch * i);
                float l =  MathHelper.cos(yaw * i) * MathHelper.cos(pitch * i);

                return new Vec3d(j, k, l);

            }

        }

    }

}
