package com.bendertales.mc.talesservercommon.repository.data;

import java.util.Set;
import java.util.function.Consumer;


public interface ICachedDataRepository<KEY, ID, DATA> extends IDataRepository<KEY, DATA> {

	DATA get(KEY key);

	void clearCache();

	void clearCache(Set<ID> retainedIds);

	default void update(KEY key, Consumer<DATA> dataUpdater) {
		var data = get(key);
		dataUpdater.accept(data);
		save(key, data);
	}
}
