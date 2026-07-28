package xyz.neonetwork.neocobblelib.utils;

import com.cobblemon.mod.common.entity.npc.NPCEntity;
import net.minecraft.server.level.ServerPlayer;
import xyz.neonetwork.neocobblelib.NeoConstants;
import xyz.neonetwork.neocobblelib.cobblemon.CobblemonPresetTrainers;
import xyz.neonetwork.neocobblelib.battles.NeoBattle;
import xyz.neonetwork.neocobblelib.battles.NeoNPCParty;

import java.util.UUID;

public class BattleUtils {
    public static NeoBattle createBattle(ServerPlayer player, NeoNPCParty party) {
        NPCEntity npcEntity = NPCUtils.createNPC(player.serverLevel(), player.blockPosition());
        npcEntity.setParty(party.getAsNPCPartyStore(npcEntity));

        return new NeoBattle(player, npcEntity, party);
    }

    public static NeoBattle createBattleWithTrainer(ServerPlayer player, CobblemonPresetTrainers trainer) {
        return new NeoBattle(player, NPCUtils.createTrainer(player.serverLevel(), trainer, player.blockPosition()), trainer.getParty());
    }

    public static boolean isUUIDPlayer(UUID uuid) {
        for (ServerPlayer player : NeoConstants.SERVER.getPlayerList().getPlayers()) {
            if (uuid.equals(player.getUUID())) {
                return true;
            }
        }

        return false;
    }

}
