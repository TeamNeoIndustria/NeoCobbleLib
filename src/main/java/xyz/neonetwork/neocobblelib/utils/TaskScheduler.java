package xyz.neonetwork.neocobblelib.utils;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TaskScheduler {
    private static final Queue<Runnable> NEXT_TICK = new ConcurrentLinkedQueue<>();

    public static void nextTickTask(Runnable task) {
        NEXT_TICK.add(task);
    }

    @SubscribeEvent
    public static void serverTick(ServerTickEvent.Post event) {
        Runnable task;
        while ((task = NEXT_TICK.poll()) != null) {
            task.run();
        }
    }
}
