package xyz.neonetwork.neocobblelib.tournament;

import com.cobblemon.mod.common.api.storage.party.NPCPartyStore;
import com.cobblemon.mod.common.entity.npc.NPCEntity;
import de.markusbordihn.easynpc.data.npc.SavedNPCEntityEntry;
import de.markusbordihn.easynpc.entity.NPCEntityFactory;
import de.markusbordihn.easynpc.entity.NPCEntityManager;
import de.markusbordihn.easynpc.entity.easynpc.EasyNPC;
import de.markusbordihn.easynpc.entity.easynpc.data.OwnerDataCapable;
import net.minecraft.network.chat.Component;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.Villager;
import xyz.neonetwork.neocobblelib.NeoConstants;
import xyz.neonetwork.neocobblelib.cobblemon.CobblemonUtils;

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
        Villager villager = new Villager(EntityType.VILLAGER, level);

        villager.moveTo(
                this.player.getX(),
                this.player.getY(),
                this.player.getZ(),
                this.player.getYRot(),
                this.player.getXRot()
        );

        villager.setNoAi(true);

        villager.setCustomName(Component.literal("Trainer"));
        villager.setCustomNameVisible(true);
        villager.setInvulnerable(true);
        villager.setPersistenceRequired();

        level.addFreshEntity(villager);

        this.entity = villager;
        this.npcUUID = villager.getUUID();
    }

    public void despawnNPC() {
        this.entity.kill();
    }

    public void startBattle() {
        if (npcParties.isEmpty()) {
            populateNPCParties();
        }

        NPCParty party = npcParties.get(battleCounter);
        NPCEntity npcEntity = new NPCEntity(this.player.serverLevel());

        npcEntity.setParty(party.getAsNPCPartyStore(npcEntity));

        NPCBattle battle = new NPCBattle(this.player, npcEntity, party);

        // Start fight
        battle.startBattle();
    }

    public void finishBattle() {
        this.battleCounter++;
    }
}
