package cloud.wpcom.events;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;

import cloud.wpcom.WPCraft;
import cloud.wpcom.commandsleeper.CSUtil;
import cloud.wpcom.commandsleeper.CommandSleeper;

public class CommandSleeperEvents implements Listener {

    private final WPCraft wpcraft;
    private final CommandSleeper commandSleeper;

    public CommandSleeperEvents(WPCraft wpcraft, CommandSleeper commandSleeper) {
        this.wpcraft = wpcraft;
        this.commandSleeper = commandSleeper;
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

        // If night skipper isnt running, start it
    }

    @EventHandler
    public void playerLeaveBed(PlayerBedLeaveEvent event) {
        
        // Check if this was the only physical sleeper
        if (!CSUtil.hasPhysicalSleeper(event.getPlayer())) {
            // If so, send a message and forcefully remove command sleepers
            wpcraft.getServer()
                    .broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                            "&f[&bWPCraft&f] &e[player] &fgot out of bed!"
                                    .replace("[player]", event.getPlayer().getName())));

            commandSleeper.forcefullyClearCommandSleepers(event.getPlayer());
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