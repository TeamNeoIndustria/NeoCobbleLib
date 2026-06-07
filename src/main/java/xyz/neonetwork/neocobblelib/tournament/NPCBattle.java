package xyz.neonetwork.neocobblelib.tournament;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.storage.party.PartyPosition;
import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore;
import com.cobblemon.mod.common.battles.*;
import com.cobblemon.mod.common.battles.actor.PlayerBattleActor;
import com.cobblemon.mod.common.battles.actor.TrainerBattleActor;
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import com.cobblemon.mod.common.entity.npc.NPCBattleActor;
import com.cobblemon.mod.common.entity.npc.NPCEntity;
import com.cobblemon.mod.common.pokemon.Pokemon;
import kotlin.Unit;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import xyz.neonetwork.neocobblelib.NeoConstants;
import xyz.neonetwork.neocobblelib.utils.NPCUtils;
import xyz.neonetwork.neocobblelib.utils.TaskScheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    public BattleStartResult getResult() {
        return result;
    }

    public void startBattle() {
        List<BattlePokemon> playerParty = new ArrayList<>();

        PlayerPartyStore playerPartyStore = Cobblemon.INSTANCE.getStorage().getParty(this.player);

        playerPartyStore.forEach(pokemon -> {
            playerParty.add(BattlePokemon.Companion.playerOwned(pokemon));
        });

        PlayerBattleActor playerActor = new PlayerBattleActor(this.player.getUUID(), playerParty);
        NPCBattleActor npcBattleActor = new NPCBattleActor(this.entity, this.npcParty.getParty(), 5);

        TaskScheduler.nextTickTask(() -> {
            playerPartyStore.toGappyList().stream()
                    .filter(Objects::nonNull)
                    .forEach(Pokemon::recall);

            BattleSide trainerSide = new BattleSide(npcBattleActor);
            BattleSide playerSide = new BattleSide(playerActor);

            try {
                playerParty.getFirst().getEffectedPokemon().sendOut(this.player.serverLevel(), this.player.getPosition(0), null, pokemonEntity -> Unit.INSTANCE);
                npcBattleActor.getPokemonList().getFirst().getEffectedPokemon().sendOut(this.player.serverLevel(), this.player.getPosition(0), null, pokemonEntity -> Unit.INSTANCE);
            } catch (Exception exception) {
                NeoConstants.LOGGER.error("Error sending out battle pokemon.");
                exception.printStackTrace();
                return;
            }

            result = BattleRegistry.startBattle(BattleFormat.Companion.getGEN_9_SINGLES(), playerSide, trainerSide, false);

            result.ifSuccessful((battle) -> {
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
