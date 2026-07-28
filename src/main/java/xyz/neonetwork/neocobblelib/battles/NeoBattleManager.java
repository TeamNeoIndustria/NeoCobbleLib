package xyz.neonetwork.neocobblelib.battles;

import net.minecraft.server.level.ServerPlayer;
import xyz.neonetwork.neocobblelib.NeoConstants;

import java.util.HashMap;
import java.util.Map;

public class NeoBattleManager {

    private static final HashMap<String, NeoBattle> neoBattles = new HashMap<>();

    public static NeoBattle getBattle(ServerPlayer player) {
        NeoBattle battle = neoBattles.get(player.getStringUUID());

        if (battle == null) {
            for (NeoBattle battles : neoBattles.values()) {
                if (battles.getStartingPlayer().getStringUUID().equals(player.getStringUUID())) {
                    battle = battles;
                }

                if (battles.getOpponentPlayer().getStringUUID().equals(player.getStringUUID())) {
                    battle = battles;
                }
            }
        }

        return battle;
    }

    public static void addBattle(ServerPlayer initiator, NeoBattle battle) {
        if (neoBattles.containsKey(initiator.getStringUUID())) {
            NeoConstants.LOGGER.error("{} is already in a battle. Report incident immediately.", initiator.getDisplayName().getString());

            return;
        }

        neoBattles.put(initiator.getStringUUID(), battle);
    }

    public static void removeBattle(ServerPlayer initiator) {
        if (!neoBattles.containsKey(initiator.getStringUUID())) {
            NeoConstants.LOGGER.error("{} is not in a battle.", initiator.getDisplayName().getString());

            return;
        }

        neoBattles.remove(initiator.getStringUUID());
    }

    public static void removeBattle(NeoBattle battle) {
        String uuid = null;

        for (Map.Entry<String, NeoBattle> entry : neoBattles.entrySet()) {
            if (entry.getValue() == battle) {
                uuid = entry.getKey();
            }
        }

        if (uuid == null) {
            return;
        }

        neoBattles.remove(uuid);
    }

    public static boolean isInBattle(ServerPlayer player) {
        for (NeoBattle battles : neoBattles.values()) {
            if (battles.getStartingPlayer().getStringUUID().equals(player.getStringUUID())) {
                return true;
            }

            if (battles.getOpponentPlayer().getStringUUID().equals(player.getStringUUID())) {
                return true;
            }
        }

        return false;
    }

    public static void finialiseBattle(ServerPlayer player) {
        NeoConstants.LOGGER.info("Called finaliseBattle");
        NeoBattle battle = getBattle(player);

        if (battle == null) {
            NeoConstants.LOGGER.info("No battle found");
            return;
        }

        battle.finishBattle();

        removeBattle(battle);
    }

}
