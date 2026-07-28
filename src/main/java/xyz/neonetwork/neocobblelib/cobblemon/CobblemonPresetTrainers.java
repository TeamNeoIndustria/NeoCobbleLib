package xyz.neonetwork.neocobblelib.cobblemon;

import com.cobblemon.mod.common.pokemon.Pokemon;
import net.minecraft.ChatFormatting;
import xyz.neonetwork.neocobblelib.battles.NeoNPCParty;

public class CobblemonPresetTrainers {
    private final String name;
    private final ChatFormatting nameColour;
    private final NeoNPCParty party;

    CobblemonPresetTrainers(String name, ChatFormatting nameColour, Pokemon... party) {
        this.name = name;
        this.nameColour = nameColour;
        NeoNPCParty npcParty = new NeoNPCParty();

        for (Pokemon pokemon : party) {
            npcParty.addPokemon(pokemon);
        }

        this.party = npcParty;
    }

    CobblemonPresetTrainers(String name, ChatFormatting nameColour, NeoNPCParty party) {
        this.name = name;
        this.nameColour = nameColour;
        this.party = party;
    }

    public String getName() {
        return name;
    }

    public ChatFormatting getNameColour() {
        return nameColour;
    }

    public NeoNPCParty getParty() {
        return party;
    }
}
