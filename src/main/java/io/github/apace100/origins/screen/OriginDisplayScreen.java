package io.github.apace100.origins.screen;

import io.github.apace100.origins.Origins;
import io.github.apace100.origins.origin.Origin;
import io.github.apace100.origins.origin.OriginLayer;
import io.github.apace100.origins.screen.widget.OriginWindowWidget;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import java.util.function.Function;

public abstract class OriginDisplayScreen extends Screen {

    public static final Identifier DIRT_BACKGROUND = Origins.identifier("textures/dirt_background.png");

    protected final OriginWindowWidget windowWidget;
    protected final boolean showDirtBackground;

    public OriginDisplayScreen(Component title, boolean showDirtBackground) {
        super(title);
        this.windowWidget = new OriginWindowWidget();
        this.showDirtBackground = showDirtBackground;
    }

    @Override
    protected void init() {

        super.init();

        this.windowWidget.init(this.font);
        this.windowWidget.setPosition((this.width - windowWidget.getWidth()) / 2, (this.height - windowWidget.getHeight()) / 2);

        this.addRenderableWidget(windowWidget);

    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta) {
        extractBackground(context, mouseX, mouseY, delta);
        super.extractRenderState(context, mouseX, mouseY, delta);
        context.centeredText(font, this.getTitle(), width / 2, windowWidget.getY() - 15, 0xFFFFFF);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta) {

        if (showDirtBackground) {
//            RenderSystem.enableBlend();
            context.blit(RenderPipelines.GUI_TEXTURED, DIRT_BACKGROUND, 0, 0, 0.0F, 0.0F, this.width, this.height, 32, 32);
//            RenderSystem.disableBlend();
        }

        else {
            super.extractBackground(context, mouseX, mouseY, delta);
        }

    }

    public void renderInGameBackground(GuiGraphicsExtractor context) {
        context.fillGradient(0, 0, this.width, this.height, 1678774288, -2112876528);
    }

    protected abstract Origin getCurrentOrigin();

    protected abstract OriginLayer getCurrentLayer();

    protected void showCurrent(Function<Origin, Origin.WindowTextures> texturesGetter) {
        windowWidget.show(getCurrentOrigin(), getCurrentLayer(), texturesGetter);
    }

    protected void resetAndShowCurrent(Function<Origin, Origin.WindowTextures> texturesGetter) {
        windowWidget.resetAndShow(getCurrentOrigin(), getCurrentLayer(), texturesGetter);
    }

}
