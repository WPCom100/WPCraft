package cloud.wpcom.tasks;

import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

import cloud.wpcom.commandsleeper.CSUtil;
import cloud.wpcom.commandsleeper.CommandSleeper;

public class CSNightWatcherTask extends BukkitRunnable {

    private boolean isSkipping;
    private World world;
    private CommandSleeper commandSleeper;

    public CSNightWatcherTask(CommandSleeper commandSleeper) {
        this.isSkipping = false;
        this.commandSleeper = commandSleeper;
    }

    @Override
    public void run() {
        // Checks if all players are sleeping, if so skips the night
        if (CSUtil.getNeededToSleep(world) <= CSUtil.getNumberSleeping(world, commandSleeper)) {
            skipNight();

            /**
             * STOPPED HERE
             * What starts the CSNightWatcher task?
             * What stops the task if everyone is out of bed?
             * Make a new CS Night Watcher everty time a player sleeps in a world?
             * remove it once night skips or everyone is out of bed?
            */
        }
    }
    
    // Returns if the night is activly being skipped
    public boolean isSkipping() {
        return isSkipping;
    }

    // Sets the world to watch for possible skipping
    public void setWorld(World world) {
        this.world = world;
    }

    private void skipNight() {
        isSkipping = true;

        /**
             * STOPPED HERE
             * What starts the CSNightWatcher task?
             * What stops the task if everyone is out of bed?
             * Make a new CS Night Watcher everty time a player sleeps in a world?
             * remove it once night skips or everyone is out of bed?
            */

        // create Skip night task

        //Send message

        //Get people out of bed at the end of the night skip

        //clear command sleepers
    }
    
}
