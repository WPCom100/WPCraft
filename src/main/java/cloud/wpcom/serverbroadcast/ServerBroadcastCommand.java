package cloud.wpcom.serverbroadcast;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import cloud.wpcom.WPCraft;

import org.bukkit.ChatColor;


public class ServerBroadcastCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] mssg) {

        // Concatinate args and check for color codes
        String message = "";
        for (String m : mssg) {
            message += m;
        }
        message = ChatColor.translateAlternateColorCodes('&', message);

        // Send message to online players, capturing number of players sent (including the console)
        Integer playersSent = sender.getServer().broadcastMessage(WPCraft.PREFIX + message);
       
        if (sender instanceof Player) { // If the sender is a player
            
            // Provide confirmation through PM
            ((Player)sender).sendMessage(WPCraft.PREFIX + WPCraft.PM_PREFIX + " Your message was sent to " + (playersSent - 1) + " player(s)");

        } else { // Handle other senders, AKA console

            // Provide confirmation through the console
            sender.getServer().getLogger().info("[WPCraft] Your message was sent to " + (playersSent - 1) + " player(s)");
        }
        
        return true;

    }
}