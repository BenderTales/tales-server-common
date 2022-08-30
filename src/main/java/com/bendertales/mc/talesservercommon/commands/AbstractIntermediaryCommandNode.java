package com.bendertales.mc.talesservercommon.commands;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.server.command.ServerCommandSource;

import static net.minecraft.server.command.CommandManager.literal;


public abstract class AbstractIntermediaryCommandNode implements TalesCommandNode {

	private final List<TalesCommandNode> children;
	private final CommandNodeRequirements requirements;

	protected AbstractIntermediaryCommandNode(List<TalesCommandNode> children) {
		this.children = children;
		this.requirements = buildRequirementsBaseOnChildren();
	}

	protected abstract String getName();

	@Override
	public LiteralArgumentBuilder<ServerCommandSource> asBrigadierNode() {
		var node = literal(getName())
                    .requires(getRequirements().asPredicate());

		getChildrenNodes().forEach(node::then);
		return node;
	}

	protected final Stream<TalesCommandNode> getChildren() {
		return children.stream();
	}

	protected final Stream<LiteralArgumentBuilder<ServerCommandSource>> getChildrenNodes() {
		return getChildren()
	        .map(TalesCommandNode::asBrigadierNode);
	}

	protected final CommandNodeRequirements buildRequirementsBaseOnChildren() {
		boolean hasRequirements = true;

		int bypassOpLevel = OP_FULL;
		Set<String> permissions = new HashSet<>();

		for (var iterator = children.iterator();
		     hasRequirements && iterator.hasNext();) {
			var child = iterator.next();
			var childRequirements = child.getRequirements();
			hasRequirements = childRequirements.hasRequirements();
			bypassOpLevel = Math.min(bypassOpLevel, childRequirements.bypassOpLevel());
			permissions.addAll(childRequirements.permissions());
		}

		if (!hasRequirements) {
			return CommandNodeRequirements.noRequirements();
		}

		return CommandNodeRequirements.of(bypassOpLevel, permissions);
	}

	@Override
	public CommandNodeRequirements getRequirements() {
		return requirements;
	}
}
