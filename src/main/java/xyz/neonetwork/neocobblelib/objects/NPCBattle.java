package xyz.neonetwork.neocobblelib.objects;

import net.minecraft.server.level.ServerPlayer;

public class NPCBattle {

    public ServerPlayer player;
    public NPCParty npcParty;

    public NPCBattle(ServerPlayer player, NPCParty party) {
        this.player = player;
        this.npcParty = party;
    }

    public ServerPlayer getPlayer() {
        return player;
    }

    public NPCParty getNpcParty() {
        return npcParty;
    }

    public void startBattle() {

    }

    public void finishBattle() {

    }
}
