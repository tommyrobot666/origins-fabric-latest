package io.github.apace100.origins.badge;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.type.ModifyCraftingPowerType;
import io.github.apace100.apoli.util.InventoryUtil;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.origins.Origins;
import io.github.apace100.origins.screen.tooltip.CraftingRecipeTooltipComponent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public record CraftingRecipeBadge(Identifier spriteId,
                                  RecipeHolder<CraftingRecipe> recipe,
                                  @Nullable Component prefix,
                                  @Nullable Component suffix) implements Badge {

    public CraftingRecipeBadge(SerializableData.Instance instance) {
        this(instance.getId("sprite"),
            instance.get("recipe"),
            instance.get("prefix"),
            instance.get("suffix"));
    }

    @Override
    public boolean hasTooltip() {
        return true;
    }

    public NonNullList<ItemStack> peekInputs(float time) {

        NonNullList<ItemStack> inputs = NonNullList.withSize(9, ItemStack.EMPTY);
        List<Ingredient> ingredients = this.recipe.value().placementInfo().ingredients();

        int seed = Mth.floor(time / 30);
        for (int index = 0; index < ingredients.size(); index++) {

            ItemStack[] stacks = ingredients.get(index).items().map(h -> h.value().getDefaultInstance()).toArray(ItemStack[]::new);
            if (stacks.length > 0) {
                inputs.set(index, stacks[seed % stacks.length]);
            }

        }

        return inputs;

    }

    @Environment(EnvType.CLIENT)
    @Override
    public List<ClientTooltipComponent> getTooltipComponents(Power power, int widthLimit, float time, Font textRenderer) {

        Minecraft client = Minecraft.getInstance();
        List<ClientTooltipComponent> tooltips = new LinkedList<>();

        if (client.level == null) {
            Origins.LOGGER.warn("Could not construct crafting recipe badge, as world was null!");
            return tooltips;
        }

        int recipeWidth = (Recipe<?>) recipe.value() instanceof ShapedRecipe shapedRecipe ? shapedRecipe.getWidth() : 3;

        RegistryAccess registryManager = client.level.registryAccess();
        SlotAccess outputStackReference = InventoryUtil.createStackReference(recipe.value().assemble(null));

        PowerHolderComponent.getPowerTypes(client.player, ModifyCraftingPowerType.class)
            .stream()
            .filter(p -> p.doesApply(recipe.id().identifier(), outputStackReference.get()))
            .max(Comparator.comparing(ModifyCraftingPowerType::getPriority))
            .ifPresent(p -> p.getNewResult(outputStackReference));
        CraftingRecipeTooltipComponent recipeTooltip = new CraftingRecipeTooltipComponent(recipeWidth, this.peekInputs(time), outputStackReference.get());

        if (client.options.advancedItemTooltips) {

            Component recipeIdText = Component.literal(recipe.id().toString()).withStyle(ChatFormatting.DARK_GRAY);
            widthLimit = Math.max(130, textRenderer.width(recipeIdText));

            if (prefix != null) {
                TooltipBadge.addLines(tooltips, prefix, textRenderer, widthLimit);
            }

            tooltips.add(recipeTooltip);
            if (suffix != null) {
                TooltipBadge.addLines(tooltips, suffix, textRenderer, widthLimit);
            }

            TooltipBadge.addLines(tooltips, recipeIdText, textRenderer, widthLimit);

        } else {

            widthLimit = 130;
            if (prefix != null) {
                TooltipBadge.addLines(tooltips, prefix, textRenderer, widthLimit);
            }

            tooltips.add(recipeTooltip);
            if (suffix != null) {
                TooltipBadge.addLines(tooltips, suffix, textRenderer, widthLimit);
            }

        }

        return tooltips;

    }

    @Override
    public SerializableData.Instance toData(SerializableData.Instance instance) {
        instance.set("sprite", spriteId);
        instance.set("recipe", recipe);
        instance.set("prefix", prefix);
        instance.set("suffix", suffix);
        return instance;
    }

    @Override
    public BadgeFactory getBadgeFactory() {
        return BadgeFactories.CRAFTING_RECIPE;
    }

}
