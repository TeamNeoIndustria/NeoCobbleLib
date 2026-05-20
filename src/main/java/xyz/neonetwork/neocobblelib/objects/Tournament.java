package xyz.neonetwork.neocobblelib.objects;

import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.List;

public class Tournament {

    private final ServerPlayer player;
    private final List<NPCParty> npcParties;
    private int battleCounter;

    public Tournament(ServerPlayer player) {
        this.player = player;
        this.npcParties = new ArrayList<>();
        this.battleCounter = 0;
    }

    public ServerPlayer getPlayer() {
        return player;
    }

    public List<NPCParty> getNPCParties() {
        return npcParties;
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
