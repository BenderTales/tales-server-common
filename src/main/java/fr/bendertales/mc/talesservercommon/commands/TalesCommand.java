package fr.bendertales.mc.talesservercommon.commands;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;


public interface TalesCommand {
	default CommandSyntaxException asCommandException(Exception e) {
		return new SimpleCommandExceptionType(
			Text.literal(e.getMessage()).formatted(Formatting.RED)
		).create();
	}
}
