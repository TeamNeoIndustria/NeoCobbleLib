package xyz.neonetwork.neocobblelib.cobblemon;

import com.cobblemon.mod.common.api.events.battles.BattleFledEvent;
import com.cobblemon.mod.common.api.events.battles.BattleVictoryEvent;
import xyz.neonetwork.neocobblelib.NeoConstants;
import xyz.neonetwork.neocobblelib.tournament.BattleManager;

public class CobblemonEventListener {
    public static void onBattleVictory(BattleVictoryEvent event) {
        NeoConstants.LOGGER.info("Victory Called");

        event.getBattle().getPlayers().forEach((player -> {
            if (BattleManager.inBattle(player)) {
                NeoConstants.LOGGER.info("{}'s victory", player.getDisplayName().getString());
                BattleManager.removeBattle(BattleManager.getBattle(player));
            }
        }));
    }

    public static void onBattleFled(BattleFledEvent event) {
        NeoConstants.LOGGER.info("Fled Called");

        event.getBattle().getPlayers().forEach((player -> {
            NeoConstants.LOGGER.info("{}'s flee", player.getDisplayName().getString());
            if (BattleManager.inBattle(player)) {
                BattleManager.removeBattle(BattleManager.getBattle(player));
            }
        }));
    }
}
