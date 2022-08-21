package com.bendertales.mc.talesservercommon.repository.serialization;

import com.google.gson.JsonSerializer;


public record JsonSerializerRegistration<T> (
		Class<T> type,
		JsonSerializer<T> serializer
) {
}
