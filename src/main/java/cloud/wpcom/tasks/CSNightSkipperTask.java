package cloud.wpcom.tasks;

import javax.annotation.Nonnull;

import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;

import cloud.wpcom.WPCraft;
import cloud.wpcom.commandsleeper.CommandSleeper;

public class CSNightSkipperTask extends BukkitRunnable {

    private final WPCraft wpcraft;
    private final CommandSleeper commandSleeper;
    private final World world;
    private boolean isSkipping;

    public CSNightSkipperTask(@Nonnull WPCraft wpcraft, @Nonnull CommandSleeper commandSleeper, @Nonnull World world) {
        this.wpcraft = wpcraft;
        this.commandSleeper = commandSleeper;
        this.world = world;
        isSkipping = false;
    }

    @Override
    public void run() {

        long currentTime = world.getTime();

        if (currentTime >= 1000) {
            world.setTime(currentTime + 80);
        } else {
            cancel();
        }

    }

    // Called once the night is done skipping
    @Override
    public void cancel() {
        isSkipping = false;

        // Reset sleeping statistics
        world.getPlayers().forEach(player -> player.setStatistic(Statistic.TIME_SINCE_REST, 0));

        // Wake all players
        world.getPlayers().stream().filter(LivingEntity::isSleeping).forEach(player -> player.wakeup(true));

        // Set weather to none
        this.world.setStorm(false);
        this.world.setThundering(false);

        // Handles command sleepers gracefully
        commandSleeper.clearCommandSleepers();

        wpcraft.getServer()
                .broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                        "&f[&bWPCraft&f] &fNight skipped. Back to the mine with ya!"));

        super.cancel();

        //TODO remove the bossbar status
    }
    
    public boolean isSkipping() {
        return isSkipping;
    }

    // Start skipping world on an interval (rate/tick)
    public void skip() {
        isSkipping = true;
        runTaskTimer(this.wpcraft, 0, 1);

        // TODO Add boss bar that says skipping
    }
    
}
