package xyz.neonetwork.neocobblelib.objects;

import net.minecraft.server.level.ServerPlayer;

import java.util.HashMap;

public class TournamentManager {
    private static HashMap<String, Tournament> playerTournaments = new HashMap<>();

    public static Tournament createTournament(ServerPlayer player, TournamentType type) {
        Tournament tournament = new Tournament(player, type);

        playerTournaments.put(player.getStringUUID(), tournament);

        return tournament;
    }

    public static boolean inTournament(ServerPlayer player) {
        return playerTournaments.containsKey(player.getStringUUID());
    }

    public static void addTournament(Tournament tournament) {
        playerTournaments.put(tournament.getPlayer().getStringUUID(), tournament);
    }

    public static void removeTournament(Tournament tournament) {
        playerTournaments.remove(tournament.getPlayer().getStringUUID());
    }

    public static Tournament getTournament(ServerPlayer player) {
        return playerTournaments.get(player.getStringUUID());
    }

    public static void invalidateTournaments() {
        playerTournaments.forEach((string, tournaments) -> {
            tournaments.finishBattle();
        });

        playerTournaments = new HashMap<>();
    }
}
