package io.github.apace100.origins;

import com.mojang.blaze3d.platform.InputConstants;
import io.github.apace100.apoli.integration.PowerClearCallback;
import io.github.apace100.apoli.util.keybinding.KeyBindingUtil;
import io.github.apace100.origins.networking.ModPacketsS2C;
import io.github.apace100.origins.registry.ModEntities;
import io.github.apace100.origins.screen.ViewOriginScreen;
import io.github.apace100.origins.util.PowerKeyManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;

public class OriginsClient implements ClientModInitializer {

    public static KeyMapping.Category originsCategory;
    public static KeyMapping primaryActiveKeyBinding;
    public static KeyMapping secondaryActiveKeyBinding;
    public static KeyMapping viewOriginKeyBinding;

    public static boolean isServerRunningOrigins = false;

    @Override
    @Environment(EnvType.CLIENT)
    public void onInitializeClient() {

//        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.TEMPORARY_COBWEB, RenderLayer.getCutout());
        EntityRenderers.register(ModEntities.ENDERIAN_PEARL, ThrownItemRenderer::new);

        ModPacketsS2C.register();

        originsCategory = KeyMapping.Category.register(Origins.identifier("category"));
        primaryActiveKeyBinding = new KeyMapping("key.origins.primary_active", InputConstants.Type.KEYBOARD, InputConstants.KEY_G, originsCategory);
        secondaryActiveKeyBinding = new KeyMapping("key.origins.secondary_active", InputConstants.Type.KEYBOARD, InputConstants.UNKNOWN.getValue(), originsCategory);
        viewOriginKeyBinding = new KeyMapping("key.origins.view_origin", InputConstants.Type.KEYBOARD, InputConstants.KEY_O, originsCategory);

        KeyBindingUtil.ALIASES.addAlias("primary", primaryActiveKeyBinding.getName());
        KeyBindingUtil.ALIASES.addAlias("secondary", secondaryActiveKeyBinding.getName());

        //  "none" is the default key used when no keybinding reference is specified in powers
        KeyBindingUtil.ALIASES.addAlias("none", primaryActiveKeyBinding.getName());

        KeyMappingHelper.registerKeyMapping(primaryActiveKeyBinding);
        KeyMappingHelper.registerKeyMapping(secondaryActiveKeyBinding);
        KeyMappingHelper.registerKeyMapping(viewOriginKeyBinding);

        ClientTickEvents.START_CLIENT_TICK.register(client -> {

            while (viewOriginKeyBinding.consumeClick()) {

                if (!(client.gui.screen() instanceof ViewOriginScreen)) {
                    client.setScreenAndShow(new ViewOriginScreen());
                }

            }

        });

        PowerClearCallback.EVENT.register(PowerKeyManager::clearCache);

    }
}
