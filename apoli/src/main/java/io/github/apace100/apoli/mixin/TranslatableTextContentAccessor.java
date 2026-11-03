package io.github.apace100.apoli.mixin;

import com.mojang.serialization.Codec;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.contents.TranslatableContents;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Mixin(TranslatableContents.class)
public interface TranslatableTextContentAccessor {

	@Final
	@Accessor("ARG_CODEC")
	static Codec<Object> getArgumentCodec() {
		throw new AssertionError();
	}

	@Invoker
	static Optional<List<Object>> callAdjustArgs(Object[] args) {
		throw new AssertionError();
	}

	@Invoker
	static Object[] callAdjustArgs(Optional<List<Object>> args) {
		throw new AssertionError();
	}

	@Invoker
	void callDecomposeTemplate(String translation, Consumer<FormattedText> partsConsumer);

}
