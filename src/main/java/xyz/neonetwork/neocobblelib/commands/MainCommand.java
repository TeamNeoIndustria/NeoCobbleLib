package xyz.neonetwork.neocobblelib.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import xyz.neonetwork.neocobblelib.NeoConstants;
import xyz.neonetwork.neocobblelib.cobblemon.CobblemonUtils;

public class MainCommand {
    public static String commandName = "neocobblelib";

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal(commandName)
                        .then(
                                Commands.literal("test")
                                        .requires(source -> source.hasPermission(2))
                                        .executes(MainCommand::testOption)

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

    public static int testOption(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Object[] extract = extractContext(context);

        if (extract == null) {
            return 0;
        }

        ServerPlayer sender = (ServerPlayer) extract[1];

        CobblemonUtils.setPlayerPartyRandom(sender);

        return 1;
    }
}
