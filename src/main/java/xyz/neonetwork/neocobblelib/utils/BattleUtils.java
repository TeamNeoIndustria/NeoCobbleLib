package xyz.neonetwork.neocobblelib.utils;

import com.cobblemon.mod.common.entity.npc.NPCEntity;
import net.minecraft.server.level.ServerPlayer;
import xyz.neonetwork.neocobblelib.cobblemon.CobblemonPresetTrainers;
import xyz.neonetwork.neocobblelib.tournament.NPCBattle;
import xyz.neonetwork.neocobblelib.tournament.NPCParty;

public class BattleUtils {
    public static NPCBattle createBattle(ServerPlayer player, NPCParty party) {
        NPCEntity npcEntity = NPCUtils.createNPC(player.serverLevel(), player.blockPosition());
        npcEntity.setParty(party.getAsNPCPartyStore(npcEntity));

        return new NPCBattle(player, npcEntity, party);
    }

    public static NPCBattle createBattleWithTrainer(ServerPlayer player, CobblemonPresetTrainers trainer) {
        return new NPCBattle(player, NPCUtils.createTrainer(player.serverLevel(), trainer, player.blockPosition()), trainer.getParty());
    }

}
