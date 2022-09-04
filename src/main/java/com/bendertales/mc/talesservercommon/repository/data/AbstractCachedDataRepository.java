package com.bendertales.mc.talesservercommon.repository.data;

import java.util.Set;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;


public abstract class AbstractCachedDataRepository<KEY, ID, DATA, FILE_CONTENT>
		extends AbtractDataRepository<KEY, FILE_CONTENT, DATA>
		implements ICachedDataRepository<KEY, ID, DATA> {

	protected final Object2ObjectMap<ID, DATA> cachedData = new Object2ObjectOpenHashMap<>();

	public AbstractCachedDataRepository(Class<FILE_CONTENT> fileClass) {
		super(fileClass);
	}

	protected abstract ID keyToId (KEY key);

	public DATA get(KEY key) {
		var id = keyToId(key);
		return cachedData.computeIfAbsent(id, (k) -> this.load(key));
	}

	public void clearCache() {
		cachedData.clear();
	}

	public void clearCache(Set<ID> retainedIds) {
		cachedData.keySet().retainAll(retainedIds);
	}

}
