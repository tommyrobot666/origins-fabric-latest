package io.github.apace100.apoli.text;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.apace100.apoli.mixin.TranslatableTextContentAccessor;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.network.chat.contents.TranslatableFormatException;

import java.util.List;
import java.util.Optional;

public class ForcedTranslatableTextContent extends TranslatableContents {

	public static final MapCodec<ForcedTranslatableTextContent> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		Codec.STRING.fieldOf("translate").forGetter(ForcedTranslatableTextContent::getKey),
		ComponentSerialization.CODEC.fieldOf("alt_text").forGetter(ForcedTranslatableTextContent::getTextFallback),
		TranslatableTextContentAccessor.getArgumentCodec().listOf().optionalFieldOf("with").forGetter(content -> TranslatableTextContentAccessor.callToOptionalList(content.getArgs()))
	).apply(instance, ForcedTranslatableTextContent::new));

//	public static final Type<ForcedTranslatableTextContent> TYPE = new Type<>(CODEC, "apoli:forced_translatable");

	private final Component textFallback;

	public ForcedTranslatableTextContent(String key, Component textFallback, Object... args) {
		super(key, null, args);
		this.textFallback = textFallback;
	}

	private ForcedTranslatableTextContent(String key, Component textFallback, Optional<List<Object>> args) {
		this(key, textFallback, TranslatableTextContentAccessor.callAdjustArgs(args));
	}

	@Override
	public MapCodec<TranslatableContents> codec() {
		return (MapCodec<TranslatableContents>) ((Object) CODEC);
	}

	@Override
	protected void decompose() {

		Language language = Language.getInstance();
		if (language == this.decomposedWith) {
			return;
		}

		String key = this.getKey();
		String translated = language.getOrDefault(key);

		this.decomposedWith = language;

		if (language.has(key)) {

			try {

				ImmutableList.Builder<FormattedText> builder = ImmutableList.builder();
				((TranslatableTextContentAccessor) this).callDecomposeTemplate(translated, builder::add);

				this.decomposedParts = builder.build();

			}

			catch (TranslatableFormatException te) {
				this.decomposedParts = ImmutableList.of(FormattedText.of(translated));
			}

		}

		else {
			this.decomposedParts = ImmutableList.of(getTextFallback());
		}

	}

	public Component getTextFallback() {
		return textFallback;
	}

}
