package com.bendertales.mc.talesservercommon.repository.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Supplier;

import com.bendertales.mc.talesservercommon.repository.FileContentChecker;
import com.bendertales.mc.talesservercommon.repository.Converter;
import com.bendertales.mc.talesservercommon.repository.serialization.JsonSerializerRegistration;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class ConfigRepository<CONFIG, FILE_CONTENT> {

	private final Class<FILE_CONTENT>    fileClass;
	private final Path                   configFile;
	private final Gson gson;

	private final Supplier<FILE_CONTENT>                   defaultFileContentSupplier;
	private final FileContentChecker<? super FILE_CONTENT> fileContentChecker;

	private final Converter<FILE_CONTENT, CONFIG> converter;

	public static<CFG, FC> ConfigRepositoryBuilder<CFG, FC> newBuilder() {
		return new ConfigRepositoryBuilder<>();
	}

	ConfigRepository(Class<FILE_CONTENT> fileClass,
	                 Supplier<FILE_CONTENT> defaultFileContentSupplier,
	                 FileContentChecker<? super FILE_CONTENT> fileContentChecker,
	                 Supplier<Path> configPathProvider,
	                 List<JsonSerializerRegistration<?>> serializerRegistrations,
	                 Converter<FILE_CONTENT, CONFIG> converter) {
		this.fileClass = fileClass;
		this.defaultFileContentSupplier = defaultFileContentSupplier;
		this.fileContentChecker = fileContentChecker;
		this.configFile = configPathProvider.get();
		this.gson = createGson(serializerRegistrations);
		this.converter = converter;
	}

	public CONFIG getConfig() {
		var fileContent = tryReadConfiguration();
		updateFileIfNecessary(fileContent);
		return converter.convert(fileContent);
	}

	private void updateFileIfNecessary(FILE_CONTENT fileContent) {
		var wasValid = fileContentChecker.check(fileContent);

		if (!wasValid || !Files.exists(configFile)) {
			tryWriteConfiguration(fileContent);
		}
	}

	private Gson createGson(List<JsonSerializerRegistration<?>> serializerRegistrations) {
		var gsonBuilder = new GsonBuilder().setPrettyPrinting();

		for (JsonSerializerRegistration<?> registration : serializerRegistrations) {
			gsonBuilder.registerTypeAdapter(registration.type(), registration.serializer());
		}

		return gsonBuilder.create();
	}

	private FILE_CONTENT tryReadConfiguration() {
		try {
			return readConfiguration();
		}
		catch (IOException e) {
			e.printStackTrace();
			return defaultConfiguration();
		}
	}

	private FILE_CONTENT defaultConfiguration() {
		return defaultFileContentSupplier.get();
	}

	private FILE_CONTENT readConfiguration() throws IOException {
		if (!Files.exists(configFile)) {
			return defaultConfiguration();
		}

		var fileContent = Files.readString(configFile);
		return gson.fromJson(fileContent, fileClass);
	}

	private void tryWriteConfiguration(FILE_CONTENT modProperties) {
		try {
			writeConfiguration(modProperties);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void writeConfiguration(FILE_CONTENT modProperties) throws IOException {
		if (!Files.exists(configFile)) {
			Files.createDirectories(configFile.getParent());
		}

		var configurationJson = gson.toJson(modProperties, fileClass);
		Files.writeString(configFile, configurationJson);
	}
}
