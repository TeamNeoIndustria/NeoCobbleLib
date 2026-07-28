package xyz.neonetwork.neocobblelib.cobblemon;

import com.cobblemon.mod.common.api.events.battles.BattleFledEvent;
import com.cobblemon.mod.common.api.events.battles.BattleVictoryEvent;
import xyz.neonetwork.neocobblelib.NeoConstants;
import xyz.neonetwork.neocobblelib.battles.NeoBattleManager;

public class CobblemonEventListener {
    public static void onBattleVictory(BattleVictoryEvent event) {
        NeoConstants.LOGGER.info("Victory Called");

        event.getBattle().getPlayers().forEach((player -> {
            if (!event.getWinners().getFirst().getUuid().equals(player.getUUID())) {
                return;
            }

            NeoConstants.LOGGER.info("{}'s Battle", player.getDisplayName().getString());
            NeoBattleManager.finialiseBattle(player);
        }));
    }

    public static void onBattleFled(BattleFledEvent event) {
        NeoConstants.LOGGER.info("Fled Called");

        event.getBattle().getPlayers().forEach((player -> {
            NeoConstants.LOGGER.info("{}'s Flee", player.getDisplayName().getString());
            NeoBattleManager.finialiseBattle(player);
        }));
    }
}
