package xyz.neonetwork.neocobblelib.tournament;

import com.cobblemon.mod.common.api.storage.party.NPCPartyStore;
import com.cobblemon.mod.common.api.storage.party.PartyPosition;
import com.cobblemon.mod.common.api.storage.party.PartyStore;
import com.cobblemon.mod.common.entity.npc.NPCEntity;
import com.cobblemon.mod.common.pokemon.Pokemon;

import java.util.HashMap;
import java.util.UUID;

public class NPCParty {

    private final String uuid;
    private final String partyUUID;
    private final HashMap<PartyPosition, Pokemon> party;
    private boolean isDefeated;

    public NPCParty() {
        this.uuid = UUID.randomUUID().toString();
        this.partyUUID = UUID.randomUUID().toString();
        this.party = new HashMap<>();
        this.isDefeated = false;
    }

    public NPCParty(HashMap<PartyPosition, Pokemon> party) {
        this.uuid = UUID.randomUUID().toString();
        this.partyUUID = UUID.randomUUID().toString();
        this.party = party;
        this.isDefeated = false;
    }

    public String getUuid() {
        return uuid;
    }

    public PartyStore getParty() {
        PartyStore partyStore = new PartyStore(UUID.fromString(this.partyUUID));

        this.party.forEach((partyStore::set));

        return partyStore;
    }

    public NPCPartyStore getAsNPCPartyStore(NPCEntity entity) {
        NPCPartyStore npcPartyStore = new NPCPartyStore(entity);

        this.party.forEach(npcPartyStore::set);

        return npcPartyStore;
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
        this.party.remove(pokemon);
    }

    public void setDefeated(boolean isDefeated) {
        this.isDefeated = isDefeated;
    }

    public boolean isDefeated() {
        return isDefeated;
    }

    public void invalidateParty() {
        this.party.clear();
    }

}
