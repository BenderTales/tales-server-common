package com.bendertales.mc.talesservercommon.repository.config;

import java.nio.file.Path;
import java.util.function.Supplier;

import net.fabricmc.loader.api.FabricLoader;


public class FabricConfigPathProvider implements Supplier<Path> {

	private final String modId;
	private final String fileName;

	public static FabricConfigPathProvider basic(String modId) {
		return of(modId, "config.json");
	}

	public static FabricConfigPathProvider of(String modId, String fileName) {
		return new FabricConfigPathProvider(modId, fileName);
	}

	private FabricConfigPathProvider(String modId, String fileName) {
		this.modId = modId;
		this.fileName = fileName;
	}

	@Override
	public Path get() {
		return FabricLoader.getInstance().getConfigDir()
		                   .resolve(modId).resolve(fileName);
	}
}
