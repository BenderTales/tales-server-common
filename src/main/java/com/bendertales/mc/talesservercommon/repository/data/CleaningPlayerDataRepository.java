package com.bendertales.mc.talesservercommon.repository.data;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.bendertales.mc.talesservercommon.repository.BidirectionnalConverter;
import com.bendertales.mc.talesservercommon.repository.serialization.JsonSerializerRegistration;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;


public class CleaningPlayerDataRepository<DATA, FILE_CONTENT>
		extends CleaningCachedDataRepository<ServerPlayerEntity, UUID, DATA, FILE_CONTENT> {

	public CleaningPlayerDataRepository(
			Class<FILE_CONTENT> fileClass,
            Supplier<FILE_CONTENT> defaultFileContentSupplier,
            String modId,
            List<JsonSerializerRegistration<?>> serializerRegistrations,
	        BidirectionnalConverter<FILE_CONTENT, DATA> dataConverter) {
		super(fileClass, defaultFileContentSupplier,
		      new FabricPlayerDataPathProvider(modId),
		      serializerRegistrations, ServerPlayerEntity::getUuid,
		      dataConverter);
	}

	@Override
	public Set<UUID> getCleaningRetainedIds(MinecraftServer minecraftServer) {
		return minecraftServer.getPlayerManager().getPlayerList().stream()
                  .map(ServerPlayerEntity::getUuid)
                  .collect(Collectors.toSet());
	}
}
