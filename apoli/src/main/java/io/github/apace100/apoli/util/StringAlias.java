package io.github.apace100.apoli.util;

import io.github.apace100.apoli.Apoli;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

import java.util.Map;

public final class StringAlias {

	public static final StringAlias GLOBAL = new StringAlias();
	private final Map<String, String> aliases = new Object2ObjectOpenHashMap<>();

	public void addAlias(String fromString, String toString) {

		if (aliases.containsKey(fromString)) {
			String owner = aliases.get(fromString);
			Apoli.LOGGER.error("Couldn't add alias \"{}\" to string \"{}\": {}", fromString, toString, (owner.equals(toString) ? "it's already defined!" : "it's already being used by string \"" + owner + "\""));
		}

		else {
			aliases.put(fromString, toString);
		}

	}
	
	public boolean hasAlias(String str) {
		return aliases.containsKey(str)
			|| (this != GLOBAL && GLOBAL.hasAlias(str));
	}

	public String resolveAlias(String str) {

		if (aliases.containsKey(str)) {
			return aliases.get(str);
		}

		else if (this != GLOBAL) {
			return GLOBAL.resolveAlias(str);
		}

		else {
			throw new IllegalArgumentException("Tried resolving non-existent alias for string \"" + str + "\"");
		}

	}

}
