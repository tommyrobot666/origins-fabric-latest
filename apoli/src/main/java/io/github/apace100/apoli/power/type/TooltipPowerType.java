package io.github.apace100.apoli.power.type;

import com.google.common.collect.Lists;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.DataResult;
import io.github.apace100.apoli.Apoli;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.condition.EntityCondition;
import io.github.apace100.apoli.condition.ItemCondition;
import io.github.apace100.apoli.data.TypedDataObjectFactory;
import io.github.apace100.apoli.power.PowerConfiguration;
import io.github.apace100.apoli.util.MiscUtil;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.RegistryOps;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;
import net.minecraft.text.Texts;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import java.util.function.Consumer;

public class TooltipPowerType extends PowerType {

    public static final TypedDataObjectFactory<TooltipPowerType> DATA_FACTORY = PowerType.createConditionedDataFactory(
        new SerializableData()
            .add("item_condition", ItemCondition.DATA_TYPE.optional(), Optional.empty())
            .add("text", SerializableDataTypes.TEXT, null)
            .addFunctionedDefault("texts", SerializableDataTypes.TEXTS, data -> MiscUtil.singletonListOrNull(data.get("text")))
            .add("should_resolve", SerializableDataTypes.BOOLEAN, false)
            .addFunctionedDefault("resolve", SerializableDataTypes.BOOLEAN, data -> data.get("should_resolve"))
            .add("tick_rate", SerializableDataTypes.INT, 20)
            .add("order", SerializableDataTypes.INT, 0)
            .validate(MiscUtil.validateAnyFieldsPresent("text", "texts")),
        (data, condition) -> new TooltipPowerType(
            data.get("item_condition"),
            data.get("texts"),
            data.get("resolve"),
            data.get("tick_rate"),
            data.get("order"),
            condition
        ),
        (powerType, serializableData) -> serializableData.instance()
            .set("item_condition", powerType.itemCondition)
            .set("texts", powerType.texts)
            .set("resolve", powerType.resolve)
            .set("tick_rate", powerType.tickRate)
            .set("order", powerType.order)
    );

    private final Optional<ItemCondition> itemCondition;
    private final List<Text> texts;

    private final boolean resolve;

    private final int tickRate;
    private final int order;

    private final ObjectArrayList<Text> tooltipTexts;

    private Integer startTicks;
    private Integer endTicks;

    private boolean wasActive;

    public TooltipPowerType(Optional<ItemCondition> itemCondition, List<Text> texts, boolean resolve, int tickRate, int order, Optional<EntityCondition> condition) {
        super(condition);

        this.itemCondition = itemCondition;
        this.texts = texts;

        this.resolve = resolve;

        this.tickRate = tickRate;
        this.order = order;

        this.tooltipTexts = new ObjectArrayList<>();

        this.startTicks = null;
        this.endTicks = null;

        this.wasActive = false;

    }

    @Override
    public @NotNull PowerConfiguration<?> getConfig() {
        return PowerTypes.TOOLTIP;
    }

    @Override
    public boolean shouldTick() {
        return resolve;
    }

    @Override
    public boolean shouldTickWhenInactive() {
        return shouldTick();
    }

    @Override
    public void serverTick() {

        LivingEntity holder = getHolder();
        int modTicks = holder.age % tickRate;

        if (isActive()) {

            if (startTicks == null) {
                this.startTicks = modTicks;
                this.endTicks = null;
            }

            else if (modTicks == startTicks) {

                List<Text> parsedTexts = parseTexts();
                this.wasActive = true;

                if (!parsedTexts.isEmpty() && Collections.disjoint(tooltipTexts, parsedTexts)) {

                    this.tooltipTexts.clear();
                    this.tooltipTexts.addAll(parsedTexts);

                    this.tooltipTexts.trim();
                    PowerHolderComponent.syncPower(getHolder(), getPower());

                }

            }

        }

        else if (wasActive) {

            if (endTicks == null) {
                this.startTicks = null;
                this.endTicks = modTicks;
            }

            else if (modTicks == endTicks) {
                this.wasActive = false;
            }

        }

    }

    @Override
    public NbtElement toTag() {

        RegistryWrapper.WrapperLookup registryLookup = getHolder().getRegistryManager();
        RegistryOps<NbtElement> nbtOps = registryLookup.getOps(NbtOps.INSTANCE);

        NbtCompound rootNbt = new NbtCompound();
        NbtList tooltipTextsNbt = new NbtList();

        tooltipTexts.stream()
            .map(text -> TextCodecs.CODEC.encodeStart(nbtOps, text))
            .filter(DataResult::isSuccess)
            .map(DataResult::getOrThrow)
            .forEach(tooltipTextsNbt::add);

        rootNbt.put("Tooltips", tooltipTextsNbt);
        return rootNbt;

    }

    @Override
    public void fromTag(NbtElement tag) {

        RegistryWrapper.WrapperLookup registryLookup = getHolder().getRegistryManager();
        RegistryOps<NbtElement> nbtOps = registryLookup.getOps(NbtOps.INSTANCE);

        this.tooltipTexts.clear();

        NbtCompound rootNbt = (NbtCompound) tag;
        NbtElement tooltipTextsNbt = rootNbt.get("Tooltips");

        if (tooltipTextsNbt instanceof NbtList actualTooltipTextNbt) {

            actualTooltipTextNbt
                .stream()
                .map(nbtElement -> TextCodecs.CODEC.parse(nbtOps, nbtElement))
                .filter(DataResult::isSuccess)
                .map(DataResult::getOrThrow)
                .forEach(this.tooltipTexts::add);

        }

        this.tooltipTexts.trim();

    }

    public int getOrder() {
        return order;
    }

    public void processTooltips(Consumer<Text> processor) {

        if (resolve) {
            tooltipTexts.forEach(processor);
        }

        else {
            texts.forEach(processor);
        }

    }

    public boolean doesApply(ItemStack stack) {
        return itemCondition
            .map(condition -> condition.test(getHolder().getWorld(), stack))
            .orElse(true);
    }

    private List<Text> parseTexts() {

        List<Text> parsedTexts = Lists.newLinkedList();
        LivingEntity holder = getHolder();

        if (texts.isEmpty() || !(holder.getWorld() instanceof ServerWorld serverWorld)) {
            return parsedTexts;
        }

        ListIterator<Text> textIterator = texts.listIterator();
        ServerCommandSource source = holder.getCommandSource()
            .withOutput(serverWorld.getServer())
            .withLevel(Apoli.config.executeCommand.permissionLevel);

        while (textIterator.hasNext()) {

            Text text = textIterator.next();
            int index = textIterator.nextIndex();

            try {
                parsedTexts.add(Texts.parse(source, text, holder, 0));
            }

            catch (CommandSyntaxException cse) {
                Apoli.LOGGER.warn("Power {} couldn't parse tooltip text at index {}: {}", this.getPower().getId(), index, cse.getMessage());
            }

        }

        return parsedTexts;

    }

}
