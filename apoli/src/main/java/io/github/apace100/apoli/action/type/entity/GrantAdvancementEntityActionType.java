package io.github.apace100.apoli.action.type.entity;

import io.github.apace100.apoli.Apoli;
import io.github.apace100.apoli.action.ActionConfiguration;
import io.github.apace100.apoli.action.context.EntityActionContext;
import io.github.apace100.apoli.action.type.EntityActionType;
import io.github.apace100.apoli.action.type.EntityActionTypes;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.data.TypedDataObjectFactory;
import io.github.apace100.apoli.util.AdvancementUtil;
import io.github.apace100.apoli.util.MiscUtil;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerAdvancementLoader;
import net.minecraft.server.command.AdvancementCommand;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GrantAdvancementEntityActionType extends EntityActionType {

    public static final TypedDataObjectFactory<GrantAdvancementEntityActionType> DATA_FACTORY = TypedDataObjectFactory.simple(
        new SerializableData()
            .add("advancement", SerializableDataTypes.IDENTIFIER)
            .add("selection", ApoliDataTypes.ADVANCEMENT_SELECTION, AdvancementCommand.Selection.ONLY)
            .add("criterion", SerializableDataTypes.STRING, null)
            .addFunctionedDefault("criteria", SerializableDataTypes.STRINGS, data -> MiscUtil.singletonListOrEmpty(data.get("criterion"))),
        data -> new GrantAdvancementEntityActionType(
            data.get("advancement"),
            data.get("selection"),
            data.get("criteria")
        ),
        (actionType, serializableData) -> serializableData.instance()
            .set("advancement", actionType.advancementId)
            .set("selection", actionType.selection)
            .set("criteria", actionType.criteria)
    );

    private final Identifier advancementId;
    private final AdvancementCommand.Selection selection;

    private final List<String> criteria;

    public GrantAdvancementEntityActionType(Identifier advancementId, AdvancementCommand.Selection selection, List<String> criteria) {
        this.advancementId = advancementId;
        this.selection = selection;
        this.criteria = criteria;
    }

    @Override
    public void accept(EntityActionContext context) {

        Entity entity = context.entity();
        MinecraftServer server = entity.getServer();

        if (server == null || !(entity instanceof ServerPlayerEntity serverPlayerEntity)) {
            return;
        }

        ServerAdvancementLoader advancementLoader = server.getAdvancementLoader();
        if (selection == AdvancementCommand.Selection.EVERYTHING) {
            AdvancementUtil.processAdvancements(advancementLoader.getAdvancements(), AdvancementCommand.Operation.GRANT, serverPlayerEntity);
        }

        else if (advancementId != null) {

            AdvancementEntry advancementEntry = advancementLoader.get(advancementId);
            if (advancementEntry == null) {
                Apoli.LOGGER.warn("Unknown advancement (\"" + advancementId + "\") referenced in `grant_advancement` entity action type!");
            }

            else if (criteria.isEmpty()) {
                AdvancementUtil.processAdvancements(AdvancementUtil.selectEntries(server.getAdvancementLoader().getManager(), advancementEntry, selection), AdvancementCommand.Operation.GRANT, serverPlayerEntity);
            }

            else {
                AdvancementUtil.processCriteria(advancementEntry, criteria, AdvancementCommand.Operation.GRANT, serverPlayerEntity);
            }

        }

    }

    @Override
    public @NotNull ActionConfiguration<?> getConfig() {
        return EntityActionTypes.GRANT_ADVANCEMENT;
    }

}
