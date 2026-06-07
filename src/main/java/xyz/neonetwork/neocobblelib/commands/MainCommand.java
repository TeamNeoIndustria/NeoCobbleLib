package xyz.neonetwork.neocobblelib.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import xyz.neonetwork.neocobblelib.NeoConstants;
import xyz.neonetwork.neocobblelib.cobblemon.CobblemonPresetTrainers;
import xyz.neonetwork.neocobblelib.cobblemon.CobblemonUtils;
import xyz.neonetwork.neocobblelib.tournament.*;

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
                                Commands.literal("tournament")
                                        .requires(source -> source.hasPermission(2))
                                        .then(Commands.argument("option", StringArgumentType.string())
                                        .executes(MainCommand::tournamentOption))

                        )
                        .then(
                                Commands.literal("trainer")
                                        .requires(source -> source.hasPermission(2))
                                        .then(Commands.argument("option", StringArgumentType.string())
                                                .executes(MainCommand::trainerOption))

                        )
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

    public static int tournamentOption(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Object[] extract = extractContext(context);

        if (extract == null) {
            return 0;
        }

        NeoConstants.LOGGER.info("Tournament Command");

        ServerPlayer sender = (ServerPlayer) extract[1];
        String option = StringArgumentType.getString(context, "option").toLowerCase();
        Tournament tournament;

        switch (option) {
            case "create":
                tournament = TournamentManager.createTournament(sender, TournamentType.RANDOMISED);
                tournament.spawnNPC();

                sender.sendSystemMessage(Component.literal("Created Tournament"));
                break;
            case "start":
                tournament = TournamentManager.getTournament(sender);

                if (tournament == null) {
                    sender.sendSystemMessage(Component.literal("You're not in a tournament."));
                    break;
                }

                tournament.startBattle();
                sender.sendSystemMessage(Component.literal("Starting Tournament"));
                break;
            case "stop":
                TournamentManager.invalidateTournament(sender);
                sender.sendSystemMessage(Component.literal("Invalidated Tournament"));
                break;
        }

        return 1;
    }

    public static int trainerOption(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Object[] extract = extractContext(context);

        if (extract == null) {
            return 0;
        }

        ServerPlayer sender = (ServerPlayer) extract[1];
        String option = StringArgumentType.getString(context, "option").toLowerCase();
        CobblemonPresetTrainers trainer = CobblemonPresetTrainers.fromString(option);

        if (trainer == null) {
            sender.sendSystemMessage(Component.literal("Invalid Trainer Name").withStyle(ChatFormatting.RED));
            return 0;
        }

        NPCBattle battle = BattleManager.createBattle(sender, trainer.getParty());

        battle.startBattle();

        return 1;
    }
}
