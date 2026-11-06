package io.github.apace100.origins.origin;

import io.github.apace100.origins.Origins;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;

public enum Impact {
	
	NONE(0, "none", ChatFormatting.GRAY, Origins.identifier("choose_origin/impact/none")),
	LOW(1, "low", ChatFormatting.GREEN, Origins.identifier("choose_origin/impact/low")),
	MEDIUM(2, "medium", ChatFormatting.YELLOW, Origins.identifier("choose_origin/impact/medium")),
	HIGH(3, "high", ChatFormatting.RED, Origins.identifier("choose_origin/impact/high"));

	public static final int WIDTH = 28;
	public static final int HEIGHT = 8;

	private final int impactValue;
	private final String translationKey;
	private final ChatFormatting textStyle;
	private final Identifier spriteId;

	Impact(int impactValue, String translationKey, ChatFormatting textStyle, Identifier spriteId) {
		this.translationKey = "origins.gui.impact." + translationKey;
		this.impactValue = impactValue;
		this.textStyle = textStyle;
		this.spriteId = spriteId;
	}

	public Identifier getSpriteId() {
		return spriteId;
	}

	public int getImpactValue() {
		return impactValue;
	}
	
	public String getTranslationKey() {
		return translationKey;
	}
	
	public ChatFormatting getTextStyle() {
		return textStyle;
	}
	
	public MutableComponent getTextComponent() {
		return Component.translatable(getTranslationKey()).withStyle(getTextStyle());
	}
	
	public static Impact getByValue(int impactValue) {
		return Impact.values()[impactValue];
	}
}
