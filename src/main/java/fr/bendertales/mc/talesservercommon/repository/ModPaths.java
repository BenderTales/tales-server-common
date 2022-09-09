package fr.bendertales.mc.talesservercommon.repository;

import java.nio.file.Path;

import net.fabricmc.loader.api.FabricLoader;
import org.apache.commons.lang3.StringUtils;


public class ModPaths {

	public static Path createDataFolder(String modId) {
		return createDataFolder(modId, null);
	}

	public static Path createDataFolder(String modId, String subFolder) {
		var targetFolder = FabricLoader.getInstance().getGameDir()
		                               .resolve("mods").resolve(modId);
		if (StringUtils.isNotBlank(subFolder)) {
			targetFolder = targetFolder.resolve(subFolder);
		}

		return targetFolder;
	}

	public static Path createConfigFolder(String modId) {
		return FabricLoader.getInstance().getConfigDir()
		                   .resolve(modId);
	}

	public static Path createConfigFile(String modId) {
		return createConfigFolder(modId).resolve("config.json");
	}
}
