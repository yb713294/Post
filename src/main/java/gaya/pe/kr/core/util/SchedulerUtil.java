package gaya.pe.kr.core.util;

import gaya.pe.kr.core.ItemMail;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

public class SchedulerUtil {

    private static final BukkitScheduler BUKKIT_SCHEDULER = Bukkit.getScheduler();

    public static void cancel(int taskId) {
        BUKKIT_SCHEDULER.cancelTask(taskId);
    }

    private static final Plugin plugin = ItemMail.getPlugin();

    public static int scheduleRepeatingTask(final Runnable task, int delay, int interval) {
        return BUKKIT_SCHEDULER.scheduleSyncRepeatingTask(plugin, task, delay, interval);
    }

    public static void runTaskLater(final Runnable task, int delay) {
        BUKKIT_SCHEDULER.runTaskLater(plugin, task, delay);
    }

    public static void runAsync(final Runnable task) {
        BUKKIT_SCHEDULER.runTaskAsynchronously(plugin, task);
    }

}
