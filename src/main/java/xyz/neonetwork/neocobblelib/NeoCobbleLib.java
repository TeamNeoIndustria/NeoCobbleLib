package xyz.neonetwork.neocobblelib;

import com.cobblemon.mod.common.api.events.CobblemonEvents;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import xyz.neonetwork.neocobblelib.battles.NeoBattle;
import xyz.neonetwork.neocobblelib.battles.NeoBattleManager;
import xyz.neonetwork.neocobblelib.cobblemon.CobblemonEventListener;
import xyz.neonetwork.neocobblelib.cobblemon.CobblemonTrainerManager;
import xyz.neonetwork.neocobblelib.commands.MainCommand;
import xyz.neonetwork.neocobblelib.utils.ConfigFile;
import xyz.neonetwork.neocobblelib.utils.TaskScheduler;

@Mod(NeoConstants.MODID)
public class NeoCobbleLib {
    public NeoCobbleLib(IEventBus modEventBus, ModContainer modContainer) {
        CobblemonEvents.BATTLE_VICTORY.subscribe(CobblemonEventListener::onBattleVictory);
        CobblemonEvents.BATTLE_FLED.subscribe(CobblemonEventListener::onBattleFled);
        NeoForge.EVENT_BUS.register(this);
        NeoForge.EVENT_BUS.register(TaskScheduler.class);

        NeoConstants.TRAINER_CONFIG = new ConfigFile("trainers");
        NeoConstants.TRAINER_CONFIG.loadConfig();
    }

    @SubscribeEvent
    public void onCommandRegister(RegisterCommandsEvent event) {
        MainCommand.register(event.getDispatcher());
    }

    @SubscribeEvent
    public void onServerStart(ServerStartingEvent event) {
        NeoConstants.SERVER = event.getServer();

        CobblemonTrainerManager.loadTrainers();
    }

    @SubscribeEvent
    public void onPlayerLeave(PlayerEvent.PlayerLoggedOutEvent event) {
        ServerPlayer player = (ServerPlayer) event.getEntity();

        NeoBattleManager.finialiseBattle(player);
    }
}
