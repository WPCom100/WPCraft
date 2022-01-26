package cloud.wpcom;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ServerBroadcast implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] mssg) {

        // Combine all message arguments to a single string
        String message = WPCraft.PREFIX;
        for (int i = 0; i < mssg.length; i++) {
            message += " " + mssg[i];
        }

        // Send message to online players, capturing number of players sent (including the console)
        Integer playersSent = sender.getServer().broadcastMessage(message);
       
        if (sender instanceof Player) { // If the sender is a player
            
            // Provide confirmation through PM if the sender is a player
            ((Player)sender).sendMessage(WPCraft.PREFIX + WPCraft.PM_PREFIX + " Your message was sent to " + (playersSent - 1) + " player(s)");

        } else { // Handle other senders, AKA console

            // Provide confirmation to the console
            sender.getServer().getLogger().info("[WPCraft] Your message was sent to " + (playersSent - 1) + " player(s)");
        }
        
        return true;

    }
}