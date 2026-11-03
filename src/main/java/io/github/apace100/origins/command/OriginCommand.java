package io.github.apace100.origins.command;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import io.github.apace100.origins.Origins;
import io.github.apace100.origins.command.argument.OriginLayerArgumentType;
import io.github.apace100.origins.command.argument.OriginArgumentType;
import io.github.apace100.origins.component.OriginComponent;
import io.github.apace100.origins.networking.packet.s2c.OpenChooseOriginScreenS2CPacket;
import io.github.apace100.origins.origin.*;
import io.github.apace100.origins.registry.ModComponents;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.UnknownNullability;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static com.mojang.brigadier.builder.RequiredArgumentBuilder.argument;
import static net.minecraft.commands.Commands.LEVEL_GAMEMASTERS;
import static net.minecraft.commands.Commands.literal;

public class OriginCommand {

	public static void register(@UnknownNullability RootCommandNode<CommandSourceStack> baseNode) {

		//	The main node of the command
		var originNode = literal("origin")
			.requires(source -> LEVEL_GAMEMASTERS.check(source.permissions()))
			.build();

		//	Add the sub-nodes as children of the main node
		originNode.addChild(SetNode.get());
		originNode.addChild(HasNode.get());
		originNode.addChild(GetNode.get());
		originNode.addChild(GuiNode.get());
		originNode.addChild(RandomNode.get());

		//	Add the main node as a child of the main node
		baseNode.addChild(originNode);

	}

	public static final class SetNode {

		public static CommandNode<CommandSourceStack> get() {
			return literal("set")
				.then(argument("targets", EntityArgument.players())
					.then(argument("layer", OriginLayerArgumentType.layer())
						.then(argument("origin", OriginArgumentType.origin())
							.executes(SetNode::execute)))).build();
		}

		public static int execute(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {

			List<ServerPlayer> targets = new ObjectArrayList<>(EntityArgument.getPlayers(context, "targets"));

			OriginLayer layer = OriginLayerArgumentType.getLayer(context, "layer");
			Origin origin = OriginArgumentType.getOrigin(context, "origin");

			CommandSourceStack source = context.getSource();
			int processedTargets = 0;

			if (origin.equals(Origin.EMPTY) || layer.getOrigins().contains(origin.getId())) {

				for (ServerPlayer target : targets) {

					OriginComponent originComponent = ModComponents.ORIGIN.get(target);
					boolean hadOriginBefore = originComponent.hadOriginBefore();

					originComponent.setOrigin(layer, origin);
					originComponent.sync();

					OriginComponent.partialOnChosen(target, hadOriginBefore, origin);
					processedTargets++;

				}

				if (processedTargets == 1) {
					source.sendSuccess(() -> Component.translatable("commands.origin.set.success.single", targets.getFirst().getName(), layer.getName(), origin.getName()), true);
				}

				else {
					int finalProcessedTargets = processedTargets;
					source.sendSuccess(() -> Component.translatable("commands.origin.set.success.multiple", finalProcessedTargets, layer.getName(), origin.getName()), true);
				}

			}

			else {
				source.sendFailure(Component.translatableEscape("commands.origin.unregistered_in_layer", origin.getId(), layer.getId()));
			}

			return processedTargets;

		}

	}

	public static final class HasNode {

		public static CommandNode<CommandSourceStack> get() {
			return literal("has")
				.then(argument("targets", EntityArgument.players())
					.then(argument("layer", OriginLayerArgumentType.layer())
						.then(argument("origin", OriginArgumentType.origin())
							.executes(HasNode::execute)))).build();
		}

		public static int execute(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {

			List<ServerPlayer> targets = new ObjectArrayList<>(EntityArgument.getPlayers(context, "targets"));

			OriginLayer layer = OriginLayerArgumentType.getLayer(context, "layer");
			Origin origin = OriginArgumentType.getOrigin(context, "origin");

			CommandSourceStack source = context.getSource();
			int processedTargets = 0;

			if (origin.equals(Origin.EMPTY) || layer.getOrigins().contains(origin.getId())) {

				for (ServerPlayer target : targets) {

					OriginComponent origincomponent = ModComponents.ORIGIN.get(target);

					if ((origin.equals(Origin.EMPTY) || origincomponent.hasOrigin(layer)) && origincomponent.getOrigin(layer).equals(origin)) {
						processedTargets++;
					}

				}

				if (processedTargets == 0) {
					source.sendFailure(Component.translatable("commands.execute.conditional.fail"));
				}

				else if (processedTargets == 1) {
					source.sendSuccess(() -> Component.translatable("commands.execute.conditional.pass"), false);
				}

				else {
					int finalProcessedTargets = processedTargets;
					source.sendSuccess(() -> Component.translatable("commands.execute.conditional.pass_count", finalProcessedTargets), false);
				}

			}

			else {
				source.sendFailure(Component.translatableEscape("commands.origin.unregistered_in_layer", origin.getId(), layer.getId()));
			}

			return processedTargets;

		}

	}

	public static final class GetNode {

		public static CommandNode<CommandSourceStack> get() {
			return literal("get")
				.then(argument("target", EntityArgument.player())
					.then(argument("layer", OriginLayerArgumentType.layer())
						.executes(GetNode::execute))).build();
		}

		public static int execute(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {

			ServerPlayer target = EntityArgument.getPlayer(context, "target");
			OriginLayer layer = OriginLayerArgumentType.getLayer(context, "layer");

			CommandSourceStack source = context.getSource();
			OriginComponent originComponent = ModComponents.ORIGIN.get(target);

			Origin origin = originComponent.getOrigin(layer);
			source.sendSuccess(() -> Component.translatable("commands.origin.get.result", target.getName(), layer.getName(), origin.getName(), origin.getId().toString()), false);

			return 1;

		}

	}

	public static final class GuiNode {

		public static CommandNode<CommandSourceStack> get() {
			return literal("gui")
				.executes(context -> openAll(context, true))
				.then(argument("targets", EntityArgument.players())
					.executes(context -> openAll(context, false))
					.then(argument("layer", OriginLayerArgumentType.layer())
						.executes(GuiNode::openSpecific))).build();
		}

		public static int openAll(CommandContext<CommandSourceStack> context, boolean self) throws CommandSyntaxException {

			CommandSourceStack source = context.getSource();
			Collection<ServerPlayer> targets = self
				? List.of(source.getPlayerOrException())
				: EntityArgument.getPlayers(context, "targets");

			for (ServerPlayer target : targets) {

				OriginComponent originComponent = ModComponents.ORIGIN.get(target);
				OriginLayerManager.values()
					.stream()
					.filter(OriginLayer::isEnabled)
					.forEach(layer -> originComponent.setOrigin(layer, Origin.EMPTY));

				boolean automaticallyAssigned = originComponent.checkAutoChoosingLayers(target, false);
				int optionCount = OriginLayerManager.getOriginOptionCount(target);

				originComponent.selectingOrigin(!automaticallyAssigned && optionCount > 0);
				originComponent.sync();

				if (originComponent.isSelectingOrigin()) {
					ServerPlayNetworking.send(target, new OpenChooseOriginScreenS2CPacket(false));
				}

			}

			source.sendSuccess(() -> Component.translatable("commands.origin.gui.all", targets.size()), true);
			return targets.size();

		}

		public static int openSpecific(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {

			Collection<ServerPlayer> targets = EntityArgument.getPlayers(context, "targets");
			OriginLayer layer = OriginLayerArgumentType.getLayer(context, "layer");

			for (ServerPlayer target : targets) {

				OriginComponent originComponent = ModComponents.ORIGIN.get(target);
				originComponent.setOrigin(layer, Origin.EMPTY);

				boolean automaticallyAssigned = originComponent.checkAutoChoosingLayers(target, false);
				int optionCount = layer.getOriginOptionCount(target);

				originComponent.selectingOrigin(!automaticallyAssigned && optionCount > 0);
				originComponent.sync();

				if (originComponent.isSelectingOrigin()) {
					ServerPlayNetworking.send(target, new OpenChooseOriginScreenS2CPacket(false));
				}

			}

			context.getSource().sendSuccess(() -> Component.translatable("commands.origin.gui.layer", targets.size(), layer.getName()), true);
			return targets.size();

		}

	}

	public static final class RandomNode {

		public static CommandNode<CommandSourceStack> get() {
			return literal("random")
				.executes(context -> randomizeAll(context, true))
				.then(argument("targets", EntityArgument.players())
					.executes(context -> randomizeAll(context, false))
					.then(argument("layer", OriginLayerArgumentType.layer())
						.executes(RandomNode::randomizeSpecific))).build();
		}

		public static int randomizeAll(CommandContext<CommandSourceStack> context, boolean self) throws CommandSyntaxException {

			CommandSourceStack source = context.getSource();
			Collection<ServerPlayer> targets = self
				? List.of(source.getPlayerOrThrow())
				: EntityArgument.getPlayers(context, "targets");

			List<OriginLayer> layers = OriginLayerManager.values()
				.stream()
				.filter(OriginLayer::isRandomAllowed)
				.toList();

			for (ServerPlayer target : targets) {

				for (OriginLayer layer : layers) {
					setAndGetRandomOrigin(target, layer);
				}

			}

			source.sendSuccess(() -> Component.translatable("commands.origin.random.all", targets.size(), layers.size()), true);
			return targets.size();

		}

		public static int randomizeSpecific(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {

			List<ServerPlayer> targets = new ObjectArrayList<>(EntityArgument.getPlayers(context, "targets"));
			OriginLayer layer = OriginLayerArgumentType.getLayer(context, "layer");

			CommandSourceStack source = context.getSource();
			AtomicReference<Origin> origin = new AtomicReference<>();

			if (layer.isRandomAllowed()) {

				for (ServerPlayer target : targets) {
					origin.set(setAndGetRandomOrigin(target, layer));
				}

				if (targets.size() > 1) {
					source.sendSuccess(() -> Component.translatable("commands.origin.random.success.multiple", targets.size(), layer.getName()), true);
				}

				else {
					source.sendSuccess(() -> Component.translatable("commands.origin.random.success.single", targets.getFirst().getName(), origin.get().getName(), layer.getName()), true);
				}

			}

			return targets.size();

		}

	}

	private static Origin setAndGetRandomOrigin(ServerPlayer target, OriginLayer layer) {

		List<Origin> origins = layer.getRandomOrigins(target)
			.stream()
			.filter(OriginManager::contains)
			.map(OriginManager::get)
			.toList();

		OriginComponent originComponent = ModComponents.ORIGIN.get(target);
		Origin origin = origins.get(Random.create().nextInt(origins.size()));

		boolean hadOriginBefore = originComponent.hadOriginBefore();
		boolean hadAllOrigins = originComponent.hasAllOrigins();

		originComponent.setOrigin(layer, origin);
		originComponent.checkAutoChoosingLayers(target, false);

		originComponent.sync();

		if (originComponent.hasAllOrigins() && !hadAllOrigins) {
			OriginComponent.onChosen(target, hadOriginBefore);
		}

		Origins.LOGGER.info("Player {} was randomly assigned the origin {} for layer {}", target.getName().getString(), origin.getId(), layer.getId());
		return origin;

	}

}
