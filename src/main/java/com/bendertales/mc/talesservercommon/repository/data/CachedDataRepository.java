package com.bendertales.mc.talesservercommon.repository.data;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.bendertales.mc.talesservercommon.repository.BidirectionnalConverter;
import com.bendertales.mc.talesservercommon.repository.Converter;
import com.bendertales.mc.talesservercommon.repository.serialization.JsonSerializerRegistration;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;


public class CachedDataRepository<KEY, ID, DATA, FILE_CONTENT> extends DataRepository<KEY, FILE_CONTENT, DATA> {

	private final Object2ObjectMap<ID, DATA> cachedData = new Object2ObjectOpenHashMap<>();
	private final Converter<KEY, ID> keyConverter;

	public CachedDataRepository(
			Class<FILE_CONTENT> fileClass,
            Supplier<FILE_CONTENT> defaultFileContentSupplier,
            DataPathProvider<KEY> dataPathProvider,
            List<JsonSerializerRegistration<?>> serializerRegistrations,
            Converter<KEY, ID> keyConverter,
            BidirectionnalConverter<FILE_CONTENT, DATA> dataConverter) {
		super(fileClass, defaultFileContentSupplier, dataPathProvider, serializerRegistrations, dataConverter);
		this.keyConverter = keyConverter;
	}

	public DATA get(KEY key) {
		var id = keyConverter.convert(key);
		return cachedData.computeIfAbsent(id, (k) -> this.load(key));
	}

	public void clearCache() {
		cachedData.clear();
	}

	public void clearCache(Set<ID> retainedIds) {
		cachedData.keySet().retainAll(retainedIds);
	}

	public void update(KEY key, Consumer<DATA> dataUpdater) {
		var data = get(key);
		dataUpdater.accept(data);
		save(key, data);
	}

}
