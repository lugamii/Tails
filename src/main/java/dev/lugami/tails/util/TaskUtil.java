package dev.lugami.tails.util;

import dev.lugami.tails.Tails;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TaskUtil {

    private static final ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(8);

    public static void scheduleAtFixedRateOnPool(Runnable runnable, long delay, long period, TimeUnit timeUnit) {
        scheduledThreadPoolExecutor.scheduleAtFixedRate(runnable, delay, period, timeUnit);
    }

    public static void scheduleOnPool(Runnable runnable, long delay, TimeUnit timeUnit) {
        scheduledThreadPoolExecutor.schedule(runnable, delay, timeUnit);
    }

    public static void executeWithPool(Runnable runnable) {
        scheduledThreadPoolExecutor.execute(runnable);
    }

    public static void run(Runnable runnable) {
        Tails.get().getServer().getScheduler().runTask(Tails.get(), runnable);
    }

    public static void runAsync(Runnable runnable) {
        try {
            Tails.get().getServer().getScheduler().runTaskAsynchronously(Tails.get(), runnable);
        } catch (IllegalStateException e) {
            Tails.get().getServer().getScheduler().runTask(Tails.get(), runnable);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public static void runTimer(Runnable runnable, long delay, long timer) {
        Tails.get().getServer().getScheduler().runTaskTimer(Tails.get(), runnable, delay, timer);
    }

    public static int runTimer(BukkitRunnable runnable, long delay, long timer) {
        runnable.runTaskTimer(Tails.get(), delay, timer);
        return runnable.getTaskId();
    }

    public static void runLater(Runnable runnable, long delay) {
        Tails.get().getServer().getScheduler().runTaskLater(Tails.get(), runnable, delay);
    }

    public static void runLaterAsync(Runnable runnable, long delay) {
        try {
            Tails.get().getServer().getScheduler().runTaskLaterAsynchronously(Tails.get(), runnable, delay);
        } catch (IllegalStateException e) {
            Tails.get().getServer().getScheduler().runTaskLater(Tails.get(), runnable, delay);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public static void runTimerAsync(Runnable runnable, long delay, long timer) {
        try {
            Tails.get().getServer().getScheduler().runTaskTimerAsynchronously(Tails.get(), runnable, delay, timer);
        } catch (IllegalStateException e) {
            Tails.get().getServer().getScheduler().runTaskTimer(Tails.get(), runnable, delay, timer);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public static void runTimerAsync(BukkitRunnable runnable, long delay, long timer) {
        Tails.get().getServer().getScheduler().runTaskTimerAsynchronously(Tails.get(), runnable, delay, timer);
    }

}
