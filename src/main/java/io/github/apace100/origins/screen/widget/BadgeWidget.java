package io.github.apace100.origins.screen.widget;

import io.github.apace100.apoli.power.Power;
import io.github.apace100.origins.badge.Badge;
import io.github.apace100.origins.screen.DrawableTooltip;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.network.chat.Component;

import java.util.List;

public final class BadgeWidget extends AbstractWidget implements DrawableTooltip {

	private final Power power;
	private final Badge badge;

	public BadgeWidget(Power power, Badge badge, int x, int y) {
		super(x, y, Badge.SIZE, Badge.SIZE, Component.empty());
		this.power = power;
		this.badge = badge;
	}

	@Override
	protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
		graphics.blit(badge.spriteId(), this.getX(), this.getY(), 0, 0, 9, 9, 9, 9);
	}

	@Override
	public void setWidth(int width) {
		//  No-op; badges aren't supposed to be resizable
	}

	@Override
	public void setHeight(int height) {
		//  No-op; badges aren't supposed to be resizable
	}

	@Override
	protected void updateWidgetNarration(NarrationElementOutput output) {

	}

	@Override
	public void renderTooltip(Font textRenderer, GuiGraphicsExtractor context, int mouseX, int mouseY, float delta) {

		Minecraft client = Minecraft.getInstance();
		Screen currentScreen = client.gui.screen();

		if (currentScreen != null) {
			context.tooltip(textRenderer,this.getTooltipComponents(textRenderer,currentScreen.width - mouseX - 24, delta),mouseX,mouseY, DefaultTooltipPositioner.INSTANCE,null,false);
		}

	}

	public List<ClientTooltipComponent> getTooltipComponents(Font textRenderer, int widthLimit, float delta) {
		return badge.getTooltipComponents(power, widthLimit, delta, textRenderer);
	}

	public boolean hasTooltip() {
		return badge.hasTooltip();
	}

}
