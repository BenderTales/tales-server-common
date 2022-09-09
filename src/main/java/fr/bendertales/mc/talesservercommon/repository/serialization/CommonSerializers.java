package fr.bendertales.mc.talesservercommon.repository.serialization;

import net.minecraft.util.Identifier;


public class CommonSerializers {

	public static JsonSerializerRegistration<Identifier> identifier() {
		return new JsonSerializerRegistration<>(Identifier.class, new IdentifierSerializer());
	}
}
