package xyz.neonetwork.neocobblelib.battles.events;

import com.cobblemon.mod.common.entity.npc.NPCEntity;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.Event;

public class TrainerBattleEndEvent extends Event {
    public enum Outcome {
        PLAYER_WON,
        PLAYER_LOST,
        PLAYER_FLED
    }

    private final ServerPlayer player;
    private final NPCEntity npc;
    private final Outcome outcome;

    public TrainerBattleEndEvent(ServerPlayer player, NPCEntity npc, Outcome outcome) {
        this.player = player;
        this.npc = npc;
        this.outcome = outcome;
    }

    public ServerPlayer getPlayer() {
        return player;
    }

    public NPCEntity getNpc() {
        return npc;
    }

    public Outcome getOutcome() {
        return outcome;
    }
}