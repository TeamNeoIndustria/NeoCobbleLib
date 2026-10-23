package xyz.neonetwork.neocobblelib.cobblemon;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.pokemon.PokemonProperties;
import com.cobblemon.mod.common.api.storage.party.PartyPosition;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.Pokemon;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.MobSpawnType;
import xyz.neonetwork.neocobblelib.NeoConstants;
import xyz.neonetwork.neocobblelib.battles.NeoNPCParty;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

public class CobblemonUtils {
    public static Pokemon generateRandomPokemon(int lowerLevel, int higherLevel) {
        String species = CobblemonSpecies.getRandomName();
        int level;

        if (lowerLevel == higherLevel) {
            level = lowerLevel;
        } else {
            level = ThreadLocalRandom.current().nextInt(lowerLevel, higherLevel);
        }

        String toParse = String.format("species=%s level=%s scale=1", species, level);

        return PokemonProperties.Companion.parse(toParse).create();
    }

    public static Pokemon createPokemon(String name, CobblemonSpecies species, int level) {
        String toParse = String.format("name=%s species=%s level=%s scale=1", name, species.getName(), level);

        return PokemonProperties.Companion.parse(toParse).create();
    }

    public static PokemonEntity spawnPokemonAtLocation(Pokemon pokemon, ServerLevel level, BlockPos position) {
        String toParse = String.format("species=%s level=%s nature=%s", pokemon.getSpecies().getName(), pokemon.getLevel(), pokemon.getNature().getDisplayName().split("\\.")[2]);
        PokemonEntity entity = PokemonProperties.Companion.parse(toParse).createEntity(level);

        entity.moveTo(position.getX(), position.getY(), position.getZ(), entity.yRotO, entity.xRotO);
        entity.finalizeSpawn(level, level.getCurrentDifficultyAt(position), MobSpawnType.COMMAND, null);

        if (!level.addFreshEntity(entity)) {
            NeoConstants.LOGGER.error("Error spawning Pokemon");
        }

        return entity;
    }

    public static Optional<PokemonEntity> spawnWildPokemon(ServerLevel level, CobblemonSpecies species, int pokemonLevel,
                                                           double x, double y, double z,
                                                           Consumer<PokemonEntity> beforeSpawn) {
        String toParse = String.format("species=%s, level=%s", species.getName(), pokemonLevel);
        PokemonEntity entity = PokemonProperties.Companion.parse(toParse).createEntity(level);

        entity.moveTo(x, y, z, 0F, 0F);
        entity.finalizeSpawn(level, level.getCurrentDifficultyAt(entity.blockPosition()), MobSpawnType.EVENT, null);
        entity.setPersistenceRequired();

        beforeSpawn.accept(entity);

        if (!level.addFreshEntity(entity)) {
            NeoConstants.LOGGER.error("Failed to add {} to the world at {}, {}, {}.", species.getName(), x, y, z);
            return Optional.empty();
        }

        return Optional.of(entity);
    }

    public static void setPlayerPartySlot(ServerPlayer player, int slot, Pokemon pokemon) {
        Cobblemon.INSTANCE.getStorage().getParty(player).set(new PartyPosition(slot), pokemon);
    }

    public static void setPlayerPartyRandom(ServerPlayer player) {
        for (int i = 0; i < 6; i++) {
            Cobblemon.INSTANCE.getStorage().getParty(player).set(new PartyPosition(i), generateRandomPokemon(1, 100));
        }
    }

    public static NeoNPCParty randomiseNPCParty(NeoNPCParty party, int amount, int lowerLevel, int upperLevel) {
        party.invalidateParty();

        for (int i = 0; i < amount; i++) {
            party.addPokemon(generateRandomPokemon(lowerLevel, upperLevel));
        }

        return party;
    }

    public static ChatFormatting getRandomColour() {
        ChatFormatting[] pokemonColours = new ChatFormatting[] {
                ChatFormatting.RED,
                ChatFormatting.BLUE,
                ChatFormatting.YELLOW,
                ChatFormatting.GRAY,
                ChatFormatting.GOLD,
                ChatFormatting.DARK_PURPLE
        };

        return pokemonColours[ThreadLocalRandom.current().nextInt(pokemonColours.length)-1];
    }
}
