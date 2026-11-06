package io.github.apace100.origins.integration;

import carpet.patches.EntityPlayerMPFake;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.level.ServerPlayer;

public class CarpetIntegration {

	public static boolean isPlayerFake(ServerPlayer serverPlayer) {
		return FabricLoader.getInstance().isModLoaded("carpet")
			&& serverPlayer instanceof EntityPlayerMPFake;
	}

}
