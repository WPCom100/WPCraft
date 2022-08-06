package cloud.wpcom.events;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import cloud.wpcom.WPCraft;

public class JoinMessageEvents implements Listener {
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        event.setJoinMessage(WPCraft.PREFIX                                                                  // Prefix
                + '[' + ChatColor.GREEN + '+' + ChatColor.WHITE + "] "                                       // Join indicator
                + ChatColor.YELLOW + event.getPlayer().getName() + ChatColor.WHITE + ", Welcome back!");     // Message itself

    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {

        event.setQuitMessage(WPCraft.PREFIX                                                                                                       // Prefix
                            + '[' + ChatColor.RED + '-' + ChatColor.WHITE + ']'                                                                   // Leave indicator
                            + ChatColor.WHITE + " See you next time " + ChatColor.YELLOW + event.getPlayer().getName() + ChatColor.WHITE + '!');  // Message itself

    }

}
