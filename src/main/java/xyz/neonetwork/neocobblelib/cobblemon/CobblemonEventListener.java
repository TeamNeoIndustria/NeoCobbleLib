package xyz.neonetwork.neocobblelib.cobblemon;

import com.cobblemon.mod.common.api.battles.model.PokemonBattle;
import com.cobblemon.mod.common.api.battles.model.actor.BattleActor;
import com.cobblemon.mod.common.api.events.battles.BattleFledEvent;
import com.cobblemon.mod.common.api.events.battles.BattleVictoryEvent;
import com.cobblemon.mod.common.entity.npc.NPCBattleActor;
import com.cobblemon.mod.common.entity.npc.NPCEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.common.NeoForge;
import xyz.neonetwork.neocobblelib.NeoConstants;
import xyz.neonetwork.neocobblelib.battles.NeoBattleManager;
import xyz.neonetwork.neocobblelib.battles.events.TrainerBattleEndEvent;

public class CobblemonEventListener {
    public static void onBattleVictory(BattleVictoryEvent event) {
        NPCEntity npc = findNpc(event.getBattle());

        event.getBattle().getPlayers().forEach((player -> {
            boolean won = event.getWinners().stream().anyMatch(actor -> actor.getUuid().equals(player.getUUID()));

            if (npc != null) {
                NeoForge.EVENT_BUS.post(new TrainerBattleEndEvent(player, npc,
                        won ? TrainerBattleEndEvent.Outcome.PLAYER_WON : TrainerBattleEndEvent.Outcome.PLAYER_LOST));
            }


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
        NPCEntity npc = findNpc(event.getBattle());

        event.getBattle().getPlayers().forEach((player -> {
            if (npc != null) {
                NeoForge.EVENT_BUS.post(new TrainerBattleEndEvent(player, npc, TrainerBattleEndEvent.Outcome.PLAYER_FLED));
            }

            if (!NeoBattleManager.isInBattle(player)) {
                return;
            }

            NeoConstants.LOGGER.info("{}'s Flee", player.getDisplayName().getString());
            NeoBattleManager.finialiseBattle(player);
        }));
    }

    private static NPCEntity findNpc(PokemonBattle battle) {
        for (BattleActor actor : battle.getActors()) {
            if (actor instanceof NPCBattleActor npcActor) {
                return npcActor.getNpc();
            }
        }

        return null;
    }
}
