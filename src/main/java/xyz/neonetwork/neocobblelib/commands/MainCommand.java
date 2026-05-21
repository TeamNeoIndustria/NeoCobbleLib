package xyz.neonetwork.neocobblelib.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import xyz.neonetwork.neocobblelib.NeoConstants;
import xyz.neonetwork.neocobblelib.cobblemon.CobblemonUtils;
import xyz.neonetwork.neocobblelib.objects.Tournament;
import xyz.neonetwork.neocobblelib.objects.TournamentManager;
import xyz.neonetwork.neocobblelib.objects.TournamentType;

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
                                        .executes(MainCommand::tournamentOption)

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

        ServerPlayer sender = (ServerPlayer) extract[1];
        Tournament tournament = TournamentManager.createTournament(sender, TournamentType.RANDOMISED);

        tournament.spawnNPC();


        return 1;
    }
}
