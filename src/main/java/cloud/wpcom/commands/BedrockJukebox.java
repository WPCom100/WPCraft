package cloud.wpcom.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import cloud.wpcom.WPCraft;
import cloud.wpcom.bedrockjukebox.JukeboxWrapper;

public class BedrockJukebox implements CommandExecutor {
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] mssg) {


    for (JukeboxWrapper j : WPCraft.jb.getJukeboxes()) {

        sender.getServer().broadcastMessage(j.getblock().toString() + j.getLocation().toString());

    }

        return true;
    }
} 
