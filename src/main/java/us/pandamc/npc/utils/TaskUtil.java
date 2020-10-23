package us.pandamc.npc.utils;

import org.bukkit.scheduler.BukkitRunnable;
import us.pandamc.npc.PandaNPC;

public class TaskUtil {
    
    public static void run(Runnable runnable) {
        PandaNPC.get().getServer().getScheduler().runTask(PandaNPC.get(), runnable);
    }

    public static void runTimer(Runnable runnable, long delay, long timer) {
        PandaNPC.get().getServer().getScheduler().runTaskTimer(PandaNPC.get(), runnable, delay, timer);
    }

    public static void runTimer(BukkitRunnable runnable, long delay, long timer) {
        runnable.runTaskTimer(PandaNPC.get(), delay, timer);
    }

    public static void runLater(Runnable runnable, long delay) {
        PandaNPC.get().getServer().getScheduler().runTaskLater(PandaNPC.get(), runnable, delay);
    }

    public static void runAsync(Runnable runnable) {
        PandaNPC.get().getServer().getScheduler().runTaskAsynchronously(PandaNPC.get(), runnable);
    }
}
