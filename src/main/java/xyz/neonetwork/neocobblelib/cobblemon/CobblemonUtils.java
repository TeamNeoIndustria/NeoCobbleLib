package xyz.neonetwork.neocobblelib.cobblemon;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.pokemon.Natures;
import com.cobblemon.mod.common.api.pokemon.PokemonProperties;
import com.cobblemon.mod.common.api.storage.party.PartyPosition;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.Nature;
import com.cobblemon.mod.common.pokemon.Pokemon;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.MobSpawnType;
import xyz.neonetwork.neocobblelib.NeoConstants;
import xyz.neonetwork.neocobblelib.tournament.NPCParty;

import java.util.concurrent.ThreadLocalRandom;

public class CobblemonUtils {

    public static Pokemon generateRandomPokemon(int lowerLevel, int higherLevel) {
        String species = CobblemonSpecies.getRandomName();
        int level;

        if (lowerLevel == higherLevel) {
            level = lowerLevel;
        } else {
            level = ThreadLocalRandom.current().nextInt(lowerLevel, higherLevel);
        }

        Nature nature = Natures.getRandomNature();
        String toParse = String.format("species=%s level=%s nature=%s", species, level, nature.getDisplayName().split("\\.")[2]);

        return PokemonProperties.Companion.parse(toParse).create();
    }

    public static void spawnPokemonAtLocation(Pokemon pokemon, ServerLevel level, BlockPos position) {
        PokemonEntity entity = PokemonProperties.Companion.parse(String.format("species=%s level=%s nature=%s", pokemon.getSpecies().getName(), pokemon.getLevel(), pokemon.getNature().getDisplayName().split("\\.")[2])).createEntity(level);

        entity.moveTo(position.getX(), position.getY(), position.getZ(), entity.yRotO, entity.xRotO);
        entity.finalizeSpawn(level, level.getCurrentDifficultyAt(position), MobSpawnType.COMMAND, null);

        if (!level.addFreshEntity(entity)) {
            NeoConstants.LOGGER.error("Error spawning Pokemon");
        }
    }

    public static void setPlayerPartySlot(ServerPlayer player, int slot, Pokemon pokemon) {
        Cobblemon.INSTANCE.getStorage().getParty(player).set(new PartyPosition(slot), pokemon);
    }

    public static void setPlayerPartyRandom(ServerPlayer player) {
        for (int i = 0; i < 6; i++) {
            Cobblemon.INSTANCE.getStorage().getParty(player).set(new PartyPosition(i), generateRandomPokemon(1, 100));
        }
    }

    public static NPCParty randomiseNPCParty(NPCParty party, int amount, int lowerLevel, int upperLevel) {
        party.invalidateParty();

        for (int i = 0; i < amount; i++) {
            party.addPokemon(generateRandomPokemon(lowerLevel, upperLevel));
        }

        return party;
    }
}
