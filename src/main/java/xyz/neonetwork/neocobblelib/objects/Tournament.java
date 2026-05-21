package xyz.neonetwork.neocobblelib.objects;

import de.markusbordihn.easynpc.api.handler.EasyNPCEntityHandler;
import de.markusbordihn.easynpc.data.npc.SavedNPCEntityEntry;
import de.markusbordihn.easynpc.entity.NPCEntityManager;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class Tournament {

    private final ServerPlayer player;
    private final UUID npcUUID;
    private final List<NPCParty> npcParties;
    private final TournamentType type;
    private int battleCounter;

    public Tournament(ServerPlayer player, TournamentType type) {
        this.player = player;
        this.npcUUID = UUID.randomUUID();
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

    public void spawnNPC() {
        EasyNPCEntityHandler.spawn(this.npcUUID, this.player.serverLevel(), this.player.position());
    }

    public Optional<SavedNPCEntityEntry> getNPC() {
        return NPCEntityManager.getNPC(this.npcUUID);
    }

    public void startBattle() {
        NPCParty party = npcParties.get(battleCounter);
        NPCBattle battle = new NPCBattle(this.player, party);

        // Start fight
        battle.startBattle();
    }

    public void finishBattle() {
        this.battleCounter++;
    }
}
