package com.bendertales.mc.talesservercommon.commands;

import java.util.Collection;
import java.util.function.Predicate;

import com.bendertales.mc.talesservercommon.helpers.Perms;
import net.minecraft.server.command.ServerCommandSource;


public class TalesCommands {

	private TalesCommands() {}

	public static Predicate<ServerCommandSource> buildRequirementsFor
		(int opLevel, Collection<String> permissions) {

		return (cmdSource) -> cmdSource.hasPermissionLevel(opLevel)
		                      || Perms.hasAny(cmdSource, permissions);
	}
}
