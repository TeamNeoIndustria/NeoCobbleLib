package xyz.neonetwork.neocobblelib.battles;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore;
import com.cobblemon.mod.common.battles.*;
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import com.cobblemon.mod.common.entity.npc.NPCBattleActor;
import com.cobblemon.mod.common.entity.npc.NPCEntity;
import com.cobblemon.mod.common.pokemon.Pokemon;
import kotlin.Unit;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import xyz.neonetwork.neocobblelib.NeoConstants;
import xyz.neonetwork.neocobblelib.cobblemon.CobblemonUtils;
import xyz.neonetwork.neocobblelib.utils.BattleUtils;
import xyz.neonetwork.neocobblelib.utils.NPCUtils;
import xyz.neonetwork.neocobblelib.utils.TaskScheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class NeoBattle {
    // Entities
    private final ServerPlayer playerOne;
    private ServerPlayer playerTwo;
    private NPCEntity entity;

    // Type
    private final NeoBattleType type;

    // Teams
    private NeoNPCParty npcParty;

    public NeoBattle(ServerPlayer player, NeoBattleType type) {
        this.playerOne = player;
        this.type = type;

        NeoBattleManager.addBattle(player, this);
    }

    public ServerPlayer getStartingPlayer() {
        return playerOne;
    }

    public ServerPlayer getOpponentPlayer() {
        return playerTwo;
    }

    public NeoNPCParty getNpcParty() {
        return npcParty;
    }

    public NPCEntity getEntity() {
        return entity;
    }

    public NeoBattleType getType() {
        return type;
    }

    public void setEntity(NPCEntity entity) {
        this.entity = entity;
    }

    public void setNpcParty(NeoNPCParty npcParty) {
        this.npcParty = npcParty;
    }

    public void setPlayerTwo(ServerPlayer player) {
        playerTwo = player;
    }

    public boolean sendOutPlayerPokemon(ServerPlayer player) {
        List<BattlePokemon> playerParty = new ArrayList<>();
        PlayerPartyStore playerPartyStore = Cobblemon.INSTANCE.getStorage().getParty(player);

        playerPartyStore.forEach(pokemon -> playerParty.add(BattlePokemon.Companion.playerOwned(pokemon)));

        try {
            TaskScheduler.nextTickTask(() -> {
                playerPartyStore.toGappyList().stream()
                        .filter(Objects::nonNull)
                        .forEach(Pokemon::recall);

                try {
                    playerParty.getFirst().getEffectedPokemon().sendOut(player.serverLevel(), player.getPosition(0), null, pokemonEntity -> Unit.INSTANCE);
                } catch (Exception exception) {
                    NeoConstants.LOGGER.error("Error sending out battle Pokemon. {}", exception.getMessage());
                }
            });
        } catch (Exception exception) {
            NeoConstants.LOGGER.error("Error in TaskSchedule#nextTickTask for NeoBattle#sendOutPlayerPokemon. [{}]", player.getDisplayName().getString());
            exception.printStackTrace();

            return false;
        }

        return true;
    }

    public boolean loadBattle() {
        // Opponent
        switch (this.type) {
            case PLAYER_V_PLAYER -> {
                // Player 1
                if (!sendOutPlayerPokemon(this.playerOne)) {
                    NeoConstants.LOGGER.error("Error sending out Player One's [{}] Pokemon in PvP.", this.playerOne.getDisplayName().getString());

                    return false;
                }

                // Player 2
                if (!sendOutPlayerPokemon(this.playerTwo)) {
                    NeoConstants.LOGGER.error("Error sending out Player Two's [{}] Pokemon in PvP.", this.playerOne.getDisplayName().getString());

                    return false;
                }
            }
            case NPC -> {
                // Player 1
                if (!sendOutPlayerPokemon(this.playerOne)) {
                    NeoConstants.LOGGER.error("Error sending out Player One's [{}] Pokemon in PvT.", this.playerOne.getDisplayName().getString());

                    return false;
                }

                // NPC
                try {
                    if (this.entity == null) {
                        this.entity = NPCUtils.createNPC(this.playerOne.serverLevel(), this.playerOne.blockPosition());
                    }

                    NPCBattleActor npcBattleActor = new NPCBattleActor(this.entity, this.npcParty.getParty(), 5);

                    npcBattleActor.getPokemonList().getFirst().getEffectedPokemon().sendOut(this.playerOne.serverLevel(), this.entity.position(), null, pokemonEntity -> Unit.INSTANCE);
                } catch (Exception exception) {
                    NeoConstants.LOGGER.error("Error sending out Trainer's Pokemon in PvT.");

                    exception.printStackTrace();
                }
            }
            case NPC_RANDOM -> {
                // Player 1
                if (!sendOutPlayerPokemon(this.playerOne)) {
                    NeoConstants.LOGGER.error("Error sending out Player One's [{}] Pokemon in PvR.", this.playerOne.getDisplayName().getString());

                    return false;
                }

                try {
                    if (this.entity == null) {
                        this.entity = NPCUtils.createNPC(this.playerOne.serverLevel(), this.playerOne.blockPosition());
                        this.entity.setCustomName(Component.literal("Randomizer").withStyle(ChatFormatting.GOLD));
                    }

                    NPCBattleActor npcBattleActor = new NPCBattleActor(this.entity, this.npcParty.getParty(), 5);

                    npcBattleActor.getPokemonList().getFirst().getEffectedPokemon().sendOut(this.playerOne.serverLevel(), this.entity.position(), null, pokemonEntity -> Unit.INSTANCE);
                } catch (Exception exception) {
                    NeoConstants.LOGGER.error("Error sending out Trainer's Pokemon in PvR.");

                    exception.printStackTrace();
                }
            }
        }

        return true;
    }

    public void startBattle() {
        if (!loadBattle()) {
            NeoConstants.LOGGER.error("Error loading battle. [{}]", this.playerOne.getDisplayName().getString());

            return;
        }

        TaskScheduler.nextTickTask(() -> {
            BattleStartResult result = BattleBuilder.INSTANCE.pvn(this.playerOne, this.entity);

            result.ifErrored(erroredBattleStart -> {
                NeoConstants.LOGGER.error("Battle Failed");

                erroredBattleStart.getErrors().forEach(battleStartError -> {
                    NeoConstants.LOGGER.error(battleStartError.toString());
                });

                if (!type.equals(NeoBattleType.PLAYER_V_PLAYER)) {
                    this.entity.kill();
                }

                return null;
            });
        });

    }

    public void finishBattle() {
        if (!type.equals(NeoBattleType.PLAYER_V_PLAYER)) {
            this.entity.kill();
        }
    }
}
