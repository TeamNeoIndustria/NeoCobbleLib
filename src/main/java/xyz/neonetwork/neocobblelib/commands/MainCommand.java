package xyz.neonetwork.neocobblelib.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import xyz.neonetwork.neocobblelib.NeoConstants;
import xyz.neonetwork.neocobblelib.cobblemon.CobblemonPresetTrainers;
import xyz.neonetwork.neocobblelib.cobblemon.CobblemonTrainerManager;
import xyz.neonetwork.neocobblelib.cobblemon.CobblemonUtils;
import xyz.neonetwork.neocobblelib.battles.*;
import xyz.neonetwork.neocobblelib.utils.BattleUtils;

import java.util.Map;

public class MainCommand {
    public static String commandName = "neocobblelib";

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal(commandName)
                        .then(
                                Commands.literal("randomparty")
                                        .requires(source -> source.hasPermission(2))
                                                .executes(MainCommand::randomPartyOption)

                        )
                        .then(
                                Commands.literal("trainer")
                                        .requires(source -> source.hasPermission(2))
                                                .then(Commands.argument("option", StringArgumentType.string())
                                                .then(Commands.argument("player", EntityArgument.player())
                                                        .executes(MainCommand::trainerOption)))

                        )
                        .then(
                                Commands.literal("trainerinfo")
                                        .requires(source -> source.hasPermission(2))
                                        .then(Commands.argument("option", StringArgumentType.string())
                                                .executes(MainCommand::trainerInfoOption))
                        )
                        .then(
                                Commands.literal("randomtrainer")
                                        .requires(source -> source.hasPermission(2))
                                                .executes(MainCommand::randomTrainerOption))
        );
    }

    public static Object[] extractContext(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Object[] extraction = new Object[]{
                context.getSource(),
                context.getSource().getPlayer()
        };

        if (extraction[1] == null) {
            NeoConstants.LOGGER.error("MainCommand sender is null");

            return null;
        }

        return extraction;
    }

    public static int randomPartyOption(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Object[] extract = extractContext(context);

        if (extract == null) {
            return 0;
        }

        ServerPlayer sender = (ServerPlayer) extract[1];

        CobblemonUtils.setPlayerPartyRandom(sender);

        return 1;
    }

    public static int trainerOption(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Object[] extract = extractContext(context);

        if (extract == null) {
            return 0;
        }

        ServerPlayer sender = (ServerPlayer) extract[1];
        String option = StringArgumentType.getString(context, "option");
        ServerPlayer player = EntityArgument.getPlayer(context, "player");
        CobblemonPresetTrainers trainer = CobblemonTrainerManager.getTrainer(option);

        if (trainer == null) {
            sender.sendSystemMessage(Component.literal("Invalid Trainer Name").withStyle(ChatFormatting.RED));

            StringBuffer buf = new StringBuffer();

            for (Map.Entry<String, CobblemonPresetTrainers> entry : CobblemonTrainerManager.getAllTrainers().entrySet()) {
                buf.append(entry.getKey()).append(", ");
            }

            sender.sendSystemMessage(Component.literal("Loaded Trainers: " + buf));
            return 0;
        }

        NeoBattle battle = BattleUtils.createBattleWithTrainer(player, trainer);

        battle.startBattle();

        return 1;
    }

    public static int trainerInfoOption(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Object[] extract = extractContext(context);

        if (extract == null) {
            return 0;
        }

        ServerPlayer sender = (ServerPlayer) extract[1];
        CobblemonPresetTrainers trainer = CobblemonTrainerManager.getTrainer(StringArgumentType.getString(context, "option"));

        sender.sendSystemMessage(Component.literal("Trainer Info:"));
        sender.sendSystemMessage(Component.literal("Name: " + trainer.getName()));
        sender.sendSystemMessage(Component.literal("Colour: " + trainer.getNameColour().getName()));
        sender.sendSystemMessage(Component.literal("Pokemon:"));
        trainer.getParty().getParty().forEach(pokemon -> {
            sender.sendSystemMessage(Component.literal("-> Name: " + pokemon.getDisplayName(false).getString()));
            sender.sendSystemMessage(Component.literal("-> Species: " + pokemon.getSpecies().getName()));
            sender.sendSystemMessage(Component.literal("-> Level: " + pokemon.getLevel()));
            sender.sendSystemMessage(Component.literal("------------"));
        });

        return 1;
    }

    public static int randomTrainerOption(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Object[] extract = extractContext(context);

        if (extract == null) {
            return 0;
        }

        ServerPlayer sender = (ServerPlayer) extract[1];
        NeoBattle battle = BattleUtils.createBattleWithRandom(sender);

        battle.startBattle();

        return 1;
    }
}
