package io.github.apace100.apoli.power.type;

import io.github.apace100.apoli.condition.EntityCondition;
import io.github.apace100.apoli.power.PowerConfiguration;
import io.github.apace100.apoli.util.HudRender;
import io.github.apace100.apoli.util.keybinding.KeyBindingReference;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public abstract class ActiveCooldownPowerType extends CooldownPowerType implements Active {

    private final KeyBindingReference key;

    public ActiveCooldownPowerType(HudRender hudRender, int cooldownDuration, KeyBindingReference key, Optional<EntityCondition> condition) {
        super(cooldownDuration, hudRender, condition);
        this.key = key;
    }

    public ActiveCooldownPowerType(HudRender hudRender, int cooldownDuration, KeyBindingReference key) {
        this(hudRender, cooldownDuration, key, Optional.empty());
    }

    @Override
    public abstract @NotNull PowerConfiguration<?> getConfig();

    @Override
    public KeyBindingReference getKey() {
        return key;
    }

    @Override
    public void onUse() {
        use();
    }

    @Override
    public boolean canTrigger() {
        return isActive();
    }

}
