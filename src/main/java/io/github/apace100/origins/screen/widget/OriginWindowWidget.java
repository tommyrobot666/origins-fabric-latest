package io.github.apace100.origins.screen.widget;

import io.github.apace100.apoli.power.MultiplePower;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.util.TextAlignment;
import io.github.apace100.origins.OriginsClient;
import io.github.apace100.origins.badge.Badge;
import io.github.apace100.origins.badge.BadgeManager;
import io.github.apace100.origins.origin.Origin;
import io.github.apace100.origins.origin.OriginLayer;
import io.github.apace100.origins.origin.OriginManager;
import io.github.apace100.origins.screen.DrawableTooltip;
import io.github.apace100.origins.util.MiscUtilClient;
import it.unimi.dsi.fastutil.objects.ObjectAVLTreeSet;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractScrollArea;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

//  TODO:   Optimize the rendering impl. by caching the sub-elements (like lines of the origin's name/description,
//          power names/descriptions, etc.)
@Environment(EnvType.CLIENT)
public class OriginWindowWidget extends AbstractScrollArea {

	public static final int LINE_HEIGHT = 11;

	public static final int WINDOW_WIDTH = 176;
	public static final int WINDOW_HEIGHT = 182;

	public static final int NAMEPLATE_WIDTH = 150;
	public static final int NAMEPLATE_HEIGHT = 26;

	private final List<DrawableTooltip> tooltips = new ObjectArrayList<>();
	private int lines;

	private OverlayContext overlayContext = OverlayContext.EMPTY;
	@Nullable
	private Font textRenderer;

	private Origin.WindowTextures textures = Origin.WindowTextures.DEFAULT;
	@Nullable
	private MutableComponent randomOrigins;

	@Nullable
	private Origin origin;
	@Nullable
	private OriginLayer layer;

	public OriginWindowWidget() {
		super(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT, Component.empty(),
				defaultSettings(1));
	}

	@Override
	public void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {

		this.overlayContext = new OverlayContext(mouseX, mouseY, delta);
		this.lines = 0;

		renderBackground(graphics);
		renderContents(graphics, mouseX, mouseY, delta);
		renderOverlay(graphics);

//		super.extractWidgetRenderState(graphics, mouseX, mouseY + Math.round((float) this.scrollBarY()), delta);

	}

	@Override
	protected void updateWidgetNarration(NarrationElementOutput output) {

	}

	@Override
	protected int contentHeight() {
		return lines * LINE_HEIGHT;
	}

	@Override
	protected double scrollRate() {
		return Minecraft.getInstance().hasShiftDown() ? LINE_HEIGHT / 2.0 : LINE_HEIGHT;
	}

	protected void renderBackground(GuiGraphicsExtractor context) {
		context.blitSprite(RenderPipelines.GUI_TEXTURED,textures.background(), this.getX(), this.getY(), this.getWidth(), this.getHeight());
	}

	protected void renderContents(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta) {
		this.renderPowersAndBadges(context, mouseX, mouseY, delta);
		this.renderNameAndImpact(context, mouseX, mouseY, delta);
	}

	protected void renderOverlay(GuiGraphicsExtractor context) {

//		super.renderOverlay(context);
		context.blitSprite(RenderPipelines.GUI_TEXTURED,textures.border(), this.getX(), this.getY(), 200, this.getWidth(), this.getHeight());

		if (textRenderer == null) {
			return;
		}

		Iterator<DrawableTooltip> tooltipIterator = tooltips.iterator();
		while (tooltipIterator.hasNext()) {

			var tooltip = tooltipIterator.next();
			tooltip.renderTooltip(textRenderer, context, overlayContext.mouseX(), overlayContext.mouseY(), overlayContext.delta());

			tooltipIterator.remove();

		}

	}

	public int getTextMaxWidth() {
		return this.getWidth() - 30;
	}

	public int getCenterX() {
		return this.getX() + (this.getWidth() / 2);
	}

	public int getCenterY() {
		return this.getY() + (this.getHeight() / 2);
	}

	public void init(Font renderer) {
		this.textRenderer = renderer;
	}

	public void show(@NotNull Origin origin, @NotNull OriginLayer layer, Function<Origin, Origin.@NotNull WindowTextures> texturesGetter) {

		this.origin = origin;
		this.layer = layer;

		if (origin == Origin.RANDOM) {

			this.randomOrigins = Component.empty();

			ObjectSortedSet<Origin> randoms = new ObjectAVLTreeSet<>(Origin::compareTo);
			layer.getRandomOrigins(Minecraft.getInstance().player).forEach(id -> randoms.add(OriginManager.get(id)));

			for (var random : randoms) {
				this.randomOrigins.append(Component.literal("\n")).append("- ").append(random.getName());
			}

		}

		else {
			this.randomOrigins = null;
		}

		this.textures = texturesGetter.apply(origin);

	}

	public void resetAndShow(@NotNull Origin origin, @NotNull OriginLayer layer, Function<Origin, Origin.@NotNull WindowTextures> texturesGetter) {
		this.show(origin, layer, texturesGetter);
		this.setScrollAmount(0.0);
	}

	protected Collection<BadgeWidget> getBadgeWidgets(Power power) {

		if (!BadgeManager.hasPowerBadges(power)) {

			if (power instanceof MultiplePower multiplePower) {

				List<BadgeWidget> widgets = new ObjectArrayList<>();
				for (var subPower : multiplePower.getSubPowers()) {

					for (var badge : BadgeManager.getPowerBadges(subPower.getId())) {
						widgets.add(new BadgeWidget(subPower, badge, 0, 0));
					}

				}

				return widgets;

			}

			else {
				return List.of();
			}

		}

		else {

			List<BadgeWidget> widgets = new ObjectArrayList<>();
			for (var badge : BadgeManager.getPowerBadges(power.getId())) {
				widgets.add(new BadgeWidget(power, badge, 0, 0));
			}

			return widgets;

		}

	}

	protected void renderPowersAndBadges(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta) {

		if (textRenderer == null) {
			return;
		}

		int x = this.getX() + 18;
		int y = this.getY() + 45;

		if (origin == null || layer == null) {

			Component emptyOrNotInstalledText = OriginsClient.isServerRunningOrigins
				? Component.translatable("origins.gui.view_origin.empty")
				: Component.translatable("origins.gui.view_origin.not_installed");

			for (var line : textRenderer.split(emptyOrNotInstalledText, this.getTextMaxWidth())) {
				context.centeredText(textRenderer, line, this.getCenterX(), y, -1);
				y += LINE_HEIGHT;
			}

		}

		else {

			Component description = origin == Origin.EMPTY && layer.getMissingDescription() != null
				? layer.getMissingDescription()
				: origin.getDescription();

			for (var descriptionLine : textRenderer.split(description, this.getTextMaxWidth())) {

				context.text(textRenderer, descriptionLine, x, y, 0xCCCCCC);

				y += LINE_HEIGHT;
				lines++;

			}

			if (randomOrigins != null) {

				for (var randomOriginLine : textRenderer.split(randomOrigins, this.getTextMaxWidth())) {

					context.text(textRenderer, randomOriginLine, x, y, 0xCCCCCC);

					y += LINE_HEIGHT;
					lines++;

				}

			}

			else {

				for (var power : origin.getPowers()) {

					if (power.isHidden()) {
						continue;
					}

					y += LINE_HEIGHT;
					lines++;

					List<FormattedCharSequence> nameLines = new ObjectArrayList<>(textRenderer.split(power.getName().withStyle(ChatFormatting.UNDERLINE), this.getTextMaxWidth()));
					int nameWidth = textRenderer.width(nameLines.getLast());

					for (var nameLine : nameLines) {

						context.text(textRenderer, nameLine, x, y, 0xFFFFFF);

						y += LINE_HEIGHT;
						lines++;

					}

					y -= LINE_HEIGHT;
					lines--;

					int badgeStartX = x + nameWidth + 4;
					int badgeEndX = x + (this.getWidth() - 6);

					int badgeOffsetX = 0;
					int badgeOffsetY = 0;

					for (var badgeWidget : this.getBadgeWidgets(power)) {

						int badgeX = badgeStartX + (badgeOffsetX * Badge.SIZE);
						int badgeY = (y - 1) + (badgeOffsetY * Badge.SIZE);

						if (badgeX >= badgeEndX) {

							badgeOffsetX = 0;
							badgeOffsetY++;

							badgeX = badgeStartX = x;
							badgeY = (y - 1) + (badgeOffsetY * Badge.SIZE);

						}

						badgeWidget.setPosition(badgeX, badgeY);
						badgeWidget.extractWidgetRenderState(context, mouseX, mouseY, delta);

						badgeOffsetX++;

						if (badgeWidget.hasTooltip() && badgeWidget.isHovered()) {
							tooltips.add(badgeWidget);
						}

					}

					int badgeOffset = Math.max(badgeOffsetY, 1);

					y += badgeOffset * Badge.SIZE;
					lines += badgeOffset;

					for (var powerDescriptionLine : textRenderer.split(power.getDescription(), this.getTextMaxWidth())) {

						context.text(textRenderer, powerDescriptionLine, x, y, 0xCCCCCC);

						y += LINE_HEIGHT;
						lines++;

					}

				}

			}

		}

		lines += 5;

	}

	protected void renderNameAndImpact(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta) {

		if (origin == null) {
			return;
		}

		int y = this.getY();

		int namePlateXStart = this.getCenterX() - (NAMEPLATE_WIDTH / 2);
		int namePlateXEnd = namePlateXStart + NAMEPLATE_WIDTH;

		context.blitSprite(RenderPipelines.GUI_TEXTURED,textures.namePlate(), namePlateXStart, y + 10, NAMEPLATE_WIDTH, NAMEPLATE_HEIGHT);

		ItemStack icon = origin.getDisplayItem();
		ImpactWidget impactWidget = new ImpactWidget(origin.getImpact(), namePlateXEnd - 32, y + 19);

		int nameStartX = namePlateXStart + 30;
		int nameStartY = y + 18;
		int nameEndX = impactWidget.getX() - 2;
		int nameEndY = nameStartY + 9;

		if (textRenderer != null) {
			MiscUtilClient.drawScrollingText(context, textRenderer, origin.getName(), TextAlignment.CENTER, nameStartX, nameStartY, nameEndX, nameEndY, 0xFFFFFF, true);
		}

		impactWidget.extractWidgetRenderState(context, mouseX, mouseY, delta);
		context.item(icon, namePlateXStart + 5, y + 15);

		if (impactWidget.isHovered()) {
			this.tooltips.add(impactWidget);
		}

	}

	record OverlayContext(int mouseX, int mouseY, float delta) {
		public static final OverlayContext EMPTY = new OverlayContext(0, 0, 0.0F);
	}

}
