package io.github.apace100.calio;

import com.google.common.collect.ImmutableMap;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Unit;
import org.jetbrains.annotations.ApiStatus;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class CalioServer {

	@ApiStatus.Internal
	public static final Map<TagKey<?>, Collection<RegistryAccess.RegistryEntry<?>>> REGISTRY_TAGS = new ConcurrentHashMap<>();
	@ApiStatus.Internal
	public static final Map<Unit, RegistryAccess> DYNAMIC_REGISTRIES = new ConcurrentHashMap<>();
	@ApiStatus.Internal
	public static final Map<Unit, ReloadableServerResources> DATA_PACK_CONTENTS = new ConcurrentHashMap<>();

	public static ImmutableMap<TagKey<?>, Collection<RegistryAccess.RegistryEntry<?>>> getRegistryTags() {
		return ImmutableMap.copyOf(REGISTRY_TAGS);
	}

	public static Optional<RegistryAccess> getDynamicRegistries() {
		return Optional.ofNullable(DYNAMIC_REGISTRIES.get(Unit.INSTANCE));
	}

	public static Optional<ReloadableServerResources> getDataPackContents() {
		return Optional.ofNullable(DATA_PACK_CONTENTS.get(Unit.INSTANCE));
	}

}
