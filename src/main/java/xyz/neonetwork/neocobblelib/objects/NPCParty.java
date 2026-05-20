package xyz.neonetwork.neocobblelib.objects;

import com.cobblemon.mod.common.api.storage.party.PartyPosition;
import com.cobblemon.mod.common.pokemon.Pokemon;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NPCParty {

    private final String uuid;
    private HashMap<PartyPosition, Pokemon> party;
    private boolean isDefeated;

    public NPCParty() {
        this.uuid = UUID.randomUUID().toString();
        this.party = new HashMap<>();
        this.isDefeated = false;
    }

    public NPCParty(HashMap<PartyPosition, Pokemon> party) {
        this.uuid = UUID.randomUUID().toString();
        this.party = party;
        this.isDefeated = false;
    }

    public String getUuid() {
        return uuid;
    }

    public HashMap<PartyPosition, Pokemon> getParty() {
        return party;
    }

    public Pokemon getPokemon(PartyPosition position) {
        return this.party.get(position);
    }

    public void setPokemon(PartyPosition position, Pokemon pokemon) {
        this.party.put(position, pokemon);
    }

    public void addPokemon(Pokemon pokemon) {
        this.party.put(new PartyPosition(this.party.size()), pokemon);
    }

    public void removePokemon(PartyPosition position) {
        this.party.remove(position);
    }

    public void removePokemon(Pokemon pokemon) {
        PartyPosition position = null;

        for (Map.Entry<PartyPosition, Pokemon> entry : this.party.entrySet()) {
            if (pokemon == entry.getValue()) {
                position = entry.getKey();
                break;
            }
        }

        if (position == null) {
            return;
        }

        removePokemon(position);
    }

    public void setDefeated(boolean isDefeated) {
        this.isDefeated = isDefeated;
    }

    public boolean isDefeated() {
        return isDefeated;
    }

    public void invalidateParty() {
        this.party = new HashMap<>();
    }

}
