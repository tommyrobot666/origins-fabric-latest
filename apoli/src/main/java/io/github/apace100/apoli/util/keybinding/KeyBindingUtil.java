package io.github.apace100.apoli.util.keybinding;

import io.github.apace100.apoli.mixin.KeyBindingAccessor;
import io.github.apace100.apoli.util.StringAlias;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.Optional;
import java.util.function.Predicate;

@Environment(EnvType.CLIENT)
public class KeyBindingUtil {

    public static final StringAlias ALIASES = new StringAlias();

    /**
     *  Get the localized name of the keybind from the specified ID. If no such keybind exists or if the keybind is
     *  not bound to any key, use the specified ID instead.
     *
     *  @param translationKey   The translation key of the keybind to get its localized bound key name of.
     *  @return                 Either a {@linkplain Text text} that is localized, or a {@linkplain net.minecraft.text.TranslatableTextContent translatable text}
     *                              that contains the specified translation key.
     */
    public static MutableText getLocalizedName(String translationKey) {
        return getKeyBinding(translationKey)
            .filter(Predicate.not(KeyBinding::isUnbound))
            .map(KeyBinding::getBoundKeyLocalizedText)
            .map(Text::copy)
            .orElseGet(() -> Text.translatable(translationKey));
    }

    public static Optional<KeyBinding> getKeyBinding(String keyBindingId) {

        keyBindingId = ALIASES.hasAlias(keyBindingId)
            ? ALIASES.resolveAlias(keyBindingId)
            : keyBindingId;

        return Optional.ofNullable(KeyBindingAccessor.getKeysById().get(keyBindingId));

    }

}
