package xyz.neonetwork.neocobblelib.utils;

import com.google.gson.*;
import org.apache.commons.io.IOUtils;
import xyz.neonetwork.neocobblelib.NeoConstants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;

public class ConfigFile {

    private final String path;
    private final String fileName;
    private File file;

    private JsonObject jsonObj;

    public ConfigFile(String name) {
        if (!name.endsWith(".json")) {
            name = name + ".json";
        }

        this.path = "./config/NeoCobbleLib";
        this.fileName = name;
        this.file = new File(this.path + "/" + this.fileName);

        checkDir();
        checkFile();
    }

    public void checkDir() {
        File dir = new File(this.path);

        if (!dir.exists()) {
            dir.mkdir();
        }
    }

    public void checkFile() {
        if (file.exists()) {
            return;
        }

        try {
            file.createNewFile();
        } catch (IOException exception) {
            NeoConstants.LOGGER.error("Error creating config file.");
            exception.printStackTrace();
        }
    }

    public void loadConfig() {
        this.file = new File(this.path + "/" + this.fileName);

        if (file.length() == 0) {
            switch (this.fileName) {
                case "trainers.json" -> setDefaultsForConfig();
                default -> {
                    NeoConstants.LOGGER.error("Unknown Config Name Supplied. [{}]", this.fileName);
                    break;
                }
            }
        }

        try (FileInputStream inputStream = new FileInputStream(this.path + "/" + this.fileName)) {
            this.jsonObj = JsonParser.parseString(IOUtils.toString(inputStream, Charset.defaultCharset())).getAsJsonObject();
        } catch (IOException e) {
            NeoConstants.LOGGER.error("Error loading config file. Continuing to create new...");
        }
    }

    public void setDefaultsForConfig() {
        // Main Trainers Object
        JsonObject configObject = new JsonObject();
        JsonArray trainerArray = new JsonArray();

        // Individual Trainer
        JsonObject trainer = new JsonObject();
        trainer.addProperty("Name", "Brock");
        trainer.addProperty("Colour", "DARK_RED");

        JsonObject party = new JsonObject();

        JsonObject pokemonOne = new JsonObject();
        pokemonOne.addProperty("Name", "Geodude");
        pokemonOne.addProperty("Species", "Geodude");
        pokemonOne.addProperty("Level", 12);

        JsonObject pokemonTwo = new JsonObject();
        pokemonTwo.addProperty("Name", "Onix");
        pokemonTwo.addProperty("Species", "Onix");
        pokemonTwo.addProperty("Level", 14);

        party.addProperty("_comment", "Only 6 Pokemon can be registered. Any Pokemon after 6 will be ignored.");
        party.add("1", pokemonOne);
        party.add("2", pokemonTwo);

        trainer.add("Party", party);
        trainerArray.add(trainer);

        configObject.add("Trainers", trainerArray);

        pushToFile(configObject);
    }

    public void pushToFile(JsonObject objectToPush) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (FileWriter writer = new FileWriter(this.path + "/" + this.fileName)) {
            gson.toJson(objectToPush, writer);
        } catch (IOException exception) {
            NeoConstants.LOGGER.error("Failed to write {} file defaults. \n{}", this.fileName, exception);
        }
    }

    public JsonObject getConfig() {
        if (jsonObj == null) {
            NeoConstants.LOGGER.error("Config was not loaded before getting.");

            return null;
        }

        return jsonObj;
    }

    public JsonObject get(String path) {
        if (jsonObj == null) {
            NeoConstants.LOGGER.error("Config was not loaded before grabbing data.");

            return null;
        }

        return jsonObj.getAsJsonObject(path);
    }

    public JsonElement getElement(String path) {
        if (jsonObj == null) {
            NeoConstants.LOGGER.error("Config was not loaded before grabbing data.");

            return null;
        }

        return jsonObj.get(path);
    }

    public boolean isModEnabled() {
        if (jsonObj == null) {
            NeoConstants.LOGGER.error("Config was not loaded before grabbing data.");

            return true;
        }

        return jsonObj.get("Enabled").getAsBoolean();
    }

}
