package io.github.apace100.origins.util;

import io.github.apace100.apoli.util.TextAlignment;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.util.Util;

import java.util.Optional;

public class MiscUtilClient {

	public static void drawScrollingText(GuiGraphicsExtractor graphics, Font renderer, Component text, TextAlignment alignment, int startX, int startY, int endX, int endY, int color, boolean shadow) {

		int textWidth = renderer.width(text);

		int height = (startY + endY - 9) / 2 + 1;
		int width = endX - startX;

		Optional<Integer> horizontalAlignment = alignment.horizontal(startX, endX, textWidth);

		if (textWidth <= width && horizontalAlignment.isPresent()) {
			graphics.text(renderer, text, horizontalAlignment.get(), height, color, shadow);
		}

		else {

			int horizontalDiff = textWidth - width;

			double d = (double) Util.getMillis() / 300;
			double e = Math.max((double) horizontalDiff * 0.5, 3.0);
			double f = Math.sin((Math.PI / 2) * Math.cos((Math.PI * 2) * d / e)) / 2.0 + 0.5;
			double g = Mth.lerp(f, 0.0, horizontalDiff);

			graphics.enableScissor(startX, startY, endX, endY);
			graphics.text(renderer, text, startX - (int) g, height, color, shadow);
			graphics.disableScissor();

		}

	}

}
