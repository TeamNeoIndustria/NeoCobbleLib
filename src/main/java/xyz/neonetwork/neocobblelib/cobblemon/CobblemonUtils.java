package xyz.neonetwork.neocobblelib.cobblemon;

import com.cobblemon.mod.common.api.pokemon.Natures;
import com.cobblemon.mod.common.api.pokemon.PokemonProperties;
import com.cobblemon.mod.common.pokemon.Nature;
import com.cobblemon.mod.common.pokemon.Pokemon;

import java.util.concurrent.ThreadLocalRandom;

public class CobblemonUtils {

    public Pokemon generateRandomPokemon(int lowerLevel, int higherLevel) {
        String species = CobblemonSpecies.getRandomName();
        int level = ThreadLocalRandom.current().nextInt(lowerLevel, higherLevel);
        Nature nature = Natures.getRandomNature();

        Pokemon pokemon = PokemonProperties.Companion.parse(String.format("species=%s, level=%s, nature=%s", species, level, nature.getDisplayName())).create();

        return pokemon;
    }
}
