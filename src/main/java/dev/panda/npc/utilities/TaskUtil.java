package dev.panda.npc.utilities;

import lombok.experimental.UtilityClass;
import org.bukkit.scheduler.BukkitRunnable;
import dev.panda.npc.PandaNPC;

@UtilityClass
public class TaskUtil {
    
    public void run(Runnable runnable) {
        PandaNPC.get().getServer().getScheduler().runTask(PandaNPC.get(), runnable);
    }

    public void runTimer(Runnable runnable, long delay, long timer) {
        PandaNPC.get().getServer().getScheduler().runTaskTimer(PandaNPC.get(), runnable, delay, timer);
    }

    public void runTimer(BukkitRunnable runnable, long delay, long timer) {
        runnable.runTaskTimer(PandaNPC.get(), delay, timer);
    }

    public void runLater(Runnable runnable, long delay) {
        PandaNPC.get().getServer().getScheduler().runTaskLater(PandaNPC.get(), runnable, delay);
    }

    public void runAsync(Runnable runnable) {
        PandaNPC.get().getServer().getScheduler().runTaskAsynchronously(PandaNPC.get(), runnable);
    }
}
