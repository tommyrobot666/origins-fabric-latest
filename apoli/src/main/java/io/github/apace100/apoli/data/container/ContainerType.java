package io.github.apace100.apoli.data.container;

import io.github.apace100.apoli.util.TextAlignment;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.ScreenHandlerFactory;
import org.jetbrains.annotations.Range;

public interface ContainerType {

	TextAlignment titleAlignment();

	ScreenHandlerFactory create(Inventory inventory);

	default int size() {
		return columns() * rows();
	}

	@Range(from = 1, to = Integer.MAX_VALUE)
	int columns();

	@Range(from = 1, to = Integer.MAX_VALUE)
	int rows();

}
