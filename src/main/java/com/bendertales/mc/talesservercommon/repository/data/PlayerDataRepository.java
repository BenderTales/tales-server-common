package com.bendertales.mc.talesservercommon.repository.data;

import java.nio.file.Path;
import java.util.UUID;

import com.bendertales.mc.talesservercommon.repository.ModPaths;
import net.minecraft.server.network.ServerPlayerEntity;


public abstract class PlayerDataRepository<DATA, FILE_CONTENT>
		extends AbstractCachedDataRepository<ServerPlayerEntity, UUID, DATA, FILE_CONTENT> {

	private final Path dataFolder;

	public PlayerDataRepository(String modId, Class<FILE_CONTENT> fileClass) {
		super(fileClass);
		this.dataFolder = ModPaths.createDataFolder(modId, "players");
	}

	@Override
	protected Path getFilePath(ServerPlayerEntity player) {
		return dataFolder.resolve(player.getUuid().toString() + ".json");
	}
}
