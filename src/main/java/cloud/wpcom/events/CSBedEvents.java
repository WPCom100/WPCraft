package cloud.wpcom.events;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;

import cloud.wpcom.WPCraft;
import cloud.wpcom.commandsleeper.CSUtil;
import cloud.wpcom.commandsleeper.CommandSleeper;
import cloud.wpcom.tasks.CSNightWatcherTask;

public class CSBedEvents implements Listener {

    private final WPCraft wpcraft;
    private final CommandSleeper commandSleeper;
    private Map<World, CSNightWatcherTask> watchers = new HashMap<>(); //TODO Move to own module

    public CSBedEvents(WPCraft wpcraft, CommandSleeper commandSleeper) {
        this.wpcraft = wpcraft;
        this.commandSleeper = commandSleeper;

        // Create a night watcher for all worlds on the server
        wpcraft.getServer().getWorlds().forEach(world -> watchers.put(world, new CSNightWatcherTask(wpcraft, commandSleeper, world)));
    }
 
    @EventHandler
    public void playerEnterBed(PlayerBedEnterEvent event) {

        // Check to make sure the player entered the bed successfuly
        if (event.getBedEnterResult() != PlayerBedEnterEvent.BedEnterResult.OK)
            return;

        wpcraft.getServer()
                .broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                        "&f[&bWPCraft&f] &e[player] &fgot in bed ([sleeping]/[needed])"
                                .replace("[player]", event.getPlayer().getName())
                                .replace("[sleeping]", CSUtil.getNumberSleeping(event.getPlayer().getWorld(), commandSleeper).toString())
                                .replace("[needed]", CSUtil.getNeededToSleep(event.getPlayer().getWorld()).toString())));

        // Check if watcher is running, if not start it for that world
        if (!watchers.get(event.getPlayer().getWorld()).isWatching()) {
            watchers.get(event.getPlayer().getWorld()).watch();
        }
    }

    @EventHandler
    public void playerLeaveBed(PlayerBedLeaveEvent event) {
        
        // Check if this was the only physical sleeper
        if (!CSUtil.hasPhysicalSleeper(event.getPlayer().getWorld())) {
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
        wpcraft.getServer()
                .broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                        "&f[&bWPCraft&f] &e[player] &fgot out of bed ([sleeping]/[needed])"
                                .replace("[player]", event.getPlayer().getName())
                                .replace("[sleeping]", CSUtil.getNumberSleeping(event.getPlayer().getWorld(), commandSleeper).toString())
                                .replace("[needed]", CSUtil.getNeededToSleep(event.getPlayer().getWorld()).toString())));
    }
}