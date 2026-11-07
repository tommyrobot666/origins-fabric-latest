package io.github.apace100.origins.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;

@Environment(EnvType.CLIENT)
public interface DrawableTooltip {

	void renderTooltip(Font textRenderer, GuiGraphicsExtractor context, int mouseX, int mouseY, float delta);

}
