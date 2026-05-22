package xyz.neonetwork.neocobblelib.tournament;

import com.cobblemon.mod.common.battles.BattleBuilder;
import com.cobblemon.mod.common.battles.BattleStartResult;
import com.cobblemon.mod.common.entity.npc.NPCEntity;
import net.minecraft.server.level.ServerPlayer;
import xyz.neonetwork.neocobblelib.NeoConstants;

public class NPCBattle {

    private final ServerPlayer player;
    private final NPCEntity entity;
    private final NPCParty npcParty;
    private BattleStartResult result;

    public NPCBattle(ServerPlayer player, NPCEntity entity, NPCParty party) {
        this.player = player;
        this.entity = entity;
        this.npcParty = party;
    }

    public ServerPlayer getPlayer() {
        return player;
    }

    public NPCParty getNpcParty() {
        return npcParty;
    }

    public void startBattle() {
        result = BattleBuilder.INSTANCE.pvn(this.player, this.entity);
        result.ifSuccessful((battle) ->{
            NeoConstants.LOGGER.info("Battle Success");
            return null;
        });
        result.ifErrored(erroredBattleStart -> {
           NeoConstants.LOGGER.error("Battle Failed");
           erroredBattleStart.getErrors().forEach(battleStartError -> {
               NeoConstants.LOGGER.error(battleStartError.toString());
           });
           return null;
        });
    }

    public void finishBattle() {
        result.ifSuccessful((battle) -> {
            battle.getWinners().forEach(actor -> {
                if (!actor.isForPlayer(this.player)) {
                    NeoConstants.LOGGER.info("Player Loses");
                    return;
                }

                NeoConstants.LOGGER.info("Player Wins");
                // Player Wins
            });
            return null;
        });
    }
}
