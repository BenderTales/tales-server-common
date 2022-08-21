package com.bendertales.mc.talesservercommon.repository.data;

import java.nio.file.Path;

import net.fabricmc.loader.api.FabricLoader;
import org.apache.commons.lang3.StringUtils;


public abstract class AbstractFabricDataPathProvider<T> implements DataPathProvider<T> {

	protected final Path dataFolder;

	public AbstractFabricDataPathProvider(String modId) {
		this(modId, null);
	}

	public AbstractFabricDataPathProvider(String modId, String subFolder) {
		var targetFolder = FabricLoader.getInstance().getGameDir()
		                               .resolve("mods").resolve(modId);
		if (StringUtils.isNotBlank(subFolder)) {
			targetFolder = targetFolder.resolve(subFolder);
		}
		this.dataFolder = targetFolder;
	}

}
