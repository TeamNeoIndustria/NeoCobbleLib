package xyz.neonetwork.neocobblelib.tournament;

import com.cobblemon.mod.common.entity.npc.NPCEntity;
import de.markusbordihn.easynpc.data.npc.SavedNPCEntityEntry;
import de.markusbordihn.easynpc.entity.NPCEntityManager;
import de.markusbordihn.easynpc.entity.easynpc.EasyNPC;
import de.markusbordihn.easynpc.entity.easynpc.data.OwnerDataCapable;
import net.minecraft.ChatFormatting;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import xyz.neonetwork.neocobblelib.NeoConstants;
import xyz.neonetwork.neocobblelib.cobblemon.CobblemonUtils;
import xyz.neonetwork.neocobblelib.utils.NPCUtils;

import java.util.*;

public class Tournament {

    private final ServerPlayer player;
    private UUID npcUUID;
    private Entity entity;
    private final List<NPCParty> npcParties;
    private final TournamentType type;
    private int battleCounter;

    public Tournament(ServerPlayer player, TournamentType type) {
        this.player = player;
        this.npcUUID = UUID.randomUUID();
        this.entity = null;
        this.npcParties = new ArrayList<>();
        this.type = type;
        this.battleCounter = 0;
    }

    public ServerPlayer getPlayer() {
        return player;
    }

    public UUID getNpcUUID() {
        return npcUUID;
    }

    public List<NPCParty> getNPCParties() {
        return npcParties;
    }

    public TournamentType getType() {
        return type;
    }

    public Optional<SavedNPCEntityEntry> getNPC() {
        return NPCEntityManager.getNPC(this.npcUUID);
    }

    public void addNPCParty(NPCParty party) {
        this.npcParties.add(party);
    }

    public void removeNPCParty(NPCParty party) {
        this.npcParties.remove(party);
    }

    public void populateNPCParties() {
        switch (this.type) {
            case RANDOMISED -> {
                for (int i = 0; i < 3; i++) {
                    NPCParty party = new NPCParty();

                    for (int o = 0; o < 3; o++) {
                        party.addPokemon(CobblemonUtils.generateRandomPokemon(50, 50));
                    }

                    addNPCParty(party);
                }
            }
            default -> NeoConstants.LOGGER.error("Type not supported.");
        }
    }

    public void spawnNPC() {
        NeoConstants.LOGGER.info("spawnNPC");

        ServerLevel level = this.player.serverLevel();

        Entity entity = NPCUtils.createHumanoidNPC(level, "Trainer", ChatFormatting.WHITE, this.player.blockPosition());

        if (entity == null) {
            NeoConstants.LOGGER.error("Error spawning tournament npc for {}", this.player.getDisplayName().getString());

            return;
        }

        if (entity instanceof EasyNPC<?> easyNPC) {
            OwnerDataCapable<?> ownerData = easyNPC.getEasyNPCOwnerData();

            if (ownerData != null) {
                ownerData.setNPCOwnerUUID(this.player.getUUID());
            }
        }

        this.entity = entity;
        this.npcUUID = entity.getUUID();
    }

    public void despawnNPC() {
        this.entity.setInvisible(true);
        this.entity.kill();
    }

    public void startBattle() {
        if (npcParties.isEmpty()) {
            populateNPCParties();
        }

        NPCParty party = npcParties.get(battleCounter);
        NPCEntity npcEntity = new NPCEntity(this.player.serverLevel());

        npcEntity.setUUID(this.npcUUID);
        npcEntity.setParty(party.getAsNPCPartyStore(npcEntity));

        NPCBattle battle = new NPCBattle(this.player, npcEntity, party);

        // Start fight
        battle.startBattle();
    }

    public void finishBattle() {
        this.battleCounter++;
    }
}
