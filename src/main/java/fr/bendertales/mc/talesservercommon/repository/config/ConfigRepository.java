package fr.bendertales.mc.talesservercommon.repository.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import fr.bendertales.mc.talesservercommon.repository.serialization.JsonSerializerRegistration;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public abstract class ConfigRepository<CONFIG, FILE_CONTENT> {

	private final Class<FILE_CONTENT>    fileClass;
	private final Path                   configFile;
	private final Gson gson;

	public ConfigRepository(Class<FILE_CONTENT> fileClass,
	                 Path configPath) {
		this.fileClass = fileClass;
		this.configFile = configPath;
		this.gson = createGson();
	}

	protected abstract FILE_CONTENT getDefaultConfiguration();
	protected abstract boolean checkFileContent(FILE_CONTENT fileContent);
	protected abstract List<JsonSerializerRegistration<?>> getSerializers();
	protected abstract CONFIG convert(FILE_CONTENT fileContent);

	public CONFIG getConfig() {
		var fileContent = tryReadConfiguration();
		updateFileIfNecessary(fileContent);
		return convert(fileContent);
	}

	private void updateFileIfNecessary(FILE_CONTENT fileContent) {
		var wasValid = checkFileContent(fileContent);

		if (!wasValid || !Files.exists(configFile)) {
			tryWriteConfiguration(fileContent);
		}
	}

	private Gson createGson() {
		var gsonBuilder = new GsonBuilder().setPrettyPrinting();

		for (JsonSerializerRegistration<?> registration : getSerializers()) {
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
			return getDefaultConfiguration();
		}
	}

	private FILE_CONTENT readConfiguration() throws IOException {
		if (!Files.exists(configFile)) {
			return getDefaultConfiguration();
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
