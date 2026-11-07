package io.github.apace100.origins.screen;

import io.github.apace100.origins.OriginsClient;
import io.github.apace100.origins.origin.Origin;
import io.github.apace100.origins.origin.OriginLayer;
import io.github.apace100.origins.registry.ModComponents;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PlayerHeadItem;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Objects;

//  FIXME:  Viewing an origin in a layer somehow does not respect the order the same way choosing an origin from a layer
//          does, despite the entries already being sorted (based on the layer's specified numeric order value)
public class ViewOriginScreen extends OriginDisplayScreen {

	private final List<Entry> entries = new ObjectArrayList<>();
	private int index = 0;

	public ViewOriginScreen() {
		super(Component.empty(), false);
	}

	@Override
	protected void init() {
		Minecraft client = Minecraft.getInstance();

		super.init();
		assert client != null && client.player != null : "Tried initializing the view origin screen with the client and its player unset!";

		Map<OriginLayer, Origin> origins = ModComponents.ORIGIN.get(client.player).getOrigins();
		this.entries.clear();

		for (var entry : origins.entrySet()) {

			OriginLayer layer = entry.getKey();
			Origin origin = entry.getValue();

			if (!layer.isEnabled() || layer.isHidden() || (origin == Origin.EMPTY && layer.getOriginOptionCount(client.player) <= 0)) {
				continue;
			}

			ItemStack icon = origin.getDisplayItem();
			this.entries.add(new Entry(layer, origin));

			if (icon.getItem() instanceof PlayerHeadItem && !icon.has(DataComponents.PROFILE)) {
				icon.set(DataComponents.PROFILE, client.player.getProfile());
			}

		}

		this.addRenderableWidget(Button.builder(Component.translatable("origins.gui.close"), button -> Objects.requireNonNull(client).gui.setScreen(null))
			.pos(windowWidget.getX() + windowWidget.getWidth() / 2 - 50, windowWidget.getY() + windowWidget.getHeight() + 5)
			.size(100, 20)
			.build());

		if (this.entries.isEmpty() || !OriginsClient.isServerRunningOrigins) {
			return;
		}

		this.entries.sort(Entry::compareTo);
		this.showCurrent();

		if (this.entries.size() <= 1) {
			return;
		}

		this.addRenderableWidget(Button.builder(Component.literal("<"), button -> this.previousLayer())
			.pos(windowWidget.getX() - 40, height / 2 - 10)
			.size(20, 20)
			.build());
		this.addRenderableWidget(Button.builder(Component.literal(">"), button -> this.nextLayer())
			.pos(windowWidget.getX() + windowWidget.getWidth() + 20, height / 2 - 10)
			.size(20, 20)
			.build());

	}

	@Override
	public Component getTitle() {

		if (entries.isEmpty()) {
			return super.getTitle();
		}

		else {
			return this.getCurrentLayer().getViewOriginTitle();
		}

	}

	@Override
	protected OriginLayer getCurrentLayer() {
		return entries.get(index).layer();
	}

	@Override
	protected Origin getCurrentOrigin() {
		return entries.get(index).origin();
	}

	void showCurrent() {
		this.showCurrent(origin -> origin.getGuiMetadata().viewing());
	}

	void resetAndShowCurrent() {
		this.resetAndShowCurrent(origin -> origin.getGuiMetadata().viewing());
	}

	void nextLayer() {
		this.index = Mth.positiveModulo(index + 1, this.entries.size());
		this.resetAndShowCurrent();
	}

	void previousLayer() {
		this.index = Mth.positiveModulo(index - 1, this.entries.size());
		this.resetAndShowCurrent();
	}

	private record Entry(OriginLayer layer, Origin origin) implements Comparable<Entry> {

		@Override
		public int compareTo(@NotNull ViewOriginScreen.Entry that) {
			return this.layer().compareTo(that.layer());
		}

	}

}
