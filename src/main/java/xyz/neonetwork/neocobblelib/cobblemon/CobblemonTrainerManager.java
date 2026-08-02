package xyz.neonetwork.neocobblelib.cobblemon;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.ChatFormatting;
import xyz.neonetwork.neocobblelib.NeoConstants;
import xyz.neonetwork.neocobblelib.battles.NeoNPCParty;

import java.util.HashMap;
import java.util.Map;

public class CobblemonTrainerManager {
    private static final HashMap<String, CobblemonPresetTrainers> trainers = new HashMap<>();

    public static void addTrainer(String name, CobblemonPresetTrainers trainer) {
        if (trainers.containsKey(name)) {
            return;
        }

        trainers.put(name, trainer);
    }

    public static void removeTrainer(String name) {
        if (!trainers.containsKey(name)) {
            return;
        }

        trainers.remove(name);
    }

    public static CobblemonPresetTrainers getTrainer(String name) {
        return trainers.get(name);
    }

    public static HashMap<String, CobblemonPresetTrainers> getAllTrainers() {
        return trainers;
    }

    public static void loadTrainers() {
        for (JsonElement element : NeoConstants.TRAINER_CONFIG.getConfig().get("Trainers").getAsJsonArray()) {
            JsonObject trainerObject = element.getAsJsonObject();
            JsonObject partyObject = trainerObject.get("Party").getAsJsonObject();
            NeoNPCParty party = new NeoNPCParty();

            for (Map.Entry<String, JsonElement> entry : partyObject.asMap().entrySet()) {
                if (entry.getKey().equals("_comment")) {
                    continue;
                }

                JsonElement pokemonElement = entry.getValue();
                JsonObject pokemonObject = pokemonElement.getAsJsonObject();
                CobblemonSpecies species = CobblemonSpecies.fromString(pokemonObject.get("Species").getAsString());

                if (species == null) {
                    continue;
                }

                party.addPokemon(CobblemonUtils.createPokemon(pokemonObject.get("Name").getAsString(), species, pokemonObject.get("Level").getAsInt()));
            }

            ChatFormatting nameColour = ChatFormatting.valueOf(trainerObject.get("Colour").getAsString());

            addTrainer(trainerObject.get("Name").getAsString(), new CobblemonPresetTrainers(trainerObject.get("Name").getAsString(), nameColour == null ? ChatFormatting.WHITE : nameColour, party));
        }
    }
}
