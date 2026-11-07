package io.github.apace100.origins.screen.widget;

import io.github.apace100.origins.Origins;
import io.github.apace100.origins.origin.Impact;
import io.github.apace100.origins.screen.DrawableTooltip;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.UnknownNullability;

public final class ImpactWidget extends AbstractWidget implements DrawableTooltip {

	private final Impact impact;

	public ImpactWidget(Impact impact, int x, int y) {
		super(x, y, Impact.WIDTH, Impact.HEIGHT, Component.translatable(Origins.MODID + ".gui.impact.impact").append(": ").append(impact.getTextComponent()));
		this.impact = impact;
	}

	@Override
	protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
		graphics.blitSprite(RenderPipelines.GUI_TEXTURED, impact.getSpriteId(), this.getX(), this.getY(), 28, 8);
	}

	@Override
	public void setWidth(int width) {
		//  No-op; impact widgets aren't supposed to be resizable
	}

	@Override
	public void setHeight(int height) {
		//  No-op; impact widgets aren't supposed to be resizable
	}

	@Override
	protected void updateWidgetNarration(NarrationElementOutput output) {
		output.add(NarratedElementType.HINT,this.getMessage());
	}

	@Override
	public void renderTooltip(Font textRenderer, @UnknownNullability GuiGraphicsExtractor context, int mouseX, int mouseY, float delta) {
		context.text(textRenderer, this.getMessage(), mouseX, mouseY, 0XFFFFFFFF);
	}

}
