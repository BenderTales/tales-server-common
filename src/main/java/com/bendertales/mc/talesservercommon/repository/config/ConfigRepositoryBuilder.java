package com.bendertales.mc.talesservercommon.repository.config;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import com.bendertales.mc.talesservercommon.repository.FileContentChecker;
import com.bendertales.mc.talesservercommon.repository.Converter;
import com.bendertales.mc.talesservercommon.repository.NoopFileContentChecker;
import com.bendertales.mc.talesservercommon.repository.serialization.JsonSerializerRegistration;
import com.google.gson.JsonSerializer;


public class ConfigRepositoryBuilder<CFG, FILE> {
	private Class<FILE>                      fileClass;
	private Supplier<FILE>                   defaultConfigSupplier;
	private FileContentChecker<? super FILE> fileContentChecker = new NoopFileContentChecker();

	private Supplier<Path> configPathProvider;
	private final List<JsonSerializerRegistration<?>> serializerRegistrations = new ArrayList<>();

	private Converter<FILE, CFG> converter;

	ConfigRepositoryBuilder() {
	}

	public ConfigRepositoryBuilder<CFG, FILE> fileClass(Class<FILE> fileClass) {
		this.fileClass = fileClass;
		return this;
	}

	public ConfigRepositoryBuilder<CFG, FILE> defaultConfigSupplier(Supplier<FILE> defaultConfigSupplier) {
		this.defaultConfigSupplier = defaultConfigSupplier;
		return this;
	}

	public ConfigRepositoryBuilder<CFG, FILE> fileContentChecker(FileContentChecker<? super FILE> fileContentChecker) {
		this.fileContentChecker = fileContentChecker;
		return this;
	}

	public ConfigRepositoryBuilder<CFG, FILE> configPathProvider(Supplier<Path> configPathProvider) {
		this.configPathProvider = configPathProvider;
		return this;
	}

	public <T> ConfigRepositoryBuilder<CFG, FILE> addSerializerRegistration(Class<T> type, JsonSerializer<T> serializer) {
		return addSerializerRegistration(new JsonSerializerRegistration<>(type, serializer));
	}

	public ConfigRepositoryBuilder<CFG, FILE> addSerializerRegistration(JsonSerializerRegistration<?> serializerRegistration) {
		this.serializerRegistrations.add(serializerRegistration);
		return this;
	}

	public ConfigRepositoryBuilder<CFG, FILE> fileContentToConfigConverter(
			Converter<FILE, CFG> converter) {
		this.converter = converter;
		return this;
	}

	public ConfigRepository<CFG, FILE> build() {
		return new ConfigRepository<>(
				fileClass,
                defaultConfigSupplier,
                fileContentChecker,
                configPathProvider,
                serializerRegistrations, converter);
	}
}
