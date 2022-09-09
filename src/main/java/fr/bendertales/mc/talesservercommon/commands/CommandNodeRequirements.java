package fr.bendertales.mc.talesservercommon.commands;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Predicate;

import net.minecraft.server.command.ServerCommandSource;


public record CommandNodeRequirements(
	boolean hasRequirements,
	int bypassOpLevel,
	Collection<String> permissions
) {

	private static final CommandNodeRequirements NO_REQUIREMENTS
			= new CommandNodeRequirements(false, 0, Collections.emptyList());

	public static CommandNodeRequirements noRequirements() {
		return NO_REQUIREMENTS;
	}

	public static CommandNodeRequirements of(int bypassOpLevel, Collection<String> permissions) {
		return new CommandNodeRequirements(true, bypassOpLevel, permissions);
	}

	public Predicate<ServerCommandSource> asPredicate() {
		if (!hasRequirements) {
			return (src) -> true;
		}

		return TalesCommands.buildRequirementsFor(bypassOpLevel, permissions);
	}
}
