package com.bendertales.mc.talesservercommon.repository.data;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import com.bendertales.mc.talesservercommon.repository.BidirectionnalConverter;
import com.bendertales.mc.talesservercommon.repository.Converter;
import com.bendertales.mc.talesservercommon.repository.serialization.JsonSerializerRegistration;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;


public class CleaningCachedDataRepository<KEY, ID, DATA, FILE_CONTENT>
		extends CachedDataRepository<KEY, ID, DATA, FILE_CONTENT> {

	public CleaningCachedDataRepository(
			Class<FILE_CONTENT> fileClass,
            Supplier<FILE_CONTENT> defaultFileContentSupplier,
            DataPathProvider<KEY> dataPathProvider,
            List<JsonSerializerRegistration<?>> serializerRegistrations,
            Converter<KEY, ID> keyConverter,
            BidirectionnalConverter<FILE_CONTENT, DATA> dataConverter) {
		super(fileClass, defaultFileContentSupplier,
		      dataPathProvider, serializerRegistrations,
		      keyConverter, dataConverter);
	}

	public Set<ID> getCleaningRetainedIds(MinecraftServer minecraftServer) {
		return Collections.emptySet();
	}

	public void registerForSelfCleanup() {
		ServerTickEvents.END_SERVER_TICK.register(this::cleanup);
	}

	private void cleanup(MinecraftServer minecraftServer) {
		if (isTimeForCleanup()) {
			var retainedIds = getCleaningRetainedIds(minecraftServer);
			clearCache(retainedIds);
		}
	}

	private boolean isTimeForCleanup() {
		return false;
	}
}
