package fr.bendertales.mc.talesservercommon.repository.data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import fr.bendertales.mc.talesservercommon.repository.ModPaths;
import net.minecraft.server.network.ServerPlayerEntity;


public abstract class AbstractPlayerDataRepository<DATA, FILE_CONTENT>
		extends AbstractCachedDataRepository<ServerPlayerEntity, UUID, DATA, FILE_CONTENT> {

	private final Path dataFolder;

	public AbstractPlayerDataRepository(String modId, Class<FILE_CONTENT> fileClass) {
		super(fileClass);
		this.dataFolder = ModPaths.createDataFolder(modId, "players");
	}

	@Override
	protected Path getFilePath(ServerPlayerEntity player) {
		return getFilePath(player.getUuid());
	}

	private Path getFilePath(UUID playerId) {
		return dataFolder.resolve(playerId.toString() + ".json");
	}

	@Override
	protected UUID keyToId(ServerPlayerEntity serverPlayerEntity) {
		return serverPlayerEntity.getUuid();
	}

	public DATA get(UUID playerId) {
		return cachedData.computeIfAbsent(playerId, (k) -> this.load(playerId));
	}

	public DATA load(UUID playerId) {
		var fileContent = tryLoadContent(playerId);
		return convert(fileContent);
	}

	public void save(UUID playerId, DATA data) {
		var fileContent = deconvert(data);
		trySaveContent(playerId, fileContent);
	}

	private FILE_CONTENT tryLoadContent(UUID playerId) {
		try {
			return loadContent(playerId);
		}
		catch (IOException e) {
			return getDefaultConfiguration();
		}
	}

	private FILE_CONTENT loadContent(UUID playerId) throws IOException {
		var dataFile = getFilePath(playerId);
		if (Files.exists(dataFile)) {
			var playerJson = Files.readString(dataFile);
			return gson.fromJson(playerJson, fileClass);
		}

		return getDefaultConfiguration();
	}

	private void trySaveContent(UUID playerId, FILE_CONTENT content) {
		try {
			saveContent(playerId, content);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void saveContent(UUID playerId, FILE_CONTENT content) throws IOException {
		var dateFile = getFilePath(playerId);
		if (!Files.exists(dateFile.getParent())) {
			Files.createDirectories(dateFile.getParent());
		}

		var json = gson.toJson(content);
		Files.writeString(dateFile, json);
	}
}
