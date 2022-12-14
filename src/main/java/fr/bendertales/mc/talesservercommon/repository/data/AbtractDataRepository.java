package fr.bendertales.mc.talesservercommon.repository.data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import fr.bendertales.mc.talesservercommon.repository.serialization.JsonSerializerRegistration;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public abstract class AbtractDataRepository<KEY, FILE_CONTENT, DATA>
	implements  IDataRepository<KEY, DATA> {

	protected final Class<FILE_CONTENT>    fileClass;

	protected final Gson gson;

	public AbtractDataRepository(Class<FILE_CONTENT> fileClass) {
		this.fileClass = fileClass;

		this.gson = createGson();
	}

	protected abstract List<JsonSerializerRegistration<?>> getSerializers();
	protected abstract DATA convert(FILE_CONTENT fileContent);
	protected abstract FILE_CONTENT deconvert(DATA data);
	protected abstract FILE_CONTENT getDefaultConfiguration();
	protected abstract Path getFilePath(KEY key);

	private Gson createGson() {
		var gsonBuilder = new GsonBuilder().setPrettyPrinting();

		for (JsonSerializerRegistration<?> registration : getSerializers()) {
			gsonBuilder.registerTypeAdapter(registration.type(), registration.serializer());
		}

		return gsonBuilder.create();
	}

	public DATA load(KEY key) {
		var fileContent = tryLoadContent(key);
		return convert(fileContent);
	}

	public void save(KEY key, DATA data) {
		var fileContent = deconvert(data);
		trySaveContent(key, fileContent);
	}

	private FILE_CONTENT tryLoadContent(KEY key) {
		try {
			return loadContent(key);
		}
		catch (IOException e) {
			return getDefaultConfiguration();
		}
	}

	private FILE_CONTENT loadContent(KEY key) throws IOException {
		var dataFile = getFilePath(key);
		if (Files.exists(dataFile)) {
			var playerJson = Files.readString(dataFile);
			return gson.fromJson(playerJson, fileClass);
		}

		return getDefaultConfiguration();
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
		var dateFile = getFilePath(key);
		if (!Files.exists(dateFile.getParent())) {
			Files.createDirectories(dateFile.getParent());
		}

		var json = gson.toJson(content);
		Files.writeString(dateFile, json);
	}

}
