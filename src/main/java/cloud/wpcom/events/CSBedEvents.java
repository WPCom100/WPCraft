package cloud.wpcom.events;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import cloud.wpcom.WPCraft;
import cloud.wpcom.commandsleeper.CSUtil;
import cloud.wpcom.commandsleeper.CommandSleeper;
import cloud.wpcom.tasks.CSNightWatcherTask;

public class CSBedEvents implements Listener {

    private final WPCraft wpcraft;
    private final CommandSleeper commandSleeper;
    private Map<World, CSNightWatcherTask> watchers = new HashMap<>();

    public CSBedEvents(WPCraft wpcraft, CommandSleeper commandSleeper) {
        this.wpcraft = wpcraft;
        this.commandSleeper = commandSleeper;

        // Create a night watcher task for all worlds on the server
        wpcraft.getServer().getWorlds()
                .forEach(world -> watchers.put(world, new CSNightWatcherTask(wpcraft, commandSleeper, world)));
    }
 
    @EventHandler
    public void playerEnterBed(PlayerBedEnterEvent event) {

        // Check to make sure the player entered the bed successfuly
        if (event.getBedEnterResult() != PlayerBedEnterEvent.BedEnterResult.OK)
            return;

        new BukkitRunnable() {
            @Override
            public void run() {
                wpcraft.getServer()
                        .broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                                "&f[&bWPCraft&f] &e[player] &fgot in bed ([sleeping]/[needed])"
                                        .replace("[player]", event.getPlayer().getName())
                                        .replace("[sleeping]",
                                                CSUtil.getNumberSleeping(event.getPlayer().getWorld(), commandSleeper)
                                                        .toString())
                                        .replace("[needed]",
                                                CSUtil.getNeededToSleep(event.getPlayer().getWorld()).toString())));                  
            }
        }.runTask(wpcraft);

        // Check if skipper is already running
        if (watchers.get(event.getPlayer().getWorld()).getSkipper().isSkipping()) {
            return;
        }
        
        // Check if watcher is running , if not create and start
        if (!watchers.get(event.getPlayer().getWorld()).isWatching()) {
            watchers.replace(event.getPlayer().getWorld(),
                    new CSNightWatcherTask(wpcraft, commandSleeper, event.getPlayer().getWorld()));
            watchers.get(event.getPlayer().getWorld()).watch();
        }
    }

    @EventHandler
    public void playerLeaveBed(PlayerBedLeaveEvent event) {
        
        // If skipper does not exist
        if (watchers.get(event.getPlayer().getWorld()).getSkipper() == null) {
            return;
        }

        // If skipper is already running
        if (watchers.get(event.getPlayer().getWorld()).getSkipper().isSkipping()) {
            return;
        }

        // Check if this was the only physical sleeper
        if (CSUtil.getNumberPhysicalSleepers(event.getPlayer().getWorld()) == 1 && commandSleeper.count() > 0) {
            // If so, send a message and forcefully remove command sleepers
            wpcraft.getServer()
                    .broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                            "&f[&bWPCraft&f] &e[player] &fgot out of bed!"
                                    .replace("[player]", event.getPlayer().getName())));

            commandSleeper.forcefullyClearCommandSleepers(event.getPlayer());

            // Stop watching the world
            watchers.get(event.getPlayer().getWorld()).cancel();
            return;
        }

        // If not, just send a message
        new BukkitRunnable() {
            @Override
            public void run() {
                wpcraft.getServer()
                .broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                        "&f[&bWPCraft&f] &e[player] &fgot out of bed ([sleeping]/[needed])"
                                .replace("[player]", event.getPlayer().getName())
                                .replace("[sleeping]", CSUtil.getNumberSleeping(event.getPlayer().getWorld(), commandSleeper).toString())
                                .replace("[needed]",
                                        CSUtil.getNeededToSleep(event.getPlayer().getWorld()).toString())));
            }
        }.runTask(wpcraft);
    }
}