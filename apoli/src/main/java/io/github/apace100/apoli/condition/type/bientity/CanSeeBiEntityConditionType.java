package io.github.apace100.apoli.condition.type.bientity;

import io.github.apace100.apoli.condition.ConditionConfiguration;
import io.github.apace100.apoli.condition.context.BiEntityConditionContext;
import io.github.apace100.apoli.condition.type.BiEntityConditionType;
import io.github.apace100.apoli.condition.type.BiEntityConditionTypes;
import io.github.apace100.apoli.data.TypedDataObjectFactory;
import io.github.apace100.apoli.util.requirement.BiEntityRequirement;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import org.jetbrains.annotations.NotNull;

public class CanSeeBiEntityConditionType extends BiEntityConditionType {

    public static final TypedDataObjectFactory<CanSeeBiEntityConditionType> DATA_FACTORY = TypedDataObjectFactory.simple(
        new SerializableData()
            .add("shape_type", SerializableDataTypes.SHAPE_TYPE, RaycastContext.ShapeType.VISUAL)
            .add("fluid_handling", SerializableDataTypes.FLUID_HANDLING, RaycastContext.FluidHandling.NONE),
        data -> new CanSeeBiEntityConditionType(
            data.get("shape_type"),
            data.get("fluid_handling")
        ),
        (conditionType, serializableData) -> serializableData.instance()
            .set("shape_type", conditionType.shapeType)
            .set("fluid_handling", conditionType.fluidHandling)
    );

    private final RaycastContext.ShapeType shapeType;
    private final RaycastContext.FluidHandling fluidHandling;

    public CanSeeBiEntityConditionType(RaycastContext.ShapeType shapeType, RaycastContext.FluidHandling fluidHandling) {
        this.shapeType = shapeType;
        this.fluidHandling = fluidHandling;
    }

    @Override
    public boolean test(BiEntityConditionContext context) {

        Entity actor = context.actor();
        Entity target = context.target();

        if (actor.getWorld() != target.getWorld()) {
            return false;
        }

        Vec3d actorEyePos = actor.getEyePos();
        Vec3d targetEyePos = target.getEyePos();

        RaycastContext raycastContext = new RaycastContext(actorEyePos, targetEyePos, shapeType, fluidHandling, actor);
        return actor.getWorld().raycast(raycastContext).getType() == HitResult.Type.MISS;

    }

    @Override
    public BiEntityRequirement getRequirement() {
        return BiEntityRequirement.BOTH;
    }

    @Override
    public @NotNull ConditionConfiguration<?> getConfig() {
        return BiEntityConditionTypes.CAN_SEE;
    }

}
