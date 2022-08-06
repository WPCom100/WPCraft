package cloud.wpcom.tasks;

import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;
import org.checkerframework.checker.nullness.qual.NonNull;

import cloud.wpcom.WPCraft;
import cloud.wpcom.commandsleeper.CSUtil;
import cloud.wpcom.commandsleeper.CommandSleeper;

public class CSNightWatcherTask extends BukkitRunnable {

    private final WPCraft wpcraft;
    private final World world;
    private final CommandSleeper commandSleeper;
    private boolean isWatching;

    public CSNightWatcherTask(@NonNull WPCraft wpcraft, @NonNull CommandSleeper commandSleeper, @NonNull World world) {
        this.wpcraft = wpcraft;
        this.commandSleeper = commandSleeper;
        this.world = world;
        this.isWatching = false;
    }

    @Override
    public void run() {

        // Checks if all players are sleeping, if so skips the night and stops watching
        if (CSUtil.getNeededToSleep(world) <= CSUtil.getNumberSleeping(world, commandSleeper)) {
            new CSNightSkipperTask(wpcraft, commandSleeper, world);
            cancel();
        }
        // POSSIBLY break plugin if you sleep while the night is skipping
        // TODO bossbar status message updates go here
    }
    
    // Stops watching a world
    @Override
    public void cancel() {
        isWatching = false;
        // Remove boss bar
        super.cancel();
    }
    
    // Returns if the night is activly being watched
    public boolean isWatching() {
        return isWatching;
    }

    // Starts task to watch for a night to be skipped
    public void watch() {
        isWatching = true;
        runTaskTimer(wpcraft, 1, 20);
    }    
}
