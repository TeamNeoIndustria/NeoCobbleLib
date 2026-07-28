package xyz.neonetwork.neocobblelib.cobblemon;

import com.cobblemon.mod.common.api.events.battles.BattleFledEvent;
import com.cobblemon.mod.common.api.events.battles.BattleVictoryEvent;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import xyz.neonetwork.neocobblelib.NeoConstants;
import xyz.neonetwork.neocobblelib.battles.NeoBattleManager;

public class CobblemonEventListener {
    public static void onBattleVictory(BattleVictoryEvent event) {
        event.getBattle().getPlayers().forEach((player -> {
            if (!NeoBattleManager.isInBattle(player)) {
                return;
            }

            NeoConstants.LOGGER.info("{}'s Battle", player.getDisplayName().getString());

            if (event.getWinners().getFirst().getUuid().equals(player.getUUID())) {
                player.sendSystemMessage(Component.literal("Congratulations you won!").withStyle(ChatFormatting.GREEN));
            } else {
                player.sendSystemMessage(Component.literal("Uh oh! You lost.").withStyle(ChatFormatting.RED));
            }

            NeoBattleManager.finialiseBattle(player);
        }));
    }

    public static void onBattleFled(BattleFledEvent event) {
        event.getBattle().getPlayers().forEach((player -> {
            if (!NeoBattleManager.isInBattle(player)) {
                return;
            }

            NeoConstants.LOGGER.info("{}'s Flee", player.getDisplayName().getString());
            NeoBattleManager.finialiseBattle(player);
        }));
    }
}
