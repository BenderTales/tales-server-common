package com.bendertales.mc.talesservercommon.repository.data;

import java.nio.file.Path;

import net.minecraft.server.network.ServerPlayerEntity;


public class FabricPlayerDataPathProvider extends AbstractFabricDataPathProvider<ServerPlayerEntity> {

	public FabricPlayerDataPathProvider(String modId) {
		this(modId, "players");
	}

	public FabricPlayerDataPathProvider(String modId, String subFolder) {
		super(modId, subFolder);
	}

	@Override
	public Path convert(ServerPlayerEntity player) {
		return dataFolder.resolve(player.getUuid().toString() + ".json");
	}
}
