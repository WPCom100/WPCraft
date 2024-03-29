package cloud.wpcom.commandsleeper;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import cloud.wpcom.WPCraft;

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
                        "&f[&bWPCraft&f] &e[player]&f's virtual bed expired. ([sleeping]/[needed])"
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
                        "&f[&bWPCraft&f] &e[player]&f's was robbed by &e[playerForcing]&f!"
                                .replace("[player]", player.getName())
                                .replace("[sleeping]", CSUtil.getNumberSleeping(player.getWorld(), commandSleeper).toString())
                                .replace("[needed]", CSUtil.getNeededToSleep(player.getWorld()).toString())
                                .replace("[playerForcing]", playerForcing.getName())));

        cancel();
    }
    
}
