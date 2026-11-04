package io.github.apace100.origins.badge;

import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.util.keybinding.KeyBindingUtil;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.origins.util.PowerKeyManager;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTextTooltip;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import java.util.List;
import java.util.Optional;

public record KeybindBadge(Identifier spriteId, String text) implements Badge {

    public KeybindBadge(SerializableData.Instance instance) {
        this(instance.getId("sprite"), instance.get("text"));
    }

    @Override
    public boolean hasTooltip() {
        return !text.isEmpty();
    }

    @Override
    public List<ClientTooltipComponent> getTooltipComponents(Power power, int widthLimit, float time, Font textRenderer) {

        Optional<String> keyId = PowerKeyManager.getKeyId(power);
        List<ClientTooltipComponent> tooltips = new ObjectArrayList<>();

        if (keyId.isPresent()) {

            Component keyName = KeyBindingUtil.getLocalizedName(keyId.get());
            Component keyText = Component.translatable(text(), Component.literal("[").append(keyName).append("]"));

            if (textRenderer.width(keyText) > widthLimit) {
                textRenderer.split(keyText, widthLimit)
                    .stream()
                    .map(ClientTextTooltip::new)
                    .forEach(tooltips::add);
            }

            else {
                tooltips.add(new ClientTextTooltip(keyText.getVisualOrderText()));
            }

        }

        return tooltips;

    }

    @Override
    public SerializableData.Instance toData(SerializableData.Instance instance) {
        instance.set("sprite", spriteId);
        instance.set("text", text);
        return instance;
    }

    @Override
    public BadgeFactory getBadgeFactory() {
        return BadgeFactories.KEYBIND;
    }

}
