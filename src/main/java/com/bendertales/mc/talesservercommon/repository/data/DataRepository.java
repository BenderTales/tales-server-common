package com.bendertales.mc.talesservercommon.repository.data;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.function.Supplier;

import com.bendertales.mc.talesservercommon.repository.BidirectionnalConverter;
import com.bendertales.mc.talesservercommon.repository.serialization.JsonSerializerRegistration;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class DataRepository<KEY, FILE_CONTENT, DATA> {

	private final Class<FILE_CONTENT>    fileClass;
	private final Supplier<FILE_CONTENT> defaultFileContentSupplier;
	private final DataPathProvider<KEY> dataPathProvider;

	private final Gson gson;
	private final BidirectionnalConverter<FILE_CONTENT, DATA> dataConverter;

	public DataRepository(Class<FILE_CONTENT> fileClass,
	                      Supplier<FILE_CONTENT> defaultFileContentSupplier,
	                      DataPathProvider<KEY> dataPathProvider,
	                      List<JsonSerializerRegistration<?>> serializerRegistrations,
	                      BidirectionnalConverter<FILE_CONTENT, DATA> dataConverter) {
		this.fileClass = fileClass;
		this.defaultFileContentSupplier = defaultFileContentSupplier;
		this.dataPathProvider = dataPathProvider;

		this.gson = createGson(serializerRegistrations);
		this.dataConverter = dataConverter;
	}

	private Gson createGson(List<JsonSerializerRegistration<?>> serializerRegistrations) {
		var gsonBuilder = new GsonBuilder().setPrettyPrinting();

		for (JsonSerializerRegistration<?> registration : serializerRegistrations) {
			gsonBuilder.registerTypeAdapter(registration.type(), registration.serializer());
		}

		return gsonBuilder.create();
	}

	public DATA load(KEY key) {
		var fileContent = tryLoadContent(key);
		return dataConverter.convert(fileContent);
	}

	public void save(KEY key, DATA data) {
		var fileContent = dataConverter.unconvert(data);
		trySaveContent(key, fileContent);
	}

	private FILE_CONTENT tryLoadContent(KEY key) {
		try {
			return loadContent(key);
		}
		catch (IOException e) {
			return defaultConfiguration();
		}
	}

	private FILE_CONTENT loadContent(KEY key) throws IOException {
		var dataFile = dataPathProvider.convert(key);
		if (Files.exists(dataFile)) {
			var playerJson = Files.readString(dataFile);
			return gson.fromJson(playerJson, fileClass);
		}

		return defaultConfiguration();
	}

	private void trySaveContent(KEY key, FILE_CONTENT content) {
		try {
			saveContent(key, content);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void saveContent(KEY key, FILE_CONTENT content) throws IOException {
		var dateFile = dataPathProvider.convert(key);
		if (!Files.exists(dateFile.getParent())) {
			Files.createDirectories(dateFile.getParent());
		}

		var json = gson.toJson(content);
		Files.writeString(dateFile, json);
	}

	private FILE_CONTENT defaultConfiguration() {
		return defaultFileContentSupplier.get();
	}


}
