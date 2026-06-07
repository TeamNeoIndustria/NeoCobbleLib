package xyz.neonetwork.neocobblelib.tournament;

import com.cobblemon.mod.common.api.battles.model.ai.BattleAI;
import com.cobblemon.mod.common.battles.BattleFormat;
import com.cobblemon.mod.common.battles.BattleRegistry;
import com.cobblemon.mod.common.battles.ai.RandomBattleAI;
import com.cobblemon.mod.common.battles.ai.StrongBattleAI;
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import com.cobblemon.mod.common.entity.npc.NPCEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import xyz.neonetwork.neocobblelib.cobblemon.CobblemonPresetTrainers;
import xyz.neonetwork.neocobblelib.utils.NPCUtils;

import java.util.ArrayList;
import java.util.List;

public class BattleManager {
    public static List<NPCBattle> battles = new ArrayList<>();

    public static NPCBattle createBattle(ServerPlayer player, NPCParty party) {
        Entity entity = NPCUtils.createHumanoidNPC(player.serverLevel(), "Trainer", ChatFormatting.WHITE, player.blockPosition());
        NPCEntity npcEntity = new NPCEntity(player.serverLevel());

        npcEntity.setParty(party.getAsNPCPartyStore(npcEntity));

        NPCBattle battle = new NPCBattle(player, npcEntity, party);

        addBattle(battle);

        return battle;
    }

    public static NPCBattle createBattleWithTrainer(ServerPlayer player, CobblemonPresetTrainers trainer) {
        Entity entity = NPCUtils.createHumanoidNPC(player.serverLevel(), trainer.getName(), trainer.getNameColour(), player.blockPosition());
        NPCEntity npcEntity = new NPCEntity(player.serverLevel());
        npcEntity.setParty(trainer.getParty().getAsNPCPartyStore(npcEntity));

        NPCBattle battle = new NPCBattle(player, npcEntity, trainer.getParty());

        addBattle(battle);

        return battle;
    }

    public static NPCBattle getBattle(ServerPlayer player) {
        if (!inBattle(player)) {
            return null;
        }

        for (NPCBattle battle : battles) {
            if (battle.getPlayer() == player) {
                return battle;
            }
        }

        return null;
    }

    public static void addBattle(NPCBattle battle) {
        battles.add(battle);
    }

    public static void removeBattle(NPCBattle battle) {
        battles.remove(battle);
    }

    public static boolean inBattle(ServerPlayer player) {
        for (NPCBattle battle : battles) {
            if (battle.getPlayer() == player) {
                return true;
            }
        }

        return BattleRegistry.getBattleByParticipatingPlayer(player) != null;
    }

}
