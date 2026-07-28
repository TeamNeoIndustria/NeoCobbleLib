package xyz.neonetwork.neocobblelib.utils;

import com.cobblemon.mod.common.entity.npc.NPCEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import xyz.neonetwork.neocobblelib.NeoConstants;
import xyz.neonetwork.neocobblelib.battles.NeoBattleType;
import xyz.neonetwork.neocobblelib.cobblemon.CobblemonPresetTrainers;
import xyz.neonetwork.neocobblelib.battles.NeoBattle;
import xyz.neonetwork.neocobblelib.battles.NeoNPCParty;
import xyz.neonetwork.neocobblelib.cobblemon.CobblemonUtils;

import java.util.UUID;

public class BattleUtils {
    public static NeoBattle createNPCBattle(ServerPlayer player, NeoNPCParty party) {
        NeoBattle neoBattle = new NeoBattle(player, NeoBattleType.NPC);

        neoBattle.setNpcParty(party);

        return neoBattle;
    }

    public static NeoBattle createBattleWithTrainer(ServerPlayer player, CobblemonPresetTrainers trainer) {
        NeoBattle neoBattle = new NeoBattle(player, NeoBattleType.NPC);
        NPCEntity entity = NPCUtils.createTrainer(player.serverLevel(), trainer, player.blockPosition());

        neoBattle.setEntity(entity);
        neoBattle.setNpcParty(trainer.getParty());

        return neoBattle;
    }

    public static NeoBattle createBattleWithRandom(ServerPlayer player) {
        NeoBattle neoBattle = new NeoBattle(player, NeoBattleType.NPC_RANDOM);
        NeoNPCParty npcParty = CobblemonUtils.randomiseNPCParty(new NeoNPCParty(), 3, 50, 50);
        NPCEntity entity = NPCUtils.createTrainerDelay(player.serverLevel(), player.blockPosition());

        entity.setParty(npcParty.getAsNPCPartyStore(entity));

        NPCUtils.sendNPCToClient(entity);

        neoBattle.setEntity(entity);
        neoBattle.setNpcParty(npcParty);

        return neoBattle;

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
