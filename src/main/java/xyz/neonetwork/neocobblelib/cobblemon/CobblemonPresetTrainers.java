package xyz.neonetwork.neocobblelib.cobblemon;

import com.cobblemon.mod.common.pokemon.Pokemon;
import net.minecraft.ChatFormatting;
import xyz.neonetwork.neocobblelib.battles.NeoNPCParty;

public enum CobblemonPresetTrainers {

    BROCK("Brock", ChatFormatting.DARK_RED,
            CobblemonUtils.createPokemon("Geodude", CobblemonSpecies.GEODUDE, 12),
            CobblemonUtils.createPokemon("Onix", CobblemonSpecies.ONIX, 14)
    ),
    MISTY("Misty", ChatFormatting.AQUA,
            CobblemonUtils.createPokemon("Staryu", CobblemonSpecies.STARYU, 18),
            CobblemonUtils.createPokemon("Starmie", CobblemonSpecies.STARMIE, 21)
    );

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

    public String getName() {
        return name;
    }

    public ChatFormatting getNameColour() {
        return nameColour;
    }

    public NeoNPCParty getParty() {
        return party;
    }

    public static CobblemonPresetTrainers fromString(String trainerName) {
        for (CobblemonPresetTrainers trainers : values()) {
            if (trainers.getName().equalsIgnoreCase(trainerName)) {
                return trainers;
            }
        }

        return null;
    }
}
