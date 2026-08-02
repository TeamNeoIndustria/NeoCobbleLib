package xyz.neonetwork.neocobblelib;

import com.mojang.logging.LogUtils;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;
import xyz.neonetwork.neocobblelib.utils.ConfigFile;

public class NeoConstants {
    // Mod
    public static final String MODID = "neocobblelib";
    public static final Logger LOGGER = LogUtils.getLogger();

    // Server
    public static MinecraftServer SERVER = null;

    // Configs
    public static ConfigFile TRAINER_CONFIG;
}
