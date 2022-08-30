package com.bendertales.mc.talesservercommon.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.server.command.ServerCommandSource;


public interface TalesCommandNode {

	int ERROR = -1;
	int SUCCESS = 0;

	int OP_JUNIOR = 1;
	int OP_MEDIOR = 2;
	int OP_SENIOR = 3;
	int OP_FULL   = 4;

	LiteralArgumentBuilder<ServerCommandSource> asBrigadierNode();
	CommandNodeRequirements getRequirements();
}
