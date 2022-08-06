package cloud.wpcom.tasks;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import cloud.wpcom.WPCraft;
import cloud.wpcom.commandsleeper.CSUtil;
import cloud.wpcom.commandsleeper.CommandSleeper;

public class CSVirtualBedExpiryTask extends BukkitRunnable {

    public final Player player;
    public final WPCraft wpcraft;
    public final CommandSleeper commandSleeper;

    public CSVirtualBedExpiryTask(WPCraft wpcraft, CommandSleeper commandSleeper, Player player) {
        this.wpcraft = wpcraft;
        this.player = player;
        this.commandSleeper = commandSleeper;
        runTaskLater(this.wpcraft, 600L);
    }

    @Override
    public void run() {
        // Remove from list of command sleepers
        commandSleeper.removeCommandSleeper(player);

        // Send a message saying the virtual bed has expired
        wpcraft.getServer()
                .broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                        "&f[&bWPCraft&f] &e[player]'s &fvirtual bed expired. ([sleeping]/[needed])"
                                .replace("[player]", player.getName())
                                .replace("[sleeping]", CSUtil.getNumberSleeping(player.getWorld(), commandSleeper).toString())
                                .replace("[needed]", CSUtil.getNeededToSleep(player.getWorld()).toString())));

    }

    @Override
    public void cancel() {
        super.cancel();
    }

    // Cancel with forcibly removed message
    public void cancelByPlayer(Player playerForcing) {
        wpcraft.getServer()
                .broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                        "&f[&bWPCraft&f] &e[player]'s &fvirtual bed was swiped from under them by &e[playerForcing]!"
                                .replace("[player]", player.getName())
                                .replace("[sleeping]", CSUtil.getNumberSleeping(player.getWorld(), commandSleeper).toString())
                                .replace("[needed]", CSUtil.getNeededToSleep(player.getWorld()).toString())
                                .replace("[playerForcing]", playerForcing.getName())));

        cancel();
    }
    
}
